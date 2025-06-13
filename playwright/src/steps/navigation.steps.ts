import { StepDefinition } from './stepTypes';

export const navigationSteps : StepDefinition[] = [
  {
    regex: /^I visit "(.*)"$/,
    argCount: 1,
    generate: (args: string[]) => `await page.goto('${args[0]}');`
  }
];
