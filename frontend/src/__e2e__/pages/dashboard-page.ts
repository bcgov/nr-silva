import { Locator, Page } from "@playwright/test";
import { routes } from "../utils/routes";
import { recentOpeningsHeaders } from "@/components/RecentOpenings/constants";

export class DashboardPage {
  private readonly page: Page;
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
  private readonly favouritesSection: Locator;

  private baseUrl: string;

  constructor(page: Page) {
    this.baseUrl = (process.env.BASE_URL ?? 'http://localhost:3000') + routes.dashboard;
    this.page = page;
    this.silvicultureSearchButton = page.getByRole('button', { name: 'Silviculture search' });
    this.mapButton = page.getByRole('button', { name: 'Hide map' })
    this.map = page.locator('.map-container');
    this.recentOpeningsSection = page.locator('.recent-openings-container');
    this.recentOpeningsTable = page.getByRole('table', { name: 'Recent openings table' });
    this.recentOpeningsTableRows = this.recentOpeningsTable.locator('tbody tr');
    this.openingSubmissionsTrendSection = page.locator('.submission-trend-container');
    this.districtDropDown = this.openingSubmissionsTrendSection.getByTestId('district-dropdown');
    this.statusDropDown = this.openingSubmissionsTrendSection.getByTestId('status-dropdown');
    this.submissionYearDropDown = this.openingSubmissionsTrendSection.getByTestId('trend-year-selection');
    this.favouritesSection = page.locator('.favourite-openings-container');
  }

  async goto() {
    await this.page.goto(this.baseUrl);
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
      await this.page.waitForSelector('.map-container', { state: 'visible' });
    }
  }

  async hideMap() {
    if (await this.map.isVisible()) {
      await this.mapButton.click();
      await this.page.waitForSelector('.map-container', { state: 'hidden' });
    }
  }

  async getRecentOpeningsCount() {
    return await this.recentOpeningsTableRows.count();
  }

  async getOpeningRowDataByOpeningId(openingId: string) {
    const row = await this.recentOpeningsTableRows.getByTestId(`opening-table-row-${openingId}`);
    const values: string[] = [];
    for (const header of recentOpeningsHeaders) {
      const cellText = await row.getByTestId(`opening-table-cell-${header.key}-${openingId}`).textContent();
      values.push(cellText ?? "");
    }

    return values;
  }

  async isOpeningFavourited(openingId: string) {
    const favButton = await this.recentOpeningsTableRows.getByTestId(`action-fav-${openingId}`);
    const favIcon = await favButton.locator('svg');
    const classAttr = await favIcon.getAttribute('class');

    return classAttr?.includes('favourited-icon') ?? false;
  }

  async favouriteOpening(openingId: string) {
    if (await this.isOpeningFavourited(openingId)) {
      console.warn(`Opening ${openingId} is already favourited.`);
      return;
    }
    const favButton = await this.recentOpeningsTableRows.getByTestId(`action-fav-${openingId}`);
    await favButton.click();
    const favouriteNotification = this.page.getByRole('status', { name: `Opening Id ${openingId} favourited` })
    await favouriteNotification.waitFor({ state: 'visible' });
  }

  async unfavouriteOpening(openingId: string) {
    if (!(await this.isOpeningFavourited(openingId))) {
      console.warn(`Opening ${openingId} is not favourited.`);
      return;
    }
    const favButton = await this.recentOpeningsTableRows.getByTestId(`action-fav-${openingId}`);
    await favButton.click();
    const unfavouriteNotification = this.page.getByRole('status', { name: `Opening Id ${openingId} unfavourited` })
    await unfavouriteNotification.waitFor({ state: 'visible' });
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

  async isOpeningFavouritedOnFavouritesection(openingId: string) {
    try {
      const favTile = this.favouritesSection.getByTestId(`favourite-opening-tile-${openingId}`);
      return await favTile.isVisible();
    } catch {
      return false;
    }
  }

  async unfavouriteOpeningOnFavouritesSection(openingId: string) {
    if (!(await this.isOpeningFavouritedOnFavouritesection(openingId))) {
      console.warn(`Opening ${openingId} is not favourited in favourites section.`);
      return;
    }

    const favTile = this.favouritesSection.getByTestId(`favourite-opening-tile-${openingId}`);
    const favButton = favTile.getByTestId(`action-fav-${openingId}`);
    await favButton.click();
    const unfavouriteNotification = this.page.getByRole('status', { name: `Opening Id ${openingId} unfavourited` })
    await unfavouriteNotification.waitFor({ state: 'visible' });
  }
} 
