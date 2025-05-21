package ca.bc.gov.restapi.results.oracle.dto;

public record SimplePageDto(
    long size,
    long number,
    long totalElements,
    long totalPages
) {

}
