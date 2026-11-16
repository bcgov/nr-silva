import fs from 'fs';
import path from 'path';
import { test as base, Page } from '@playwright/test';
import v8toIstanbul from 'v8-to-istanbul';

const COVERAGE_DIR = path.resolve(process.cwd(), '.nyc_output');

export const test = base.extend<{
  page: Page;
}>({
  page: async ({ page }, use, testInfo) => {
    const storageStatePath = testInfo.project.use.storageState;
    if (typeof storageStatePath === 'string') {
      const resolvedStoragePath = path.isAbsolute(storageStatePath)
        ? storageStatePath
        : path.resolve(process.cwd(), storageStatePath);
      const sessionFile = resolvedStoragePath.replace(/\.json$/, '.session.json');
      if (fs.existsSync(sessionFile)) {
        const sessionEntries = JSON.parse(fs.readFileSync(sessionFile, 'utf-8')) as Record<string, string>;
        const baseURL = testInfo.project.use.baseURL;
        const targetOrigin = baseURL ? new URL(baseURL).origin : null;
        await page.context().addInitScript(
          ({ entries, origin }: { entries: Record<string, string>; origin: string | null }) => {
            if (origin && window.location.origin !== origin) {
              return;
            }
            if (window.sessionStorage.length === 0) {
              for (const [key, value] of Object.entries(entries)) {
                window.sessionStorage.setItem(key, value);
              }
            }
          },
          { entries: sessionEntries, origin: targetOrigin }
        );
      }
    }

    const isChromium = testInfo.project.name === 'chromium';

    if (isChromium) {
      await page.coverage.startJSCoverage({ reportAnonymousScripts: true });
    }

    await use(page);

    if (!isChromium) return;

    const jsCoverage = await page.coverage.stopJSCoverage();

    if (!fs.existsSync(COVERAGE_DIR)) {
      fs.mkdirSync(COVERAGE_DIR, { recursive: true });
    }

    for (const entry of jsCoverage) {
      if (!entry.url.startsWith('http://localhost:3000/src')) continue;

      const absPath = entry.url.replace('http://localhost:3000', process.cwd());
      if (!fs.existsSync(absPath)) {
        // Skipping, file not found
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
    }
  },
});
