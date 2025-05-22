import { expect, test } from '@playwright/test';

test('Dashboard', async ({ page }) => {
  await page.goto('/');
  await expect(page).toHaveTitle(/Silva/);
  await page.waitForURL('**/dashboard');
  await expect(page.getByRole('heading', { name: 'Dashboard' })).toBeVisible();
});
