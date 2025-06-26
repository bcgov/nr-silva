import { Locator, Page } from "@playwright/test";

export class LoginPage {
  readonly page: Page;
  readonly idirLoginButton: Locator;
  readonly bceidLoginButton: Locator;
  readonly idirUsernameInput: Locator;
  readonly idirPasswordInput: Locator;
  readonly idirLoginSubmitButton: Locator;
  readonly bceidUsernameInput: Locator;
  readonly bceidPasswordInput: Locator;
  readonly bceidLoginSubmitButton: Locator;

  private baseUrl: string;

  constructor(page: Page) {
    this.page = page;
    this.baseUrl = process.env.BASE_URL ?? 'http://localhost:3000';
    this.idirLoginButton = page.getByRole('button', { name: 'Login with IDIR' });
    this.bceidLoginButton = page.getByRole('button', { name: 'Login with Business BCeID' });
    this.idirUsernameInput = page.locator('input[name="user"]');
    this.idirPasswordInput = page.locator('input[name="password"]');
    this.idirLoginSubmitButton = page.locator('input[name="btnSubmit"]');
    this.bceidUsernameInput = page.locator('input[name="user"]');
    this.bceidPasswordInput = page.locator('input[name="password"]');
    this.bceidLoginSubmitButton = page.locator('input[name="btnSubmit"]');
  }

  async goto() {
    await this.page.goto(`${this.baseUrl}`);
  }

  async loginWithIDIR(username: string, password: string) {
    await this.idirLoginButton.click();
    await this.page.waitForURL('https://logon*.gov.bc.ca/**');
    await this.idirUsernameInput.fill(username);
    await this.idirPasswordInput.fill(password);
    await this.idirLoginSubmitButton.click();
    await this.page.waitForURL('**/dashboard');
  }

  async loginWithBCEID(username: string, password: string) {
    await this.bceidLoginButton.click();
    await this.page.waitForURL('https://logon*.gov.bc.ca/**');
    await this.bceidUsernameInput.fill(username);
    await this.bceidPasswordInput.fill(password);
    await this.bceidLoginSubmitButton.click();
    await this.page.waitForURL('**/dashboard');
  }
}
