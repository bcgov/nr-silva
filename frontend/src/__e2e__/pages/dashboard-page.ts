import { Locator, Page } from "@playwright/test";
import { routes } from "../utils/routes";
import { recentOpeningsHeaders } from "@/components/RecentOpenings/constants";
import { BasePage } from "./base-page";

export class DashboardPage extends BasePage {
  private readonly silvicultureSearchButton: Locator;
  private readonly mapButton: Locator;
  private readonly map: Locator;
  private readonly recentOpeningsSection: Locator;
  private readonly recentOpeningsTable: Locator;
  private readonly recentOpeningsTableRows: Locator;
  private readonly openingSubmissionsTrendSection: Locator;
  private readonly districtDropDownBtn: Locator;
  private readonly statusDropDownBtn: Locator;
  private readonly submissionYearDropDownBtn: Locator;
  private readonly submissionYearDropDownMenu: Locator;
  private readonly favouritesSection: Locator;

  constructor(page: Page) {
    super(page, routes.dashboard());

    this.silvicultureSearchButton = page.getByRole('button', { name: 'Silviculture search' });
    this.recentOpeningsSection = page.locator('.recent-openings-container');
    this.mapButton = this.recentOpeningsSection.getByTestId('toggle-map-button');
    this.map = this.recentOpeningsSection.locator('.leaflet-container');
    this.recentOpeningsTable = this.recentOpeningsSection.getByRole('table', { name: 'Recent openings table' });
    this.recentOpeningsTableRows = this.recentOpeningsTable.locator('tbody tr');
    this.openingSubmissionsTrendSection = page.locator('.submission-trend-container');
    this.districtDropDownBtn = this.openingSubmissionsTrendSection.locator('#district-dropdown-toggle-button');
    this.statusDropDownBtn = this.openingSubmissionsTrendSection.locator('#status-dropdown-toggle-button');
    this.submissionYearDropDownBtn = this.openingSubmissionsTrendSection.locator('[id="downshift-«ri0»-toggle-button"]');
    this.submissionYearDropDownMenu = this.openingSubmissionsTrendSection.getByTestId('trend-year-selection');
    this.favouritesSection = page.locator('.favourite-openings-container');
  }

  async isRecentOpeningsSectionVisible() {
    return await this.recentOpeningsSection.isVisible();
  }

  async isOpeningSubmissionsTrendSectionVisible() {
    return await this.openingSubmissionsTrendSection.isVisible();
  }

  async isFavouritesSectionVisible() {
    return await this.favouritesSection.isVisible();
  }

  async clickSilvicultureSearchButton() {
    await this.silvicultureSearchButton.click();
    await this.page.waitForURL('**/' + routes.silvicultureSearch());
  }

  async isMapVisible() {
    return await this.map.isVisible();
  }

  async showMap() {
    if (!(await this.map.isVisible())) {
      await this.mapButton.click();
      await this.page.waitForSelector('.leaflet-container', { state: 'visible' });
    }
  }

  async hideMap() {
    if (await this.map.isVisible()) {
      await this.mapButton.click();
      await this.page.waitForSelector('.leaflet-container', { state: 'hidden' });
    }
  }

  async getRecentOpeningsCount() {
    await this.recentOpeningsTable.waitFor({ state: 'visible' });
    return await this.recentOpeningsTableRows.count();
  }

  async getOpeningRowDataByOpeningId(openingId: string) {
    await this.recentOpeningsTable.waitFor({ state: 'visible' });
    const row = await this.recentOpeningsTable.getByTestId(`opening-table-row-${openingId}`);
    const values: string[] = [];
    for (const header of recentOpeningsHeaders) {
      if (header.key === 'actions') {
        continue;
      }
      const cell = await row.getByTestId(`opening-table-cell-${header.key}-${openingId}`);
      const cellText = await cell.textContent();
      values.push(cellText ?? "");
    }

    return values;
  }

  async isOpeningFavourited(openingId: string) {
    const favButton = await this.recentOpeningsTableRows.getByTestId(`action-fav-${openingId}`);
    const ariaPressed = await favButton.getAttribute('aria-pressed');
    return ariaPressed === 'true';
  }

  async favouriteOpening(openingId: string) {
    if (await this.isOpeningFavourited(openingId)) {
      console.warn(`Opening ${openingId} is already favourited.`);
      return;
    }
    const favButton = await this.recentOpeningsTableRows.getByTestId(`action-fav-${openingId}`);
    await favButton.click();
  }

  async unfavouriteOpening(openingId: string) {
    if (!(await this.isOpeningFavourited(openingId))) {
      console.warn(`Opening ${openingId} is not favourited.`);
      return;
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

  async openOpeningFromRecentOpenings(openingId: string) {
    const cell = await this.recentOpeningsTableRows.getByTestId(`opening-table-cell-openingId-${openingId}`);
    const link = await cell.locator('a');
    await link.click();
    await this.page.waitForURL('**/' + routes.openingDetails(openingId));
  }

  async openOpeningInNewTab(openingId: string) {
    const btn = await this.recentOpeningsTableRows.getByRole('button', { name: 'Open 1004185 in a new tab' });
    const [newPage] = await Promise.all([
      this.page.context().waitForEvent('page'),
      btn.click(),
    ]);
    await newPage.waitForURL('**/' + routes.openingDetails(openingId));
  }

  async checkOpening(openingId: string) {
    if (!await this.isMapVisible()) {
      console.warn("Map is not visible, cannot select opening.");
      return;
    }

    const openingCheckbox = await this.recentOpeningsTableRows.getByTestId(`checkbox-${openingId}`);
    await openingCheckbox.check();
  }

  async isOpeningFavouritedOnFavouriteSection(openingId: string) {
    try {
      const favTile = this.favouritesSection.getByTestId(`favourite-opening-tile-${openingId}`);
      return true;
    } catch {
      return false;
    }
  }

  async unfavouriteOpeningOnFavouritesSection(openingId: string) {
    if (!(await this.isOpeningFavouritedOnFavouriteSection(openingId))) {
      console.warn(`Opening ${openingId} is not favourited in favourites section.`);
      return;
    }

    const favTile = this.favouritesSection.getByTestId(`favourite-opening-tile-${openingId}`);
    const favButton = favTile.getByTestId(`action-fav-${openingId}`);
    await favButton.click();
    const unfavouriteNotification = this.page.getByRole('status', { name: `Opening Id ${openingId} unfavourited` })
    await unfavouriteNotification.waitFor({ state: 'visible' });
  }

  async chooseDistrict(district: string) {
    await this.districtDropDownBtn.click();
    const menu = this.openingSubmissionsTrendSection.locator('#district-dropdown__menu');
    await menu.getByRole('option', { name: district }).click();
  }

  async chooseStatus(status: string) {
    await this.statusDropDownBtn.click();
    const menu = this.openingSubmissionsTrendSection.locator('#status-dropdown__menu');
    await menu.getByRole('option', { name: status }).click();
  }

  async chooseSubmissionYear(year: string) {
    const submissionYearContainer = this.page.locator('.trend-year-selection-combobox');
    await this.submissionYearDropDownMenu.click();
    try {
      await submissionYearContainer.getByRole('option', { name: year }).click();
    } catch (error) {
      console.error(`Failed to select submission year ${year}:`, error);
    }
  }
}
