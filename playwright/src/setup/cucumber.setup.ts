import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import { Parser, AstBuilder, GherkinClassicTokenMatcher } from '@cucumber/gherkin';
import { FeatureChild, IdGenerator } from '@cucumber/messages';

import generateCodeFromStep from '../steps/index';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const featuresDir = path.resolve(__dirname,'../features');
const outputDir = path.resolve(__dirname,'../generated');


const generateTestContent = (child: FeatureChild, steps: string[]): string => {
  if (!child.scenario) return '';
  return `
  test('${child.scenario.name}', async ({ page }) => {
    ${steps.join('\n\n    ')}
  });`;
}

const generateSpecFileContent = (feature, scenarioBlocks: string) => {
  return `
import { test, expect } from '@playwright/test';
import { QuantityAttribute, validAttributes } from '../steps/stepTypes';

test.describe('${feature.name}', () => {
  ${scenarioBlocks}
});
`;
}

const parseFeatureChild = (child: FeatureChild) => {
  if (!child.scenario) return '';
  const steps = child.scenario.steps.map(step => generateCodeFromStep(step.text));
  return generateTestContent(child, steps);
}

const generateSpecs = () => {
  const parser = new Parser(new AstBuilder(IdGenerator.uuid()), new GherkinClassicTokenMatcher());

  fs.rmSync(outputDir, { recursive: true, force: true });
  fs.mkdirSync(outputDir, { recursive: true });

  const files = fs.readdirSync(featuresDir).filter(f => f.endsWith('.feature'));

  for (const file of files) {
    const content = fs.readFileSync(path.join(featuresDir, file), 'utf-8');
    console.log(`[spec-gen] Processing: ${file}`);

    const gherkinDoc = parser.parse(content);
    const feature = gherkinDoc.feature;
    if (!feature) continue;    
    console.log(`[spec-gen] Feature: ${feature.name}`);

    const scenarioBlocks = feature.children.map(parseFeatureChild).join('\n');
    const generated = generateSpecFileContent(feature, scenarioBlocks);

    const outputPath = path.join(outputDir, file.replace('.feature', '.spec.ts'));
    fs.writeFileSync(outputPath, generated);

    console.log(`[spec-gen] Generated: ${outputPath}`);
  }
}

export default generateSpecs;

