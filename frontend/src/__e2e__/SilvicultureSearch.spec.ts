import { expect } from '@playwright/test';
import { test } from './setup/coverage';
import { loadStub } from './utils/stubLoader';

test.describe('Silviculture Search', () => {

  test.beforeEach(async ({ page }) => {

    // Intercept global data calls return a stubbed response
    // Intercept the recent openings API call and return a stubbed response
    await page.route('**/api/openings/recent**', async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub('openings','recent-start.json'),
      });
    });

    // Intercept the org units API call and return a stubbed response
    await page.route('**/api/codes/org-units**', async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub('codes/org-units.json'),
      });
    })
    
    // Intercept the user submission trends API call and return a stubbed response
    await page.route('**/api/users/submission-trends**', async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub('users','submission-trends.json'),
      });
    });

    // Intercept the favorites API call and return a stubbed response
    await page.route('**/api/openings/favourites**', async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub('openings/favourites.json'),
      });
    });

    //Intercept the categories API call and return a stubbed response
    await page.route('**/api/codes/categories**', async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub('codes/categories.json'),
      });
    });

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

  test('Navigate to Silviculture Search', async ({ page }) => {
    await expect(page.getByRole('heading', { name: 'Silviculture Search' })).toBeVisible();  
  });

});