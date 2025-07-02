import { Locator, Page } from "@playwright/test";
import { BasePage } from "./base-page"
import { routes } from "../utils/routes";

export class OpeningDetailsPage extends BasePage {
  private readonly openingId: string;

  private readonly openingSummary: Locator;
  private readonly map: Locator;
  private readonly mapButton: Locator;
  private readonly mapZoomInButton: Locator;
  private readonly mapZoomOutButton: Locator;
  private readonly mapExpandButton: Locator;
  private readonly mapLayersButton: Locator;

  private readonly overviewTabButton: Locator;
  private readonly tenureIndentificationTabButton: Locator;
  private readonly standardUnitsTabButton: Locator;
  private readonly activitiesTabButton: Locator;
  private readonly forestCoverTabButton: Locator;

  // Overview Section
  private readonly overviewSection: Locator;

  // Tenure Identification Section
  private readonly tenureIdentificationSection: Locator;
  private readonly tenureIdentificationTable: Locator;
  private readonly tenureIdentificationTableRows: Locator;
  private readonly tenureIdentificationTableSearchInput: Locator;
  private readonly tenureIdentificationSearchButton: Locator;

  // Stanrdard Units Section
  private readonly standardUnitsSection: Locator;
  private readonly standaardUnitsAccordions: Locator;

  // Activities Section
  private readonly activitiesSection: Locator;
  private readonly activitiesDisturbanceAccordion: Locator;
  private readonly activitiesDisturbanceTable: Locator;
  private readonly activitiesDisturbanceTableRows: Locator;
  private readonly activitiesDisturbanceTableSearchInput: Locator;

  private readonly activitiesSilvicultureAccordion: Locator;
  private readonly activitiesSilvicultureTable: Locator;
  private readonly activitiesSilvicultureTableRows: Locator;
  private readonly activitiesSilvicultureTableSearchInput: Locator;
  private readonly activitiesSilvicultureSearchButton: Locator;

  // Forest Cover Section
  private readonly forestCoverSection: Locator;
  private readonly forestCoverTable: Locator;
  private readonly forestCoverTableRows: Locator;
  private readonly forestCoverTableSearchInput: Locator;
  private readonly forestCoverSearchButton: Locator;

  constructor(page: Page, openingId: string) {
    super(page, routes.openingDetails(openingId));
    this.openingId = openingId;

    this.openingSummary = page.locator('.opening-summary-grid');
    this.map = page.locator('.leaflet-container');
    this.mapButton = page.getByTestId('toggle-map-button');
    this.mapZoomInButton = this.map.locator('.leaflet-control-zoom-in');
    this.mapZoomOutButton = this.map.locator('.leaflet-control-zoom-out');
    this.mapExpandButton = this.map.locator('.leaflet-control-fullscreen-button');
    this.mapLayersButton = page.locator('.leaflet-control-layers-toggle');

    this.overviewTabButton = page.getByRole('tab', { name: 'Overview' });
    this.tenureIndentificationTabButton = page.getByRole('tab', { name: 'Tenure Identification' });
    this.standardUnitsTabButton = page.getByRole('tab', { name: 'Standard Units' });
    this.activitiesTabButton = page.getByRole('tab', { name: 'Activities' });
    this.forestCoverTabButton = page.getByRole('tab', { name: 'Forest Cover' });

    //Overview Section
    this.overviewSection = page.getByRole('tabpanel', { name: 'Overview' })

    // Tenure Identification Section
    this.tenureIdentificationSection = page.getByRole('tabpanel', { name: 'Tenure identification' });
    this.tenureIdentificationTable = this.tenureIdentificationSection.getByRole('table', { name: 'Tenure identification table' });
    this.tenureIdentificationTableRows = this.tenureIdentificationTable.locator('tbody tr');
    this.tenureIdentificationTableSearchInput = this.tenureIdentificationSection.getByRole('searchbox', { name: 'Filter table' });
    this.tenureIdentificationSearchButton = this.tenureIdentificationSection.getByRole('button', { name: 'Search' });

    // Standard Units Section
    this.standardUnitsSection = page.getByRole('tabpanel', { name: 'Standard units' });
    this.standaardUnitsAccordions = this.standardUnitsSection.locator('.opening-standard-units-grid');

    // Activities Section
    this.activitiesSection = page.getByRole('tabpanel', { name: 'Activities' });
    this.activitiesDisturbanceAccordion = this.activitiesSection.getByTestId('disturbance-accordion-container');
    this.activitiesDisturbanceTable = this.activitiesDisturbanceAccordion.getByRole('table', { name: 'Disturbance table' });
    this.activitiesDisturbanceTableRows = this.activitiesDisturbanceTable.locator('tbody tr');
    this.activitiesDisturbanceTableSearchInput = this.activitiesDisturbanceAccordion.getByRole('searchbox', { name: 'Filter disturbances' });

    this.activitiesSilvicultureAccordion = this.activitiesSection.getByTestId('activity-accordion-container');
    this.activitiesSilvicultureTable = this.activitiesSilvicultureAccordion.getByRole('table', { name: 'Activity table' });
    this.activitiesSilvicultureTableRows = this.activitiesSilvicultureTable.locator('tbody tr');
    this.activitiesSilvicultureTableSearchInput = this.activitiesSilvicultureAccordion.getByRole('searchbox', { name: 'Filter table' })
    this.activitiesSilvicultureSearchButton = this.activitiesSilvicultureAccordion.getByRole('button', { name: 'Search' });

    // Forest Cover Section
    this.forestCoverSection = page.getByRole('tabpanel', { name: 'Forest Cover' });
    this.forestCoverTableSearchInput = this.forestCoverSection.getByRole('searchbox', { name: 'Filter table' });
    this.forestCoverSearchButton = this.forestCoverSection.getByRole('button', { name: 'Search' });
    this.forestCoverTable = this.forestCoverSection.getByRole('table', { name: 'Forest cover table' });
    this.forestCoverTableRows = this.forestCoverTable.locator('tbody tr');
  }

