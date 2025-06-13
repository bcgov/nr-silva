import { expect } from '@playwright/test';
import { test } from './setup/coverage';
import { loadStub } from './utils/stubLoader';

test.describe('Dashboard', () => {
  test.beforeEach(async ({ page }) => {
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
  });

  test('Load and check', async ({ page }) => {

    await page.goto('/');
    await expect(page).toHaveTitle('Silva');
    await page.waitForURL('**/dashboard');
    await expect(page.getByRole('heading', { name: 'Dashboard' })).toBeVisible();
  });

});
