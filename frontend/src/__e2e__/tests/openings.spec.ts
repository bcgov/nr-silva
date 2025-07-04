import test, { expect } from "@playwright/test";
import { OpeningsPage } from "../pages/openings-page";
import { loadStub } from "../utils/stubLoader";

test.describe('Openings', () => {
  let openingsPage: OpeningsPage;
  const openingId = '60000';

  test.beforeEach(async ({ page }) => {
    openingsPage = new OpeningsPage(page);
    await openingsPage.goto();

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
  });

  test('map button should hide and reveal map', async () => {
    expect(await openingsPage.isMapVisible()).toBe(true);
    await openingsPage.toggleMapButton();
    expect(await openingsPage.isMapVisible()).toBe(false);
  });

  test('favourite button should toggle favorite status', async () => {
    expect(await openingsPage.isOpeningFavourited(openingId)).toBe(false);
    await openingsPage.favouriteOpening(openingId);

    expect(await openingsPage.isFavouriteNotificationVisible(openingId)).toBe(true);
    expect(await openingsPage.isOpeningFavourited(openingId)).toBe(true);

    await openingsPage.unfavouriteOpening(openingId);

    expect(await openingsPage.isOpeningFavourited(openingId)).toBe(false);
    expect(await openingsPage.isUnfavouriteNotificationVisible(openingId)).toBe(true);
  });

  test('check opening should make it visible on the map', async () => {
    await openingsPage.checkOpeningInTable(openingId);
    expect(await openingsPage.isOpeningVisibleOnMap()).toBe(true);
  });
});
