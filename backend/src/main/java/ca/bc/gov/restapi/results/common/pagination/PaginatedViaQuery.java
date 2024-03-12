package ca.bc.gov.restapi.results.common.pagination;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** This class represents a pagination documentation. */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
    in = ParameterIn.QUERY,
    description = "Zero-based page index indicating the page to be returned.",
    name = "page",
    schema = @Schema(type = "integer", defaultValue = "0", minimum = "0"))
@Parameter(
    in = ParameterIn.QUERY,
    description = "The maximum number of results in a page.",
    name = "pageSize",
    schema = @Schema(type = "integer", defaultValue = "5", minimum = "1"))
public @interface PaginatedViaQuery {}
