package ca.bc.gov.restapi.results.common.dto;

import java.time.LocalDateTime;

/** This record holds messages and its local date time when it happended. */
public record OracleLogDto(String message, LocalDateTime eventTime) {}
