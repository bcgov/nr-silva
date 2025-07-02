import { Page } from "@playwright/test";

export class BasePage {
  protected readonly page: Page;
  protected readonly baseUrl: string;

  constructor(page: Page, route: string = '') {
    this.page = page;
    this.baseUrl = (process.env.BASE_URL ?? 'http://localhost:3000') + route;
  }

  async goto() {
    await this.page.goto(this.baseUrl);
  }

  async getTitle() {
    return await this.page.title();
  }

  async getHeading() {
    const titleContainer = await this.page.locator('.title-container h1');
    const titleText = await titleContainer.textContent();
    return titleText ? titleText.trim() : '';
  }

  async isHeadingVisible() {
    const titleContainer = await this.page.locator('.title-container h1');
    return await titleContainer.isVisible();
  }
}
