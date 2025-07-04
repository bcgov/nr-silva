import { test, expect } from "@playwright/test";
import { SideBar } from "../pages/side-bar";

test.describe('Side Bar', () => {
  test('elements should be visible', async ({ page }) => {
    const sideBar = new SideBar(page);
    await sideBar.goto();

    await expect(sideBar.sideBarMenu).toBeVisible();
    await expect(sideBar.mainActivitiesTitle).toBeVisible();
    await expect(sideBar.dashboardButton).toBeVisible();
    await expect(sideBar.silvicultureSearchButton).toBeVisible();
    await expect(sideBar.openingsButton).toBeVisible();

    await expect(sideBar.mainActivitiesTitle).toHaveText('Main activities');
    await expect(sideBar.dashboardButton).toHaveText('Dashboard');
    await expect(sideBar.silvicultureSearchButton).toHaveText('Silviculture search');
    await expect(sideBar.openingsButton).toHaveText('Openings');
  });

  test('should navigate to dashboard', async ({ page }) => {
    const sideBar = new SideBar(page);
    await sideBar.goto();
    await sideBar.goToDashboard();

    await expect(page).toHaveURL(/.*\/dashboard/);
    await expect(page.getByRole('heading', { name: 'Dashboard' })).toBeVisible();
  });

  test('should navigate to silviculture search', async ({ page }) => {
    const sideBar = new SideBar(page);
    await sideBar.goto();
    await sideBar.goToSilvicultureSearch();

    await expect(page).toHaveURL(/.*\/silviculture-search/);
    await expect(page.getByRole('heading', { name: 'Silviculture search' })).toBeVisible();
  });

  test('should navigate to openings', async ({ page }) => {
    const sideBar = new SideBar(page);
    await sideBar.goto();
    await sideBar.goToOpenings();

    await expect(page).toHaveURL(/.*\/openings/);
    await expect(page.getByRole('heading', { name: 'Openings', exact: true })).toBeVisible();
  });

});
