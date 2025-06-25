import { Locator, Page } from "@playwright/test";

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
    await this.themeToggle.click();
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
    return classAttr?.includes('bx--header-panel--expanded') ?? false;
  }

  // async clickProfileButton() {
  //   const wasOpen = await this.isProfileMenuOpen();
  //   await this.profileButton.click();
  //   const menuHandle = await this.profileMenu.elementHandle();
  //   if (!menuHandle) throw new Error('Profile menu element not found');
  //   if (wasOpen) {
  //     // Wait for the menu to close
  //     await this.page.waitForFunction(
  //       (el) => !el.classList.contains('bx--header-panel--expanded'),
  //       menuHandle
  //     );
  //   } else {
  //     // Wait for the menu to open
  //     await this.page.waitForFunction(
  //       (el) => el.classList.contains('bx--header-panel--expanded'),
  //       menuHandle
  //     );
  //   }
  // }
  async openProfileMenu() {
    if (!(await this.isProfileMenuOpen())) {
      await this.profileButton.click();
      const menuHandle = await this.profileMenu.elementHandle();
      if (!menuHandle) throw new Error('Profile menu element not found');
      await this.page.waitForFunction(
        (el) => el.classList.contains('bx--header-panel--expanded'),
        menuHandle
      );
    }
  }

  async closeProfileMenu() {
    if (await this.isProfileMenuOpen()) {
      await this.profileButton.click();
      const menuHandle = await this.profileMenu.elementHandle();
      if (!menuHandle) throw new Error('Profile menu element not found');
      await this.page.waitForFunction(
        (el) => !el.classList.contains('bx--header-panel--expanded'),
        menuHandle
      );
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
    this.changeThemeButton.click();
  }

  async logout() {
    this.logoutButton.click();
  }
}
