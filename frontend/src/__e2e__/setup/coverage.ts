import fs from 'fs';
import path from 'path';
import { test as base, TestInfo, Page } from '@playwright/test';
import v8toIstanbul from 'v8-to-istanbul';

const COVERAGE_DIR = path.resolve(process.cwd(), '.nyc_output');

export const test = base.extend<{
  context: any;
  page: Page;
}>({
  context: async ({ browser }, use) => {
    const context = await browser.newContext();
    await use(context);
    await context.close(); // clean up
  },

  page: async ({ context }, use) => {
    const page = await context.newPage();
    await page.coverage.startJSCoverage({ reportAnonymousScripts: true });
    await use(page);

    const jsCoverage = await page.coverage.stopJSCoverage();

    if (!fs.existsSync(COVERAGE_DIR)) {
      fs.mkdirSync(COVERAGE_DIR, { recursive: true });
    }

    for (const entry of jsCoverage) {
      if (!entry.url.startsWith('http://localhost:3000/src')) continue;

      const absPath = entry.url.replace('http://localhost:3000', process.cwd());
      if (!fs.existsSync(absPath)) {
        console.warn('[coverage] Skipping, file not found:', absPath);
        continue;
      }

      const sourceCode = fs.readFileSync(absPath, 'utf-8');
      const converter = v8toIstanbul(absPath, 0, { source: sourceCode });
      await converter.load();
      converter.applyCoverage(entry.functions);

      const istanbulCoverage = converter.toIstanbul();
      const fileName = `coverage-${path.basename(absPath).replace(/\W+/g, '_')}-${Date.now()}.json`;
      const outPath = path.join(COVERAGE_DIR, fileName);
      fs.writeFileSync(outPath, JSON.stringify(istanbulCoverage, null, 2));
      console.log('[coverage] Written:', outPath);
    }
  },
});
