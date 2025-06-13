import { defineConfig, devices, type VideoMode } from '@playwright/test';
import path from 'path';
import * as dotenv from 'dotenv';

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
    storageState: './src/auth/user.chromium.json',
  },
  {
    name: 'firefox',
    device: devices['Desktop Firefox'],
    storageState: './src/auth/user.firefox.json',
  },
  {
    name: 'webkit',
    device: devices['Desktop Safari'],
    storageState: './src/auth/user.webkit.json',
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
  timeout: 30000,  // 30 seconds
  testDir: './src',  
  forbidOnly: !!process.env.CI,  
  retries: 0,  
  workers: process.env.CI ? 1 : undefined,
  reporter: [
    ['list'],
    ['junit', { outputFile: 'results.xml' }]
  ],
  globalSetup: './src/setup/global.setup.ts',
  globalTeardown: './src/setup/global.teardown.ts',
  projects,
});
