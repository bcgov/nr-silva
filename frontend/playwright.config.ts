import { defineConfig, devices, type VideoMode } from '@playwright/test';
import { THIRTY_SECONDS } from './src/constants/TimeUnits';

const baseURL = process.env.BASE_URL ?? 'http://localhost:3000';

// Check if CLI args include "--all-browsers"
const isAllBrowsers = process.argv.includes('--all-browsers');

const commonSettings = {
  headless: true,
  baseURL,
  viewport: { width: 1920, height: 1080 },
  ignoreHTTPSErrors: true,
  video: { mode: 'retain-on-failure' as VideoMode },
  storageState: './src/__e2e__/auth/user.json',
  contextOptions: {
    recordVideo: {
      dir: './test-results/videos',
    },
  },
};

const projects = isAllBrowsers
  ? [
    {
      name: 'chromium',
      use: { ...commonSettings, ...devices['Desktop Chrome'] },
    },
    {
      name: 'firefox',
      use: { ...commonSettings, ...devices['Desktop Firefox'] },
    },
    {
      name: 'webkit',
      use: { ...commonSettings, ...devices['Desktop Safari'] },
    },
  ]
  : [
    {
      name: 'chromium',
      use: { ...commonSettings, ...devices['Desktop Chrome'] },
    },
  ];

export default defineConfig({
  timeout: THIRTY_SECONDS,
  retries: 1,
  testDir: './src/__e2e__',
  globalSetup: './src/__e2e__/auth/auth.setup.ts',
  globalTeardown: './src/__e2e__/auth/auth.teardown.ts',
  projects,
});
