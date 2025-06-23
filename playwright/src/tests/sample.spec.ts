import { test, expect, type Page } from '@playwright/test';
import { QuantityAttribute, validAttributes } from '../steps/stepTypes';

test.beforeEach(async ({ page }) => {
  await page.goto('/');
  await expect(page).toHaveTitle('Silva');
  await page.waitForURL('**/dashboard');
});

test.describe('Basic validation', () => {

  test('has title', async ({ page }) => {
    await expect(page.getByRole('heading', { name: 'Dashboard' })).toBeVisible();
  });

  test('has navigation links', async ({ page }) => {
    const silvicultureLink = page.locator('a.side-nav-item:has-text("Silviculture search")');
    await silvicultureLink.waitFor({ state: 'visible' });
  });

  test('navigates to Silviculture Search page', async ({ page }) => {
    const silvicultureLink = page.locator('a.side-nav-item:has-text("Silviculture search")');
    await silvicultureLink.click();
    await page.waitForURL('**/silviculture-search');
    await expect(page).toHaveTitle('Silviculture Search - Silva');  
  });


});
