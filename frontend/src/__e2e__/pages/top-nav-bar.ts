import { Locator, Page } from "@playwright/test";
import { CARBON_CLASS_PREFIX } from "../../constants";

export class TopNavBar {
  readonly page: Page;
  readonly baseUrl: string;
  readonly title: Locator;
  readonly themeToggle: Locator;
  readonly profileButton: Locator;
  readonly profileMenu: Locator;
  readonly changeThemeButton: Locator;
  readonly logoutButton: Locator;

  constructor(page: Page) {
    this.page = page;
    this.baseUrl = process.env.BASE_URL ?? 'http://localhost:3000';
    this.title = page.getByTestId('header-name');
    this.themeToggle = page.getByTestId('header').getByRole('button').first();
    this.profileButton = page.getByTestId('header-button__user');
    this.profileMenu = page.getByLabel('User Profile Tab');
    this.changeThemeButton = page.locator('a').filter({ hasText: 'Change theme' });
    this.logoutButton = this.page.locator('a').filter({ hasText: 'Log out' });
  }

  async goto() {
    await this.page.goto(this.baseUrl);
  }

  async getTitle() {
    return await this.title.textContent();
  }

  async toggleThemeSwitch() {
    const initialTheme = await this.getCurrentTheme();
    await this.themeToggle.click();
    // Wait for the theme to actually change
    await this.page.waitForFunction(() => {
      const el = document.querySelector('[data-testid="header"] button');
      if (!el) return false;
      const classAttr = el.getAttribute('class');
      if (initialTheme === 'dark') {
        return classAttr?.includes('off');
      } else {
        return classAttr?.includes('on');
      }
    });
  }

  async getCurrentTheme() {
    const classAtrr = await this.themeToggle.getAttribute('class');
    if (classAtrr?.includes('on')) {
      return 'dark';
    } else if (classAtrr?.includes('off')) {
      return 'light';
    }
    return 'unknown';
  }

  async isProfileMenuOpen() {
    const classAttr = await this.profileMenu.getAttribute('class');
    return classAttr?.includes(`${CARBON_CLASS_PREFIX}--header-panel--expanded`) ?? false;
  }

  async openProfileMenu() {
    if (!(await this.isProfileMenuOpen())) {
      await this.profileButton.click();
      const prefix = CARBON_CLASS_PREFIX;
      await this.page.waitForFunction(() => {
        const el = document.querySelector('[aria-label="User Profile Tab"]');
        return el ? el.classList.contains(`${prefix}--header-panel--expanded`) : false;
      });
    }
  }

  async closeProfileMenu() {
    if (await this.isProfileMenuOpen()) {
      await this.profileButton.click();
      const prefix = CARBON_CLASS_PREFIX;
      await this.page.waitForFunction(() => {
        const el = document.querySelector('[aria-label="User Profile Tab"]');
        return el ? !el.classList.contains(`${prefix}--header-panel--expanded`) : true;
      });
    }
  }

  async getUserInfo() {
    const userData = this.profileMenu.locator('.user-data');
    const name = await userData.locator('.user-name').textContent();
    const username = await userData.locator('p').nth(1).textContent();
    const email = await userData.locator('p').nth(2).textContent();
    return {
      name: name?.trim() ?? '',
      username: username?.replace(/^.*?:\s*/, '').trim() ?? '',
      email: email?.replace(/^Email:\s*/, '').trim() ?? '',
    };
  }

  async changeThemeFromMenu() {
    await this.changeThemeButton.click();
  }

  async logout() {
    await this.logoutButton.click();
  }
}
