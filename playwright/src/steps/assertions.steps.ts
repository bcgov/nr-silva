import { StepDefinition, QuantityAttribute, validAttributes } from './stepTypes';

export const assertionSteps : StepDefinition[] = [
  {
    regex: /^I can read "(.*)"$/,
    argCount: 1,
    generate: ([text]: string[]) => `await expect(page.getByText('${text}').first()).toBeVisible();`
  },
  {
    regex: /^I can see the page is called "(.*)"$/,
    argCount: 1,
    generate: ([title]: string[]) => `await expect(page).toHaveTitle('${title}');`
  },
  {
    regex: /^I cannot see "(.*)"$/,
    argCount: 1,
    generate: ([text]: string[]) => `await expect(page.locator('text="${text}"')).toHaveCount(0);`
  },
  {
    regex: /^I can see the page URL is "(.*)"$/,
    argCount: 1,
    generate: ([url]: string[]) => `await page.waitForURL('**/${url}');`
  },
  {
    regex: /^I can see the title "(.*)"$/,
    argCount: 1,
    generate: ([title]: string[]) => `await expect(page.getByRole('heading', { name: '${title}' })).toBeVisible();`
  },
  {
    regex: /^I can see the link "(.*)"$/,
    argCount: 1,
    generate: ([link]: string[]) => `await expect(page.getByRole('link', { name: '${link}' })).toBeVisible();`
  },
  {
    regex: /^I can see the button "(.*)"$/,
    argCount: 1,
    generate: ([button]: string[]) => `await expect(page.getByRole('button', { name: '${button}' })).toBeVisible();`
  },
  {
    regex: /^I can see the element with id "(.*)"$/,
    argCount: 1,
    generate: ([id]: string[]) => `await expect(page.getByTestId('${id}')).toBeVisible();`
  },
  {
    regex: /^I can see the element with class "(.*)"$/,
    argCount: 1,
    generate: ([className]: string[]) => `await expect(page.locator('.${className}')).toBeVisible();`
  },
  {
    regex: /^I can see "(.*)" "(.*)" result on the table "(.*)"$/,
    argCount: 3,
    generate: ([attribute, quantity, tableName]: string[]) => 
      `const attribute = '${attribute}';
    const quantity = parseInt('${quantity}', 10);
    if (!validAttributes.has(attribute as QuantityAttribute)) {
      throw new Error('Invalid quantity attribute: ${attribute}');
    }
    const typedAttribute = attribute as QuantityAttribute;

    await expect(page.getByRole('table', { name: 'loading table data' })).toHaveCount(0, {
      timeout: 10000
    });

    const realTable = page.getByRole('table', {name: '${tableName}'});
    await expect(realTable).toBeVisible({
      timeout: 30000
    });
    
    const bodyRows = realTable.locator('tbody > tr');    
    const rowCount = await bodyRows.count();

    switch (typedAttribute) {
      case 'at least':
        if (rowCount < quantity) {
          throw new Error('Expected at least ${quantity} row in the table, but found '+ rowCount);
        }
        break;
      case 'at most':
        if (rowCount > quantity) {
          throw new Error('Expected at most ${quantity} rows in the table, but found ' + rowCount);
        }
        break;
      case 'exactly':
        if (rowCount !== quantity) {
          throw new Error('Expected exactly ${quantity} rows in the table, but found ' + rowCount);
        }
        break;
      default:
        throw new Error('Unknown attribute: ${attribute}');
    }`
  }
];
