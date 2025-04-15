import { expect } from '@playwright/test';
import test from './config/AuthTest';

test.use({ authRequired: false })

test('Landing loads', async ({ page }) => {
  await page.goto('/');
  await expect(page).toHaveTitle(/Silva/);
});
