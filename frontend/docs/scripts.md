# Scripts Documentation

This document describes each script in the `scripts` block of the `package.json` file, including its purpose, when to use it and some explanations regarding pre and post scripts.

---

## Development & Build

### dev

Runs `npm start` [see start for more info](#start). It trigger a pre-script that execute the [OpenAPI type generation](#generateopenapi) before starting the server, updating the type definitions.

#### Pre script {#predev}

The `predev` script runs before `dev`, ensuring that the OpenAPI types are generated before starting the development server.

### start  

Runs `vite --host` and serves the application on port `3000` by default.

### build

Runs `vite build` and generates a production-ready static asset bundle that is later deployed inside an HTTP server.

#### Pre script {#prebuild}

Runs `tsc` (TypeScript compiler) that type-checks the codebase before building to catch type errors early.

### build:env

Writes environment variables to `build/env.js`.  This script is used to inject environment configuration into the build output for runtime use.

### preview

Runs `vite preview --port 4173` that serves the built app locally for preview/testing.

### generate:openapi {#generateopenapi}  

Runs the OpenAPI code generation, that generate TypeScript types from the backend OpenAPI spec. It keeps API types in sync with the backend. The actual command calls the `openapi-typescript` tool to fetch the OpenAPI spec from the backend and generate types in `src/types/OpenApiTypes.d.ts`.

### lint

Runs `eslint src --ext .ts` to lint all TypeScript files in the `src` directory. It generates a report of linting issues in the codebase.

### lint:fix

Runs `eslint` with `--fix`, which automatically fixes lint errors where possible. This is useful for quickly resolving simple issues without manual intervention.

---

## Testing & Coverage

### coverage

Runs the [unit test](#testunit), then the [ui test](#uitest) and generates a coverage report.

#### Pre script {#precoverage}

Removes the `.nyc_output` directory, which is used to store coverage data from previous `playwright` runs and the `coverage` directory, which is used to store the coverage data from previous `vitest` runs. This ensures that each coverage run starts fresh.

### Post script {#postcoverage}

It creates the `coverage` directory if it does not exist and the `coverage/coverage-final.json` file if it doesn't exist. Then it copies the `coverage-final.json` file from the `coverage` directory to the `.nyc_output` directory as `unit-test.json`. Then, it merges the JSON files inside the `.nyc_output` directory into a single `coverage.json` file and generates a lcov coverage report that can be used by sonar.

### test:unit {#testunit}

Runs `vitest` to execute unit tests in the codebase and generate the coverage for it. It provides detailed output of the test results in a verbose way.

### test:unit:dev

Same as [test:unit](#testunit), but runs in watch mode, allowing developers to see test results in real-time as they make changes to the code.

### test:ui

Runs `playwright test` to execute UI tests. It uses the `BASE_URL` environment variable to determine the base URL for the tests, which is set to `http://localhost:3000` by default. It starts the frontend server before running the tests, ensuring that the application is available for testing.

### test:ui:dev

Runs `playwright test` in watch mode, allowing developers to see UI test results in real-time as they make changes to the code. It uses the `BASE_URL` environment variable to determine the base URL for the tests, which is set to `http://localhost:3000` by default. It starts the frontend server before running the tests, ensuring that the application is available for testing.
