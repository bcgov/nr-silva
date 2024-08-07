package ca.bc.gov.restapi.results.common.dto;

import java.time.LocalDateTime;

/** This record holds messages and its local date time when it happened. */
public record OracleLogDto(String message, LocalDateTime eventTime) {}
