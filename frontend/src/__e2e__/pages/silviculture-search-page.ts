import { Locator, Page } from "@playwright/test";
import { routes } from "../utils/routes";
import { BasePage } from "./base-page";

export class SilvicultureSearchPage extends BasePage {
  private readonly openingsTab: Locator;
  private readonly mainSearchInput: Locator;
  private readonly categoryDropdownButton: Locator;
  private readonly orgUnitDropdownButton: Locator;
  private readonly statusDropdownButton: Locator;
  private readonly searchButton: Locator;
  private readonly advancedSearchButton: Locator;
  private readonly searchResultsTable: Locator;
  private readonly mapButton: Locator;
  private readonly editColumnButton: Locator;
  private readonly map: Locator;

  // Adavanced Search Modal Elements
  private readonly advancedSearchModal: Locator;
  private readonly advancedSearchCloseButton: Locator;
  private readonly advancedSearchSearchInput: Locator;
  private readonly advancedSearchCreatedByMeCheckbox: Locator;
  private readonly advancedSearchFrpaCheckbox: Locator;
  private readonly advancedSearchCategoryDropdownButton: Locator;
  private readonly advancedSearchOrgUnitDropdownButton: Locator;
  private readonly advancedSearchStatusDropdownButton: Locator;
  private readonly advancedSearchClientDropdownInput: Locator;
  private readonly advancedSearchClientDropdownButton: Locator;
  private readonly advancedSearchLocationCodeInput: Locator;
  private readonly advancedSearchDateTypeDropdownButton: Locator;
  private readonly advancedSearchStartDateInput: Locator;
  private readonly advancedSearchEndDateInput: Locator;
  private readonly advancedSearchCutBlockInput: Locator;
  private readonly advancedSearchCuttingPermitInput: Locator;
  private readonly advancedSearchTimberMarkInput: Locator;
  private readonly advnacedSearchCancelButton: Locator;
  private readonly advancedSearchSearchButton: Locator;

  constructor(page: Page) {
    super(page, routes.silvicultureSearch());

    this.openingsTab = page.getByRole('tabpanel', { name: 'Openings' });
    this.mainSearchInput = page.getByTestId('main-search-term-input');
    this.categoryDropdownButton = page.locator('#category-multi-select-toggle-button');
    this.orgUnitDropdownButton = page.locator('#orgunit-multiselect-toggle-button');
    this.statusDropdownButton = page.locator('#status-multiselect').locator('#status-multiselect-toggle-button');
    this.searchButton = page.locator('.search-buttons-container').locator('#outer-search-button');
    this.advancedSearchButton = page.locator('.search-buttons-container').locator('.advanced-search-button');
    this.searchResultsTable = page.getByRole('table', { name: 'Opening search table' });
    this.mapButton = page.locator('.map-button');
    this.editColumnButton = page.getByRole('button', { name: 'Edit columns' });
    this.map = page.locator('.leaflet-container');
    this.searchResultsTable = page.getByRole('table', { name: 'Opening search table' });

    this.advancedSearchModal = page.getByTestId('advanced-search-modal');
    this.advancedSearchCloseButton = this.advancedSearchModal.locator('.bx--modal-close-button')
    this.advancedSearchSearchInput = this.advancedSearchModal.getByTestId('advanced-search-input');
    this.advancedSearchCreatedByMeCheckbox = this.advancedSearchModal.getByTestId('created-by-me-checkbox');
    this.advancedSearchFrpaCheckbox = this.advancedSearchModal.getByTestId('frpa-checkbox');
    this.advancedSearchCategoryDropdownButton = this.advancedSearchModal.locator('#advanced-category-multiselect-toggle-button');
    this.advancedSearchOrgUnitDropdownButton = this.advancedSearchModal.locator('#advanced-orgunit-multiselect-toggle-button');
    this.advancedSearchStatusDropdownButton = this.advancedSearchModal.locator('#advanced-status-multiselect-toggle-button');
    this.advancedSearchClientDropdownInput = this.advancedSearchModal.locator('#opening-advanced-search-client-input');
    this.advancedSearchClientDropdownButton = this.advancedSearchModal.locator('#downshift-«r1d»-toggle-button');
    this.advancedSearchLocationCodeInput = this.advancedSearchModal.locator('#opening-advanced-location-code-input');
    this.advancedSearchDateTypeDropdownButton = this.advancedSearchModal.locator('.date-filter-container').locator('#downshift-«r1j»-toggle-button');
    this.advancedSearchStartDateInput = this.advancedSearchModal.getByRole('textbox', { name: 'Start Date' });
    this.advancedSearchEndDateInput = this.advancedSearchModal.getByRole('textbox', { name: 'End Date' });
    this.advancedSearchCutBlockInput = this.advancedSearchModal.locator('#cut-block-text-input');
    this.advancedSearchCuttingPermitInput = this.advancedSearchModal.locator('#cutting-permit-text-input');
    this.advancedSearchTimberMarkInput = this.advancedSearchModal.locator('#timber-mark-text-input');
    this.advnacedSearchCancelButton = this.advancedSearchModal.getByTestId('advanced-modal-cancel-button');
    this.advancedSearchSearchButton = this.advancedSearchModal.locator('#modal-search-button-sm');
  }