  async toggleMapButton() {
    await this.mapButton.click();
  }

  async isMapVisible() {
    return await this.map.isVisible();
  }

  async zoomInMap() {
    await this.mapZoomInButton.click();
  }

  async zoomOutMap() {
    await this.mapZoomOutButton.click();
  }

  async expandMap() {
    await this.mapExpandButton.click();
  }

  async hoverMapLayersButton() {
    await this.mapLayersButton.hover();
  }

  async clickOverviewTab() {
    await this.overviewTabButton.click();
  }

  async clickTenureIdentificationTab() {
    await this.tenureIndentificationTabButton.click();
  }

  async clickStandardUnitsTab() {
    await this.standardUnitsTabButton.click();
  }

  async clickActivitiesTab() {
    await this.activitiesTabButton.click();
  }

  async clickForestCoverTab() {
    await this.forestCoverTabButton.click();
  }

  async getCurrentActiveTab() {
    if (await this.overviewTabButton.getAttribute('aria-selected') === 'true') {
      return 'Overview';
    } else if (await this.tenureIndentificationTabButton.getAttribute('aria-selected') === 'true') {
      return 'Tenure identification';
    }
    else if (await this.standardUnitsTabButton.getAttribute('aria-selected') === 'true') {
      return 'Standard units';
    }
    else if (await this.activitiesTabButton.getAttribute('aria-selected') === 'true') {
      return 'Activities';
    }
    else if (await this.forestCoverTabButton.getAttribute('aria-selected') === 'true') {
      return 'Forest cover';
    }
    return 'Unknown';
  }

  async fillTenureIdentificationSearchInput(value: string) {
    await this.tenureIdentificationTableSearchInput.fill(value);
  }

  async clickTenureIdentificationSearchButton() {
    await this.tenureIdentificationSearchButton.click();
  }

  async clickStandardUnitsAccordion(standardUnitId: string) {
    const accordion = this.standaardUnitsAccordions.getByTestId(`standard-unit-accordion-${standardUnitId}`);
    await accordion.click();
  }

  async clickActivitiesDisturbancesAccordion() {
    await this.activitiesDisturbanceAccordion.click();
  }

  async clickActivitiesSilvicultureAccordion() {
    await this.activitiesSilvicultureAccordion.click();
  }

  async fillActivitiesDisturbanceSearchInput(value: string) {
    await this.activitiesDisturbanceTableSearchInput.fill(value);
  }

  async fillActivitiesSilvicultureSearchInput(value: string) {
    await this.activitiesSilvicultureTableSearchInput.fill(value);
  }

  async clickActivitiesSilvicultureSearchButton() {
    await this.activitiesSilvicultureSearchButton.click();
  }

  async fillForestCoverSearchInput(value: string) {
    await this.forestCoverTableSearchInput.fill(value);
  }

  async clickForestCoverSearchButton() {
    await this.forestCoverSearchButton.click();
  }
}
