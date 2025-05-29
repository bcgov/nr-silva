# E2E with Playwright

Playwright is a Node.js library that enables reliable end-to-end testing for modern web apps.
Visit Playwright's page: https://playwright.dev/

The tests run in parallel by default and are currently configured to use one Test BCeID account. This means that if multiple tests perform CRUD operations on the same data, there could or will be data conflicts. However, Playwright can be set up to use multiple testing accounts. This is not currently implemented, as our app is read-only at the time of writing.

Our E2E tests are configured to run only on screens where a user is logged in, so pages like the Landing page are not tested. The landing page indirectly tested in `/auth/auth.setup.ts` when creating a session.

#### Requirements
- Process environment variables:
  1. `TEST_BCEID_USERNAME`
  2. `TEST_BCEID_PASSWORD`

#### Structure
- `/auth` contains 3 files:
  1. `auth.setup.ts` signs in to the app and maintains the user session for all tests. The session is stored in `user.json`.
  2. `auth.teardown.ts` clears the user session by resetting `user.json`. This is triggered at the end of all test runs or when tests are manually terminated.
  3. `user.{browser_name}.json` stores the user session during E2E runs. Ensure that `user.*.json` are empty (`{}`) when pushing to the repo. Playwright should clear this automatically, but if it fails, clear it manually.

- `/setup` contains 1 file:
  1. `coverage.ts` collects one coverage file per test. These are merged by `nyc` in a separate script. All tests must `import { test } from './setup/coverage'` instead of importing from `@playwright/test` to enable coverage generation.


## Usage
To run local end-to-end tests, ensure Chromium is installed, it’s the default browser used for local testing. You can optionally install additional browsers (Firefox, WebKit) if you’d like to test across multiple environments locally.
```
npx playwright install chromium
```
**Note**: On GitHub Actions, all three browsers (Chromium, Firefox, and WebKit) are installed and tested automatically as part of the CI workflow.

#### Writing tests
Import `test` from our modified wrapper in the coverage file. Import `expect` and other tools from Playwright directly.

Example:
```typescript
import { expect } from '@playwright/test';
import { test } from './setup/coverage';
```

Use `npm run e2e:local`, `e2e:local:ui`, or `e2e:build` to test locally.

#### Scripts

- **precoverage**
  Removes the `.nyc_output` directory to ensure a clean slate for coverage data collection.

- **e2e:local**
  Runs local end-to-end tests with coverage:
  - Clears previous coverage
  - Sets `BASE_URL` to `http://localhost:3000`
  - Runs Playwright tests
  - Generates coverage report

- **e2e:local:ui**
  Same as `e2e:local` but opens the Playwright UI for test execution and debugging.

- **e2e:build**
  Runs end-to-end tests against a built frontend app:
  - Clears previous coverage
  - Builds the app
  - Injects runtime config with `build:env`
  - Serves app using `vite preview` on port 4173
  - Runs Playwright tests against `http://localhost:4173`

- **e2e:ci:dev**
  CI version of `e2e:local`:
  - Clears previous coverage
  - Starts Vite dev server
  - Waits for `http://localhost:3000` to be ready
  - Runs Playwright tests
  - Generates coverage report

- **e2e:ci:build**
  CI version of `e2e:build` that assumes the app is already built and only runs Playwright tests.

- **report:coverage**
  Merges all `.nyc_output` coverage data files and generates a combined coverage report.

## Coverage

The coverage is generated with `v8-to-istanbul`, which instruments the webpage by transforming its JavaScript files to collect execution data.

The benefit is wide coverage with minimal testing effort: as long as a component is rendered, it is considered covered.

The downside is that it is up to the team to write meaningful tests. Without thoughtful test design, coverage alone does not reflect E2E test quality.

## CI

Once a Pull Request is opened, GitHub Actions will first build and deploy the apps, then run the following in parallel:

1. Run `e2e:ci:dev` to start a Vite dev server, execute tests, and generate coverage for SonarCloud.
2. Run `e2e:ci:build` to execute tests against the deployed frontend and backend.
