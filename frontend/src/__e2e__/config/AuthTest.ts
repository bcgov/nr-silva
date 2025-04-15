import { test as base } from '@playwright/test';

type TestOptions = {
  authRequired: boolean;
};

/**
 * Custom Playwright test fixture that:
 * - Requires authentication by default using a shared storage state
 * - Allows opting out of auth per test with `test.use({ authRequired: false })`
 * - Creates isolated browser contexts per test, with or without session
 */
const test = base.extend<TestOptions>({
  /**
   * Whether the test requires authentication.
   * Defaults to true. Use `test.use({ authRequired: false })` to opt out.
   */
  authRequired: [true, { option: true }],

  /**
   * Creates a browser context with or without the stored session state depending on `authRequired`.
   * - Loads session from 'storageState.json' if auth is required
   * - Uses a blank context if auth is not required
   */
  context: async ({ browser, authRequired }, use) => {
    const context = authRequired
      ? await browser.newContext({ storageState: 'storageState.json' })
      : await browser.newContext();

    await use(context);
    await context.close();
  },

  /**
   * Creates a new page for each test using the prepared context.
   */
  page: async ({ context }, use) => {
    const page = await context.newPage();
    await use(page);
  },
});

export default test;
