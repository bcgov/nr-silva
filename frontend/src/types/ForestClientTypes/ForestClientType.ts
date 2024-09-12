export type ForestClientCodeDescription = {
  code: string,
  description: string
};

export type ForestClientType = {
  clientNumber: string,
  clientName: string,
  legalFirstName: string,
  legalMiddleName: string,
  clientStatusCode: ForestClientCodeDescription,
  clientTypeCode: ForestClientCodeDescription,
  acronym: string
};
