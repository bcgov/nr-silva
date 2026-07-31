package ca.bc.gov.restapi.results.common.projection;

/** Projection used to validate a cut block and retrieve key lookup fields. */
public interface CutBlockValidationProjection {

  /** Returns the unique identifier for the cut block. */
  Long getCbSkey();

  /** Returns the timber mark associated with the cut block. */
  String getTimberMark();
}
