import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const outputDir = path.resolve(__dirname,'../generated');

async function cucumberTeardown() {
  // Clean up the output directory
  fs.rmSync(outputDir, { recursive: true, force: true });
  // Recreate the output directory
  fs.mkdirSync(outputDir, { recursive: true });
  // Create a .gitkeep file to ensure the directory is tracked by git
  fs.writeFileSync(path.join(outputDir, '.gitkeep'),'');
}

export default cucumberTeardown;
