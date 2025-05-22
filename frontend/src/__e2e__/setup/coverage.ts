import fs from 'fs';
import path from 'path';
import { test as base } from '@playwright/test';
import v8toIstanbul from 'v8-to-istanbul';

const COVERAGE_DIR = path.resolve(process.cwd(), '.nyc_output');

export const test = base.extend({
  context: async ({ browser }, use) => {
    const context = await browser.newContext();
    const page = await context.newPage();
    await page.coverage.startJSCoverage({ reportAnonymousScripts: true });

    await use(context);

    const jsCoverage = await page.coverage.stopJSCoverage();

    if (!fs.existsSync(COVERAGE_DIR)) {
      fs.mkdirSync(COVERAGE_DIR, { recursive: true });
    }

    console.log('[debug] jsCoverage result:', JSON.stringify(jsCoverage, null, 2));

    for (const entry of jsCoverage) {
      if (!entry.url.startsWith('http://localhost:3000')) continue;

      const relPath = entry.url.replace('http://localhost:3000', process.cwd());
      if (!fs.existsSync(relPath)) {
        console.warn(`[coverage] Skipping (not found): ${relPath}`);
        continue;
      }

      const converter = v8toIstanbul(relPath, 0, {
        source: fs.readFileSync(relPath, 'utf-8'),
      });

      await converter.load();
      converter.applyCoverage(entry.functions);

      const istanbulCoverage = converter.toIstanbul();
      const outPath = path.join(COVERAGE_DIR, `coverage-${Date.now()}.json`);
      fs.writeFileSync(outPath, JSON.stringify(istanbulCoverage), 'utf-8');
      console.log(`[coverage] Written: ${outPath}`);
    }
  },
});
