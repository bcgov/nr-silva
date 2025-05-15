import { expect } from '@playwright/test';
import test from './config/AuthTest';

test('Dashboard', async ({ page }) => {
  await page.goto('/');
  await expect(page).toHaveTitle(/Silva/);
});
