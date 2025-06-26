import test, { expect } from "@playwright/test";
import { LoginPage } from "../pages/login-page";

test.describe('Login', () => {

  test.skip();
  test('should login with IDIR and redirect to dashboard', async ({ page }) => {
    const loginPage = new LoginPage(page);
    await loginPage.goto();
    await loginPage.loginWithIDIR(process.env.IDIR_USERNAME!, process.env.IDIR_PASSWORD!);
    await expect(page).toHaveURL(/.*\/dashboard/);
    await expect(page.getByRole('heading', { name: 'Dashboard' })).toBeVisible();
  });

  test.skip();
  test('should login with BCEID and redirect to dashboard', async ({ page }) => {
    const loginPage = new LoginPage(page);
    await loginPage.goto();
    await loginPage.loginWithBCEID(process.env.TEST_BCEID_USERNAME!, process.env.TEST_BCEID_USERNAME!);
    await expect(page).toHaveURL(/.*\/dashboard/);
    await expect(page.getByRole('heading', { name: 'Dashboard' })).toBeVisible();
  });
});
