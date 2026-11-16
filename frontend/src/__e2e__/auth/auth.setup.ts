import { chromium, firefox, webkit, expect } from '@playwright/test';
import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const baseURL = process.env.BASE_URL ?? 'http://localhost:3000';
const bceidUser = process.env.TEST_BCEID_USERNAME ?? '';
const bceidPassword = process.env.TEST_BCEID_PASSWORD ?? '';
const isAllBrowsers = process.env.ALL_BROWSERS === 'true';

const browserMap = {
  chromium,
  firefox,
  webkit,
} as const;

async function loginAndSaveStorage(browserTypeName: keyof typeof browserMap) {
  const browserType = browserMap[browserTypeName];
  const browser = await browserType.launch();
  const page = await browser.newPage();

  console.log(`Global setup - Browser: ${browserTypeName}, url: ${baseURL}`)

  await page.goto(baseURL);

  await page.click('[data-testid="landing-button__bceid"]');
  await page.waitForSelector('#user');
  await page.fill('#user', bceidUser);
  await page.fill('#password', bceidPassword);
  await page.click('input[name="btnSubmit"]');
  await page.waitForURL('**/dashboard');
  await page.waitForLoadState('networkidle');

  await expect(page.getByRole('heading', { name: 'Dashboard' })).toBeVisible();

  const authFile = path.join(__dirname, `./user.${browserTypeName}.json`);
  await page.context().storageState({ path: authFile });

  const sessionStorageEntries = await page.evaluate(() => {
    const entries: Record<string, string> = {};
    for (let i = 0; i < window.sessionStorage.length; i += 1) {
      const key = window.sessionStorage.key(i);
      if (key) {
        entries[key] = window.sessionStorage.getItem(key) ?? '';
      }
    }
    return entries;
  });
  const sessionFile = path.join(__dirname, `./user.${browserTypeName}.session.json`);
  fs.writeFileSync(sessionFile, JSON.stringify(sessionStorageEntries, null, 2));

  console.log(`[globalSetup] Created: ${authFile} and ${sessionFile}`);
  await browser.close();
}

async function globalSetup() {
  if (!bceidUser || !bceidPassword) {
    throw new Error('No BCeID credentials set in environment variables');
  }

  if (isAllBrowsers) {
    for (const name of Object.keys(browserMap)) {
      await loginAndSaveStorage(name as keyof typeof browserMap);
    }
  } else {
    await loginAndSaveStorage('chromium');
  }
}

export default globalSetup;
