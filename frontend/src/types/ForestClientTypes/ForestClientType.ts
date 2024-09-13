export type ForestClientCodeDescription = {
  code: string,
  description: string
};

export type ForestClientType = {
  clientNumber: string,
  clientName: string,
  legalFirstName: string,
  legalMiddleName: string,
  clientStatus: ForestClientCodeDescription,
  clientType: ForestClientCodeDescription,
  acronym: string
};
