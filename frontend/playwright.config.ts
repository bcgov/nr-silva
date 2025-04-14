import { defineConfig, chromium } from '@playwright/test';
import { THIRTY_SECONDS } from './src/constants/TimeUnits';

export default defineConfig({
  testDir: './src/__e2e__',
  timeout: THIRTY_SECONDS,
  retries: 0,
  use: {
    headless: true,
    baseURL: 'http://localhost:4173',
    viewport: { width: 1920, height: 1080 },
    ignoreHTTPSErrors: true,
    video: 'retain-on-failure',
    launchOptions: {
      executablePath: chromium.executablePath(),
    },
  },
});
