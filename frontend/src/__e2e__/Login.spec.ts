import { test, expect } from '@playwright/test';

const baseURL = process.env.BASE_URL ?? 'http://localhost:3000';
const bceidUser = process.env.TEST_BCEID_USERNAME ?? '';
const bceidPassword = process.env.TEST_BCEID_PASSWORD ?? '';

test('visualize login flow', async ({ page }) => {
  await page.goto(baseURL);
  await page.click('[data-testid="landing-button__bceid"]');

  await page.waitForSelector('#user');
  await page.fill('#user', bceidUser);

  await page.fill('#password', bceidPassword);

  await page.click('input[name="btnSubmit"]');

  await page.waitForURL('**/dashboard');

  await expect(page.getByRole('heading', { name: 'Dashboard' })).toBeVisible();
});
