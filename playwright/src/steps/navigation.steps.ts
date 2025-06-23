import { StepDefinition } from './stepTypes';

export const navigationSteps : StepDefinition[] = [
  {
    regex: /^I visit "(.*)"$/,
    argCount: 1,
    generate: ([url]: string[]) => `await page.goto('/${url}');`
  }
];
