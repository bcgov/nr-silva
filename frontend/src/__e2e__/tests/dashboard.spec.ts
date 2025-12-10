import { expect, Page } from '@playwright/test';
import { test } from '../setup/coverage';
import { loadStub } from '../utils/stubLoader';
import { DashboardPage } from '../pages/dashboard-page';

test.describe('Dashboard', () => {
  let dashboardPage: DashboardPage;

  test.beforeEach(async ({ page }) => {
    dashboardPage = new DashboardPage(page);

    // Intercept the recent openings API call and return a stubbed response
    await page.route('**/api/openings/recent**', async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub('openings', 'recent-start.json'),
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
        body: loadStub('users', 'submission-trends.json'),
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

    await dashboardPage.goto();
  });

  test('Load and check', async ({ page }) => {
    await page.waitForURL('**/dashboard');
    expect(await dashboardPage.getTitle()).toBe('Silva');
    expect(await dashboardPage.getHeading()).toBe('Dashboard');
  });

  test('sections should be visible', async () => {
    // expect(await dashboardPage.isRecentOpeningsSectionVisible()).toBe(true);
    expect(await dashboardPage.isOpeningSubmissionsTrendSectionVisible()).toBe(true);
    expect(await dashboardPage.isFavouritesSectionVisible()).toBe(true);
  });

  test('map button toggles map visibility', async () => {
    const initialMapVisibility = await dashboardPage.isMapVisible();
    await dashboardPage.showMap();
    expect(await dashboardPage.isMapVisible()).toBe(!initialMapVisibility);
    await dashboardPage.hideMap();
    expect(await dashboardPage.isMapVisible()).toBe(initialMapVisibility);
  });

  test('recent openings count should be correct', async () => {
    const count = await dashboardPage.getRecentOpeningsCount();
    expect(count).toBe(2);
  });


  test('recent opening should have correct data', async () => {
    const openingData = await dashboardPage.getOpeningRowDataByOpeningId('1004185');
    const expectedData = ['1004185', 'TFL47', 'FTMLForest Tenure - Major Licensee', 'Free Growing', '12U', '47/12U', '12-44', '79.4', 'Sep 08, 2004'];
    expect(openingData).toEqual(expectedData);
  });

  test('favourite and unfavourite opening', async ({ page }) => {
    const openingId = '60000';
    await page.route(`**/api/openings/favourites/**`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: 'false',
      });
    });

    expect(await dashboardPage.isOpeningFavourited(openingId)).toBe(false);
    await page.route(`**/api/openings/favourites/${openingId}`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: 'true',
      });
    });
    await dashboardPage.favouriteOpening(openingId);

    expect(await dashboardPage.isOpeningFavourited(openingId)).toBe(true);

    await page.route(`**/api/openings/favourites/${openingId}`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: 'false',
      });
    });
    await dashboardPage.unfavouriteOpening(openingId);
    expect(await dashboardPage.isOpeningFavourited(openingId)).toBe(false);
  });

  test('favourite opening should be visible in favourites section', async ({ page }) => {
    const openingId = '1004185';

    expect(await dashboardPage.isOpeningFavouritedOnFavouriteSection(openingId)).toBe(true);
  });

  test('opening trend fields should be okay to fill', async () => {
    await dashboardPage.chooseDistrict('DAS - Development Unit');
    await dashboardPage.chooseStatus('AMD - Amended');
    await dashboardPage.chooseSubmissionYear('2023');
  });
});
