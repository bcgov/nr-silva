import { StepDefinition } from './stepTypes';

export const formSteps: StepDefinition[] = [
  {
    regex: /^I click on the "(.*)" button$/,
    argCount: 1,
    generate: ([name]: string[]) =>
      `const button = page.getByRole('button', { name: '${name}' });
    await button.waitFor({ state: 'visible' });
    await button.click();`
  },
  {
    regex: /^I search for "(.*)"$/,
    argCount: 1,
    generate: ([value]: string[]) =>
      `const searchInput = page.getByRole('searchbox', { name: 'Search' });
    await searchInput.fill('${value}');
    await searchInput.press('Enter');`
  }

];