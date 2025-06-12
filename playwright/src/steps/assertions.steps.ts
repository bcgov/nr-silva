import { StepDefinition } from './stepTypes';

export const assertionSteps : StepDefinition[] = [
  {
    regex: /^I can read "(.*)"$/,
    argCount: 1,
    generate: (args: string[]) => `await expect(page.getByText('${args[0]}').first()).toBeVisible();`
  },
  {
    regex: /^I can see the page is called "(.*)"$/,
    argCount: 1,
    generate: (args: string[]) => `await expect(page).toHaveTitle('${args[0]}');`
  }
];
