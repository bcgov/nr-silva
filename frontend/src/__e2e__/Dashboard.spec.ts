import { expect } from '@playwright/test';
import { test } from './setup/coverage';

test('Dashboard', async ({ page }) => {
  await page.goto('/');
  await expect(page).toHaveTitle('Silva');
  await page.waitForURL('**/dashboard');
  await expect(page.getByRole('heading', { name: 'Dashboard' })).toBeVisible();
});
