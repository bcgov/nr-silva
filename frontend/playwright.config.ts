import { defineConfig, chromium } from '@playwright/test';
import { THIRTY_SECONDS } from './src/constants/TimeUnits';

const baseURL = process.env.BASE_URL ?? 'http://localhost:3000';

export default defineConfig({
  testDir: './src/__e2e__',
  timeout: THIRTY_SECONDS,
  retries: 0,
  globalSetup: './src/__e2e__/config/global.setup.ts',
  use: {
    headless: true,
    baseURL,
    viewport: { width: 1920, height: 1080 },
    ignoreHTTPSErrors: true,
    video: 'retain-on-failure',
    launchOptions: {
      executablePath: chromium.executablePath(),
    },
  },
});
