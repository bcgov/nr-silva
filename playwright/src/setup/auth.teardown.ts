import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

async function setupTeardown() {
  const files = fs.readdirSync(__dirname);

  files.forEach((file) => {
    if (file.startsWith('user.') && file.endsWith('.json')) {
      const fullPath = path.join(__dirname, file);
      fs.writeFileSync(fullPath, '{}', 'utf-8');
      console.log(`[setupTeardown] Cleared ${file}`);
    }
  });
}

export default setupTeardown;
