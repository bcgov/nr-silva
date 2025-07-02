import { Locator, Page } from "@playwright/test";

export class SideBar {
  readonly page: Page;
  readonly baseUrl: string;
  readonly sideBarMenu: Locator;
  readonly mainActivitiesTitle: Locator;
  readonly dashboardButton: Locator;
  readonly silvicultureSearchButton: Locator;
  readonly openingsButton: Locator;

  constructor(page: Page) {
    this.page = page;
    this.baseUrl = process.env.BASE_URL ?? 'http://localhost:3000';
    this.sideBarMenu = page.getByRole('navigation', { name: 'Side menu' }).getByRole('list');
    this.mainActivitiesTitle = this.sideBarMenu.getByText('Main activities');
    this.dashboardButton = this.sideBarMenu.getByTestId('side-nav-link-dashboard');
    this.silvicultureSearchButton = this.sideBarMenu.getByTestId('side-nav-link-silviculture-search');
    this.openingsButton = this.sideBarMenu.getByTestId('side-nav-link-openings');
  }

  async goto() {
    await this.page.goto(this.baseUrl);
  }

  async goToDashboard() {
    await this.dashboardButton.click();
    await this.page.waitForURL('**/dashboard');
  }

  async goToSilvicultureSearch() {
    await this.silvicultureSearchButton.click();
    await this.page.waitForURL('**/silviculture-search');
  }

  async goToOpenings() {
    await this.openingsButton.click();
    await this.page.waitForURL('**/openings');
  }
}
