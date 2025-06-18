import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

export const loadStub = (...segments: string[]): string => {
  return fs.readFileSync(
    path.resolve(__dirname, '../../../stub/__files', ...segments),
    'utf-8'
  );
}