import test, { expect } from "@playwright/test";
import { OpeningsPage } from "../pages/openings-page";
import { loadStub } from "../utils/stubLoader";

test.describe('Openings', () => {
  let openingsPage: OpeningsPage;
  const openingId = '60000';

  test.beforeEach(async ({ page }) => {
    openingsPage = new OpeningsPage(page);

    await page.route('**/api/openings/recent', async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub('openings/recent-start.json'),
      });
    });

    await page.route(`**/api/openings/map/${openingId}**`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub(`openings/map`, 'opening.json'),
      });
    });

    await page.route(`**/api/openings/favourites`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([]),
      });
    });

    await page.route(`**/api/openings/favourites/**`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
      });
    });

    await openingsPage.goto();
    await page.waitForLoadState('networkidle');
  });

  test('map button should hide and reveal map', async ({ page }) => {
    // Wait for the map to finish loading so the test isn't flaky when run in sequence
    await page.waitForSelector('.leaflet-container', { state: 'visible', timeout: 5000 });
    expect(await openingsPage.isMapVisible()).toBe(true);
    await openingsPage.toggleMapButton();
    expect(await openingsPage.isMapVisible()).toBe(false);
  });

  test('favourite button should toggle favorite status', async ({ page }) => {

    expect(await openingsPage.isOpeningFavourited(openingId)).toBe(false);

    await page.unroute(`**/api/openings/favourites`);
    await page.route(`**/api/openings/favourites`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        // make sure we return numbers, not strings, so `includes(openingId:number)` matches
        body: JSON.stringify([Number(openingId)]),
      });
    });

    // Click the favourite button which triggers a PUT and then a refetch of GET /api/openings/favourites
    await Promise.all([
      // wait specifically for the GET /api/openings/favourites response (not PUT/DELETE to /api/openings/favourites/{id})
      page.waitForResponse(resp => resp.url().endsWith('/api/openings/favourites') && resp.status() === 200),
      openingsPage.favouriteOpening(openingId),
    ]);

    // wait for the UI to show the filled bookmark icon
    const favBtn = page.getByTestId(`actionable-bookmark-button-${openingId}`);
    await favBtn.locator('.bookmark-filled-icon').waitFor({ state: 'visible' });

    expect(await openingsPage.isOpeningFavourited(openingId)).toBe(true);

    await page.unroute(`**/api/openings/favourites`);
    await page.route(`**/api/openings/favourites`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([]),
      });
    });

    // Click the unfavourite button which triggers a DELETE and then a refetch of GET /api/openings/favourites
    await Promise.all([
      // wait specifically for the GET /api/openings/favourites response (not PUT/DELETE to /api/openings/favourites/{id})
      page.waitForResponse(resp => resp.url().endsWith('/api/openings/favourites') && resp.status() === 200),
      openingsPage.unfavouriteOpening(openingId),
    ]);

    // wait for the filled bookmark icon to disappear
    await favBtn.locator('.bookmark-filled-icon').waitFor({ state: 'detached' });

    expect(await openingsPage.isOpeningFavourited(openingId)).toBe(false);
  });
});
