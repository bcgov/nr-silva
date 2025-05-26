import { chromium, expect } from '@playwright/test';
import path from 'path';
import { fileURLToPath } from 'url';
import { TWO_MINUTE } from '@/constants/TimeUnits';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const authFile = path.join(__dirname, './user.json');
const baseURL = process.env.BASE_URL ?? 'http://localhost:3000';
const bceidUser = process.env.TEST_BCEID_USERNAME ?? '';
const bceidPassword = process.env.TEST_BCEID_PASSWORD ?? '';


async function globalSetup() {
  console.log(`Global setup - Base URL: ${baseURL}`);

  const browser = await chromium.launch();
  const page = await browser.newPage();

  await page.goto(baseURL);
  await page.click('[data-testid="landing-button__bceid"]');

  await page.waitForSelector('#user');
  await page.fill('#user', bceidUser);
  await page.fill('#password', bceidPassword);
  await page.click('input[name="btnSubmit"]');

  // DEBUG
  await page.waitForTimeout(5000);
  let currentURL = page.url();
  let pageTitle = await page.title();
  console.log(`[debug] URL: ${currentURL}, Title: ${pageTitle}`);

  // DEBUG
  await page.waitForTimeout(5000);
  currentURL = page.url();
  pageTitle = await page.title();
  console.log(`[debug] URL: ${currentURL}, Title: ${pageTitle}`);

  // DEBUG
  await page.waitForTimeout(5000);
  currentURL = page.url();
  pageTitle = await page.title();
  console.log(`[debug] URL: ${currentURL}, Title: ${pageTitle}`);

  // DEBUG
  await page.waitForTimeout(5000);
  currentURL = page.url();
  pageTitle = await page.title();
  console.log(`[debug] URL: ${currentURL}, Title: ${pageTitle}`);

  console.log(`[debug] content: ${page.content}`);

  await page.waitForURL('**/dashboard', { timeout: TWO_MINUTE });
  await expect(page.getByRole('heading', { name: 'Dashboard' })).toBeVisible();

  await page.context().storageState({ path: authFile });
  await browser.close();
};


export default globalSetup;
