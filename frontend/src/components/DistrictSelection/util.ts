import { ForestClientDto } from "@/services/OpenApi";

export const filterClientByKeyword = (client: ForestClientDto, keyword: string): boolean => {
  return [
    client.acronym,
    client.clientName,
    client.clientNumber,
    client.legalFirstName,
    client.legalMiddleName,
    client.name
  ]
    .filter((field): field is string => Boolean(field))
    .some(field => field.trim().toLowerCase().includes(keyword.toLowerCase()));
};
