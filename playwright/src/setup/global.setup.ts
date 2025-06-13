import authSetup from './auth.setup';
import generateSpecs from './cucumber.setup'

async function globalSetup() {
  await authSetup();
  await generateSpecs();
}

export default globalSetup;
