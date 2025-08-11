import { Locator, Page } from "@playwright/test";
import { BasePage } from "./base-page";
import { routes } from "../utils/routes";

export class OpeningsPage extends BasePage {
  private readonly mapButton: Locator;
  private readonly map: Locator;
  private readonly mapZoomInButton: Locator;
  private readonly mapZoomOutButton: Locator;
  private readonly mapExpandButton: Locator;

  private readonly recentOpeningsTable: Locator;
  private readonly recentOpeningsTableRows: Locator;

  constructor(page: Page) {
    super(page, routes.openings());

    this.mapButton = page.locator('.title-section').locator('.map-button');
    this.map = page.locator('.leaflet-container');
    this.mapZoomInButton = this.map.locator('.leaflet-control-zoom-in');
    this.mapZoomOutButton = this.map.locator('.leaflet-control-zoom-out');
    this.mapExpandButton = this.map.locator('.leaflet-control-fullscreen-button');

    this.recentOpeningsTable = page.getByRole('table', { name: 'Recent openings table' });
    this.recentOpeningsTableRows = this.recentOpeningsTable.locator('tbody tr');
  }

  async toggleMapButton() {
    await this.mapButton.click();
  }

  async isMapVisible() {
    return await this.map.isVisible();
  }

  async zoomInMap() {
    await this.mapZoomInButton.click();
  }

  async zoomOutMap() {
    await this.mapZoomOutButton.click();
  }

  async expandMap() {
    await this.mapExpandButton.click();
  }

  async isRecentOpeningsTableVisible() {
    return await this.recentOpeningsTable.isVisible();
  }

  async isOpeningFavourited(openingId: string) {
    const favButton = await this.recentOpeningsTableRows.getByTestId(`action-fav-${openingId}`);
    const ariaPressed = await favButton.getAttribute('aria-pressed');
    return ariaPressed === 'true';
  }

  async favouriteOpening(openingId: string) {
    if (await this.isOpeningFavourited(openingId)) {
      throw new Error(`Opening ${openingId} is already favourited.`);
    }
    const favButton = await this.recentOpeningsTableRows.getByTestId(`action-fav-${openingId}`);
    await favButton.click();
  }

  async unfavouriteOpening(openingId: string) {
    if (!(await this.isOpeningFavourited(openingId))) {
      throw new Error(`Opening ${openingId} is not favourited.`);
    }
    const favButton = await this.recentOpeningsTableRows.getByTestId(`action-fav-${openingId}`);
    await favButton.click();
  }

  async isFavouriteNotificationVisible(openingId: string) {
    try {
      const notification = this.page.getByRole('status', { name: `Opening Id ${openingId} favourited` });
      return true;
    } catch {
      return false;
    }
  }

  async isUnfavouriteNotificationVisible(openingId: string) {
    try {
      const notification = this.page.getByRole('status', { name: `Opening Id ${openingId} unfavourited` });
      return true;
    } catch {
      return false;
    }
  }

  async openOpeningFromTable(openingId: string) {
    const cell = await this.recentOpeningsTableRows.getByTestId(`opening-table-cell-openingId-${openingId}`);
    const link = await cell.locator('a');
    await link.click();
    await this.page.waitForURL('**/' + routes.openingDetails(openingId));
  }

  async openOpeningInNewTabFromTable(openingId: string) {
    const btn = await this.recentOpeningsTableRows.getByRole('button', { name: 'Open 1004185 in a new tab' });
    const [newPage] = await Promise.all([
      this.page.context().waitForEvent('page'),
      btn.click(),
    ]);
    await newPage.waitForURL('**/' + routes.openingDetails(openingId));
  }

  async checkOpeningInTable(openingId: string) {
    if (!await this.isMapVisible()) {
      throw new Error("Map is not visible. Cannot check opening.");
    }

    const openingCheckbox = await this.recentOpeningsTableRows.getByTestId('opening-table-cell-actions-60000').locator('label');
    await openingCheckbox.click();
  }

  async isOpeningVisibleOnMap() {
    const overlayPane = this.page.locator('.leaflet-overlay-pane');
    await this.page.waitForTimeout(1000);
    const gTag = overlayPane.locator('svg > g');

    await overlayPane.waitFor({ state: 'attached' });

    const childCount = await gTag.evaluate((g) => g.children.length);
    return childCount > 0;
  }
}
