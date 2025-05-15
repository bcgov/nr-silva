import { chromium } from '@playwright/test';

const baseURL = process.env.BASE_URL ?? 'http://localhost:3000';

async function globalSetup() {
  const browser = await chromium.launch();
  const page = await browser.newPage();

  await page.goto(baseURL);

  await page.click('[data-testid="landing-button__bceid"]');

  await page.waitForSelector('#user');
  await page.fill('#user', 'user');

  // await page.fill('#username', 'user');
  // await page.fill('#password', 'pass');
  // await page.click('button[type="submit"]');
  // await page.waitForURL('/dashboard');

  // // Save login session
  // await page.context().storageState({ path: 'storageState.json' });
  await browser.close();
}

export default globalSetup;
