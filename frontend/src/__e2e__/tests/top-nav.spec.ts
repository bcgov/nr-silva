import test, { expect } from "@playwright/test";
import { TopNavBar } from "../pages/top-nav-bar";

test.describe('Top Navigation Bar', () => {
  test('elements shoudl be visible', async ({ page }) => {
    const topNavBar = new TopNavBar(page);
    await topNavBar.goto();
    await expect(topNavBar.title).toBeVisible();
    await expect(await topNavBar.getTitle()).toBe('Silva');
    await expect(topNavBar.themeToggle).toBeVisible();
    await expect(topNavBar.profileButton).toBeVisible();
  });

  test('profile button should open and close the profile menu', async ({ page }) => {
    const topNavBar = new TopNavBar(page);
    await topNavBar.goto();

    await expect(topNavBar.profileButton).toBeVisible();

    await topNavBar.openProfileMenu();
    await expect(await topNavBar.isProfileMenuOpen()).toBe(true);

    await topNavBar.closeProfileMenu();
    await expect(await topNavBar.isProfileMenuOpen()).toBe(false);
  });

  test('profile button should open profile menu and check user information', async ({ page }) => {
    const topNavBar = new TopNavBar(page);
    await topNavBar.goto();
    await topNavBar.openProfileMenu();

    const userInfo = await topNavBar.getUserInfo();
    await expect(userInfo).toBeDefined();
    await expect(userInfo.name).toBeDefined();
    await expect(userInfo.username).toBeDefined();
    await expect(userInfo.email).toBeDefined();
    await expect(userInfo.username).toBe(process.env.TEST_BCEID_USERNAME);
  });


});
