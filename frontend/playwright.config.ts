import { defineConfig, chromium } from '@playwright/test';
import { TWO_MINUTE } from './src/constants/TimeUnits';

const baseURL = process.env.BASE_URL ?? 'http://localhost:3000';

export default defineConfig({
  testDir: './src/__e2e__',
  timeout: TWO_MINUTE,
  retries: 0,
  globalSetup: './src/__e2e__/auth/auth.setup.ts',
  globalTeardown: './src/__e2e__/auth/auth.teardown.ts',
  use: {
    actionTimeout: TWO_MINUTE,
    headless: true,
    baseURL,
    viewport: { width: 1920, height: 1080 },
    ignoreHTTPSErrors: true,
    video: 'retain-on-failure',
    launchOptions: {
      executablePath: chromium.executablePath(),
    },
    storageState: './src/__e2e__/auth/user.json',
  },
});
