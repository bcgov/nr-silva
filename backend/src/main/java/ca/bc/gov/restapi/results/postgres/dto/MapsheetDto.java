package ca.bc.gov.restapi.results.postgres.dto;

/**
 * Internal DTO holding the parsed components of a BCGS 1:20K mapsheet tile key.
 *
 * @param grid the NTS 1:250K sheet number (chars 0–2, e.g. "092")
 * @param letter the NTS 1:50K block letter (char 3, e.g. "L")
 * @param square the BCGS 1:20K block number (chars 4–6, e.g. "057")
 * @param quad the quadrant ("0" for standard 20K resolution)
 * @param subQuad the sub-quadrant ("0" for standard 20K resolution)
 */
public record MapsheetDto(
    String grid,
    String letter,
    String square,
    String quad,
    String subQuad) {}
