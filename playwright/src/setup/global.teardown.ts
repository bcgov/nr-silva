import setupTeardown from "./auth.teardown";
import cucumberTeardown from "./cucumber.teardown";

async function globalTeardown() {
  await setupTeardown();
  await cucumberTeardown();
}

export default globalTeardown;