  async isOpeningsTabVisible() {
    return await this.openingsTab.isVisible();
  }

  async fillMainSearchInput(value: string) {
    await this.mainSearchInput.fill(value);
  }

  async isSearchResultsTableVisible() {
    return await this.searchResultsTable.isVisible();
  }

  async chooseCategoryDropdownOption(option: string) {
    await this.categoryDropdownButton.click();
    const menu = this.page.locator('#category-multi-select__menu');
    await menu.getByRole('option', { name: option }).click();
  }

  async chooseOrgUnitDropdownOption(option: string) {
    await this.orgUnitDropdownButton.click();
    const menu = this.page.locator('#orgunit-multiselect__menu');
    await menu.getByRole('option', { name: option }).click();
  }

  async chooseStatusDropdownOption(option: string) {
    await this.statusDropdownButton.click();
    const menu = this.page.locator('#status-multiselect__menu');
    await menu.getByRole('option', { name: option }).click();
  }

  async clickSearchButton() {
    await this.searchButton.click();
    await this.searchResultsTable.waitFor({ state: 'visible' });
  }

  async getTotalSearchResultsCount() {
    const totalRowsComponent = this.page.locator('.total-rows-display');
    const text = await totalRowsComponent.textContent();
    const match = text?.match(/Total search results:\s*(\d+)/);
    return match && match[1] ? parseInt(match[1], 10) : 0;
  }

  async openAdvancedSearchModal() {
    await this.advancedSearchButton.click();
    await this.advancedSearchModal.waitFor({ state: 'visible' });
  }

  async closeAdvancedSearchModal() {
    await this.advancedSearchCloseButton.click();
    await this.advancedSearchModal.waitFor({ state: 'hidden' });
  }

  async isAdvancedSearchModalOpen() {
    const hiddenAttribute = await this.advancedSearchModal.getAttribute('aria-hidden');
    return hiddenAttribute ? hiddenAttribute === 'false' : true;
  }

  private async getSearchResultsTableRowByOpeningId(openingId: string) {
    const row = this.searchResultsTable.getByTestId(`opening-table-row-${openingId}`);
    await row.waitFor({ state: 'visible' });
    return row;
  }

  async isOpeningInSearchResults(openingId: string) {
    try {
      await this.getSearchResultsTableRowByOpeningId(openingId);
      return true;
    } catch (error) {
      return false;
    }
  }

  async clickMapButton() {
    await this.mapButton.click();
  }

  async isMapVisible() {
    return await this.map.isVisible();
  }

  async checkOpening(openingId: string) {
    const row = await this.getSearchResultsTableRowByOpeningId(openingId);
    const checkbox = row.getByTestId('opening-table-cell-actions-60000').locator('label');
    await checkbox.click();
  }

  async isOpeningVisibleOnMap(): Promise<boolean> {
    const overlayPane = this.page.locator('.leaflet-overlay-pane');
    const gTag = overlayPane.locator('svg > g');
    // Wait for the overlay pane to be attached (optional, depending on your flow)
    await overlayPane.waitFor({ state: 'attached' });
    // Check if the <g> tag has any child elements (e.g., <path>)
    const childCount = await gTag.evaluate((g) => g.children.length);
    return childCount > 0;
  }
}
