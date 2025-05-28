import { chromium, firefox, webkit, expect, type FullConfig, type BrowserType } from '@playwright/test';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const baseURL = process.env.BASE_URL ?? 'http://localhost:3000';
const bceidUser = process.env.TEST_BCEID_USERNAME ?? '';
const bceidPassword = process.env.TEST_BCEID_PASSWORD ?? '';


function getBrowserType(projectName: string): BrowserType {
  switch (projectName) {
    case 'firefox':
      return firefox;
    case 'webkit':
      return webkit;
    case 'chromium':
    default:
      return chromium;
  }
}

async function globalSetup(config: FullConfig) {
  if (!bceidUser || !bceidPassword) {
    throw new Error('No BCeID credential.')
  }

  const projectName = config.projects[0].name;
  const authFile = path.join(__dirname, `./user.${projectName}.json`);

  console.log(authFile);

  console.log(`Global setup - Base URL: ${baseURL}`);

  const browser = await getBrowserType(projectName).launch();
  const page = await browser.newPage();

  await page.goto(baseURL);
  await page.click('[data-testid="landing-button__bceid"]');

  await page.waitForSelector('#user');
  await page.fill('#user', bceidUser);
  await page.fill('#password', bceidPassword);
  await page.click('input[name="btnSubmit"]');

  await page.waitForURL('**/dashboard');
  await expect(page.getByRole('heading', { name: 'Dashboard' })).toBeVisible();

  await page.context().storageState({ path: authFile });
  await browser.close();
};


export default globalSetup;
