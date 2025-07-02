import { expect } from '@playwright/test';
import { test } from '../setup/coverage';
import { loadStub } from '../utils/stubLoader';
import { SilvicultureSearchPage } from '../pages/silviculture-search-page';

test.describe('Silviculture Search', () => {
  let silvicultureSearchPage: SilvicultureSearchPage;
  const openingId = '60000';
  const category = 'FTML - Forest Tenure - Major Licensee';
  const orgUnit = 'TWO - Design Unit';
  const status = 'FG - Free Growing';
  const client = '1000175 B.C. LTD., 00170022';
  const locationCode = '03 - No name provided';
  const dateType = 'Disturbance';
  const startDate = '2025/01/01';
  const endDate = '2025/01/01';
  const cutBlock = '1000000';
  const cuttingPermit = '1000000';
  const timberMark = '1000000';


  test.beforeEach(async ({ page }) => {
    silvicultureSearchPage = new SilvicultureSearchPage(page);
    await silvicultureSearchPage.goto();

    // Intercept global data calls return a stubbed response
    // Intercept the recent openings API call and return a stubbed response
    await page.route('**/api/openings/recent**', async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub('openings', 'recent-start.json'),
      });
    });

    // Intercept the org units API call and return a stubbed response
    await page.route('**/api/codes/org-units**', async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub('codes/org-units.json'),
      });
    })

    // Intercept the user submission trends API call and return a stubbed response
    await page.route('**/api/users/submission-trends**', async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub('users', 'submission-trends.json'),
      });
    });

    // Intercept the favorites API call and return a stubbed response
    await page.route('**/api/openings/favourites**', async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub('openings/favourites.json'),
      });
    });

    //Intercept the categories API call and return a stubbed response
    await page.route('**/api/codes/categories**', async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub('codes/categories.json'),
      });
    });

    await page.route('**/api/forest-clients/byNameAcronymNumber*', async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub('forest-clients/clients.json'),
      });
    });

    await page.route('**/api/forest-clients/*/locations', async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub('forest-clients/locations_00170022.json'),
      });
    });

    await page.route('**/api/openings/search*', async route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: loadStub('search/search_openings_60000.json'),
      });
    });
  });

  test('header should be visible', async () => {
    expect(await silvicultureSearchPage.getHeading()).toBe('Silviculture Search');
    expect(await silvicultureSearchPage.isHeadingVisible()).toBe(true);
  });

  test('advanced search modal should open and close', async () => {
    await silvicultureSearchPage.openAdvancedSearchModal();
    await expect(await silvicultureSearchPage.isAdvancedSearchModalOpen()).toBe(true);
    await silvicultureSearchPage.closeAdvancedSearchModal();
    await expect(await silvicultureSearchPage.isAdvancedSearchModalOpen()).toBe(false);
  });

  test('should search by opening ID', async () => {
    await silvicultureSearchPage.fillMainSearchInput(openingId);
    await silvicultureSearchPage.clickSearchButton();

    expect(await silvicultureSearchPage.isSearchResultsTableVisible()).toBe(true);
    const totalCount = await silvicultureSearchPage.getTotalSearchResultsCount();
    expect(totalCount).toBe(1);
    expect(await silvicultureSearchPage.isOpeningInSearchResults(openingId)).toBe(true);
  });

  test('should search by category ', async () => {
    await silvicultureSearchPage.chooseCategoryDropdownOption(category);
    await silvicultureSearchPage.clickSearchButton();

    expect(await silvicultureSearchPage.isSearchResultsTableVisible()).toBe(true);

    const totalCount = await silvicultureSearchPage.getTotalSearchResultsCount();
    expect(totalCount).toBe(1);
  });

  test('should search by org unit', async () => {
    await silvicultureSearchPage.chooseOrgUnitDropdownOption(orgUnit);
    await silvicultureSearchPage.clickSearchButton();

    expect(await silvicultureSearchPage.isSearchResultsTableVisible()).toBe(true);
    const totalCount = await silvicultureSearchPage.getTotalSearchResultsCount();
    expect(totalCount).toBe(1);
  });

  test('should search by status', async () => {
    await silvicultureSearchPage.chooseStatusDropdownOption(status);
    await silvicultureSearchPage.clickSearchButton();

    expect(await silvicultureSearchPage.isSearchResultsTableVisible()).toBe(true);
    const totalCount = await silvicultureSearchPage.getTotalSearchResultsCount();
    expect(totalCount).toBe(1);
  });

  test('should search by multiple criteria', async () => {
    await silvicultureSearchPage.fillMainSearchInput(openingId);
    await silvicultureSearchPage.chooseCategoryDropdownOption(category);
    await silvicultureSearchPage.chooseOrgUnitDropdownOption(orgUnit);
    await silvicultureSearchPage.chooseStatusDropdownOption(status);
    await silvicultureSearchPage.clickSearchButton();

    expect(await silvicultureSearchPage.isSearchResultsTableVisible()).toBe(true);
    const totalCount = await silvicultureSearchPage.getTotalSearchResultsCount();
    expect(totalCount).toBe(1);
    expect(await silvicultureSearchPage.isOpeningInSearchResults(openingId)).toBe(true);
  });

  test('should search opening, check it, and verify map visibility', async () => {
    await silvicultureSearchPage.fillMainSearchInput(openingId);
    await silvicultureSearchPage.clickSearchButton();

    expect(await silvicultureSearchPage.isSearchResultsTableVisible()).toBe(true);
    const totalCount = await silvicultureSearchPage.getTotalSearchResultsCount();
    expect(totalCount).toBe(1);
    expect(await silvicultureSearchPage.isOpeningInSearchResults(openingId)).toBe(true);

    await silvicultureSearchPage.clickMapButton();
    expect(await silvicultureSearchPage.isMapVisible()).toBe(true);

    await silvicultureSearchPage.checkOpening(openingId);
    expect(await silvicultureSearchPage.isOpeningVisibleOnMap()).toBe(true);
  });

  test('should fill advance search by opening ID ', async () => {
    await silvicultureSearchPage.openAdvancedSearchModal();
    await silvicultureSearchPage.fillAdvancedSearchInput(openingId);

    await silvicultureSearchPage.toggleCreatedByMeCheckbox();
    await silvicultureSearchPage.toggleFrpaCheckbox();

    await silvicultureSearchPage.chooseAdvancedCategoryDropdownOption(category);
    await silvicultureSearchPage.chooseAdvancedOrgUnitDropdownOption(orgUnit);
    await silvicultureSearchPage.chooseAdvancedStatusDropdownOption(status);

    await silvicultureSearchPage.chooseAdvancedClientDropdownOption(client);
    await silvicultureSearchPage.chooseAdvancedLocationCodeInput(locationCode);

    await silvicultureSearchPage.chooseAdvancedDateTypeDropdownOption(dateType);
    await silvicultureSearchPage.fillAdvancedSearchStartDate(startDate);
    await silvicultureSearchPage.fillAdvancedSearchEndDate(endDate);

    await silvicultureSearchPage.fillAdvancedSearchCutBlock(cutBlock);
    await silvicultureSearchPage.fillAdvancedSearchCuttingPermit(cuttingPermit);
    await silvicultureSearchPage.fillAdvancedSearchTimberMark(timberMark);

    await silvicultureSearchPage.clickAdavancedSearchSearchButton();
  });
});
