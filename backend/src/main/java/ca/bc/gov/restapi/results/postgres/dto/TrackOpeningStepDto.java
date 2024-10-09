package ca.bc.gov.restapi.results.postgres.dto;

public record TrackOpeningStepDto(
    int step, // sequential step starting from 1
    String status, // one of 'complete' or 'invalid'
    String description, // what happened. E.g.: Opening Id, Disturbance activity, activity planned
    String subtitle // when it happened if complete, or 'Please, update...' if invalid
    ) {}
