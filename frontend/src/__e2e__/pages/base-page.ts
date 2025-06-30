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
    return await this.page.getByRole('heading', { name: 'Silviculture Search' }).textContent();
  }

  async isHeadingVisible() {
    return await this.page.getByRole('heading', { name: 'Silviculture Search' }).isVisible();
  }
}
