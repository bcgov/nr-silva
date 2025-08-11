import { defineConfig, devices, type VideoMode } from '@playwright/test';
import * as dotenv from 'dotenv';
import { THIRTY_SECONDS } from './src/constants/TimeUnits';

dotenv.config();

const baseURL = process.env.BASE_URL ?? 'http://localhost:3000';
const isAllBrowsers = process.env.ALL_BROWSERS === 'true';

const commonSettings = {
  headless: true,
  baseURL,
  viewport: { width: 1920, height: 1080 },
  ignoreHTTPSErrors: true,
  video: { mode: 'retain-on-failure' as VideoMode },
  contextOptions: {
    recordVideo: {
      dir: './test-results/videos',
    },
  },
};

const browserProjects = [
  {
    name: 'chromium',
    device: devices['Desktop Chrome'],
    storageState: './src/__e2e__/auth/user.chromium.json',
  },
  {
    name: 'firefox',
    device: devices['Desktop Firefox'],
    storageState: './src/__e2e__/auth/user.firefox.json',
  },
  {
    name: 'webkit',
    device: devices['Desktop Safari'],
    storageState: './src/__e2e__/auth/user.webkit.json',
  },
];

// Filter based on ALL_BROWSERS env
const projects = (isAllBrowsers ? browserProjects : [browserProjects[0]!]).map(({ name, device, storageState }) => ({
  name,
  use: {
    ...commonSettings,
    ...device,
    storageState,
  },
}));

export default defineConfig({
  timeout: THIRTY_SECONDS,
  retries: process.env.CI ? 2 : 0,
  testDir: './src/__e2e__',
  globalSetup: './src/__e2e__/auth/auth.setup.ts',
  globalTeardown: './src/__e2e__/auth/auth.teardown.ts',
  projects,
  webServer: {
    command: 'npm start',
    url: baseURL,
    reuseExistingServer: true,
  }
});
