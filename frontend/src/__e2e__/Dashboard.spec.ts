import { expect } from '@playwright/test';
import test from './config/AuthTest';


test('Dashboard', async ({ page }) => {
  await page.goto('http://localhost:4173');
  await expect(page).toHaveTitle(/Silva/);
});
