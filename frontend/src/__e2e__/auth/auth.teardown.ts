import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const storagePath = path.resolve(__dirname, './user.json');

async function globalTeardown() {
  fs.writeFileSync(storagePath, '{}', 'utf-8');
  console.log('[globalTeardown] Cleared user.json');
}

export default globalTeardown;
