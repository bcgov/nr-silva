import { chromium, firefox, webkit, expect } from '@playwright/test';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const baseURL = process.env.BASE_URL ?? 'http://localhost:3000';
const bceidUser = process.env.TEST_BCEID_USERNAME ?? '';
const bceidPassword = process.env.TEST_BCEID_PASSWORD ?? '';

const browserMap = {
  chromium,
  firefox,
  webkit,
} as const;

async function loginAndSaveStorage(browserTypeName: keyof typeof browserMap) {
  const browserType = browserMap[browserTypeName];
  const browser = await browserType.launch();
  const page = await browser.newPage();

  await page.goto(baseURL);
  await page.click('[data-testid="landing-button__bceid"]');
  await page.waitForSelector('#user');
  await page.fill('#user', bceidUser);
  await page.fill('#password', bceidPassword);
  await page.click('input[name="btnSubmit"]');

  await page.waitForURL('**/dashboard');
  await expect(page.getByRole('heading', { name: 'Dashboard' })).toBeVisible();

  const authFile = path.join(__dirname, `./user.${browserTypeName}.json`);
  await page.context().storageState({ path: authFile });

  console.log(`[globalSetup] Created: ${authFile}`);
  await browser.close();
}

async function globalSetup() {
  if (!bceidUser || !bceidPassword) {
    throw new Error('No BCeID credentials set in env');
  }

  for (const name of Object.keys(browserMap) as (keyof typeof browserMap)[]) {
    await loginAndSaveStorage(name);
  }
}

export default globalSetup;
