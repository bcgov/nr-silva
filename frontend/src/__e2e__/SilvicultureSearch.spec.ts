import { expect } from '@playwright/test';
import { test } from './setup/coverage';

test('Navigate to Silviculture Search', async ({ page }) => {
  // Go to dashboard
  await page.goto('/');
  await expect(page).toHaveTitle('Silva');
  await page.waitForURL('**/dashboard');
  await expect(page.getByRole('heading', { name: 'Dashboard' })).toBeVisible();

  // Click on the side nav link
  const silvicultureLink = page.locator('a.side-nav-item:has-text("Silviculture search")');
  await silvicultureLink.waitFor({ state: 'visible' });
  await silvicultureLink.click();
  await page.waitForURL('**/silviculture-search');
  await expect(page).toHaveTitle('Silviculture Search - Silva');
});
