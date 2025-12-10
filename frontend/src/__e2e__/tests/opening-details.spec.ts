import test, { expect } from "@playwright/test";
import { OpeningDetailsPage } from "../pages/opening-details-page";
import { loadStub } from "../utils/stubLoader";

test.describe('Opening Details', () => {
  let openingDetailsPage: OpeningDetailsPage;
  const openingId = '60000';


  test.beforeEach(async ({ page }) => {
    openingDetailsPage = new OpeningDetailsPage(page, openingId);

    await page.route(`**/api/openings/favourites/${openingId}`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub(`openings/details/opening-${openingId}`, 'favourites.json'),
      });
    });

    await page.route(`**/api/openings/${openingId}/tombstone`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub(`openings/details/opening-${openingId}`, 'tombstone.json'),
      });
    });

    await page.route(`**/api/openings/${openingId}/tombstone`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub(`openings/details/opening-${openingId}`, 'tombstone.json'),
      });
    });

    await page.route(`**/api/openings/${openingId}/tenures?filter=*&page=0&size=10`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub(`openings/details/opening-${openingId}`, 'tenures-filtered.json'),
      });
    });

    await page.route(`**/api/openings/${openingId}/tenures?page=0&size=10`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub(`openings/details/opening-${openingId}`, 'tenures.json'),
      });
    });

    await page.route(`**/api/openings/${openingId}/ssu`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub(`openings/details/opening-${openingId}`, 'ssu.json'),
      });
    });

    await page.route(`**/api/openings/${openingId}/disturbances*`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub('openings/details', 'disturbances.json'),
      });
    });

    await page.route(`**/api/openings/${openingId}/activities?filter=*&page=0&size=10`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub(`openings/details/opening-${openingId}`, 'activities-filtered.json'),
      });
    });

    await page.route(`**/api/openings/${openingId}/activities?page=0&size=10`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub('openings/details', 'activities.json'),
      });
    });

    await page.route(`**/api/openings/${openingId}/activities/:activityId`, async route => {
      const url = route.request().url();
      const match = url.match(/activities\/(\d+)/);
      const activityId = match ? match[1] : null;
      if (activityId) {
        const fileName = `activities-${activityId}.json`;
        route.fulfill({
          status: 200,
          contentType: 'application/json',
          body: loadStub('openings/details', fileName),
        });
      } else {
        route.fulfill({
          status: 404,
          contentType: 'application/json',
          body: JSON.stringify({ error: 'Not Found' }),
        });
      }
    });

    await page.route(`**/api/openings/${openingId}/cover`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub('openings/details', 'cover.json'),
      });
    });

    await page.route(`**/api/openings/${openingId}/cover?mainSearchTerm=*`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub(`openings/details/opening-${openingId}`, 'cover-filtered.json'),
      });
    });

    await page.route(`**/api/openings/${openingId}/cover/*`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub('openings/details', 'coverdetails.json'),
      });
    });

    await openingDetailsPage.goto();
    await page.waitForLoadState('networkidle');
  });

  test('should load opening details page', async () => {
    expect(await openingDetailsPage.getHeading()).toBe(`Opening ID ${openingId}`);
    expect(await openingDetailsPage.isHeadingVisible()).toBe(true);
  });

  test('should change tabs', async () => {
    await openingDetailsPage.clickOverviewTab();
    expect(await openingDetailsPage.getCurrentActiveTab()).toBe('Overview');

    await openingDetailsPage.clickTenureIdentificationTab();
    expect(await openingDetailsPage.getCurrentActiveTab()).toBe('Tenure identification');

    await openingDetailsPage.clickStandardUnitsTab();
    expect(await openingDetailsPage.getCurrentActiveTab()).toBe('Standard units');

    await openingDetailsPage.clickActivitiesTab();
    expect(await openingDetailsPage.getCurrentActiveTab()).toBe('Activities');

    await openingDetailsPage.clickForestCoverTab();
    expect(await openingDetailsPage.getCurrentActiveTab()).toBe('Forest cover');
  });

  test('map buttons should work correctly', async () => {
    expect(await openingDetailsPage.isMapVisible()).toBe(true);
    await openingDetailsPage.toggleMapButton();
    expect(await openingDetailsPage.isMapVisible()).toBe(false);
    await openingDetailsPage.toggleMapButton();

    await openingDetailsPage.zoomInMap();
    await openingDetailsPage.zoomOutMap();
  });
});
