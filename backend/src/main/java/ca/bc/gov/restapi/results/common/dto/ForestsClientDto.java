package ca.bc.gov.restapi.results.common.dto;

public record ForestsClientDto(
    String clientNumber,
    String clientName,
    String legalFirstName,
    String legalMiddleName,
    String clientStatusCode,
    Character clientTypeCode,
    String acronym) {}
