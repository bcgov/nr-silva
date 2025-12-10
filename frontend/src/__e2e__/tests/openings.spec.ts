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

    await page.route(`**/api/openings/favourites/**`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: 'false',
      });
    });

    await openingsPage.goto();
  });

  test('map button should hide and reveal map', async () => {
    expect(await openingsPage.isMapVisible()).toBe(true);
    await openingsPage.toggleMapButton();
    expect(await openingsPage.isMapVisible()).toBe(false);
  });

  test('favourite button should toggle favorite status', async ({ page }) => {

    expect(await openingsPage.isOpeningFavourited(openingId)).toBe(false);

    await page.route(`**/api/openings/favourites/**`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: 'true',
      });
    });

    await openingsPage.favouriteOpening(openingId);

    expect(await openingsPage.isOpeningFavourited(openingId)).toBe(true);

    await page.route(`**/api/openings/favourites/**`, async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: 'false',
      });
    });

    await openingsPage.unfavouriteOpening(openingId);

    expect(await openingsPage.isOpeningFavourited(openingId)).toBe(false);
  });
});
