package ca.bc.gov.restapi.results.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The enum Audit action code enum.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum AuditActionCodeEnum {
  UPD("UPD", "Update"),
  COR("COR", "Correction"),
  O("O", "Original"),
  SEC197("197", "Section 197"),
  AMG("AMG", "Amalgamate"),
  ES("ES", "E-submission"),
  MIL("MIL", "Milestone"),
  MIN("MIN", "Amended (Minor)"),
  SPA("SPA", "Site Plan Amendment"),
  VAR("VAR", "Variation"),
  AMD("AMD", "Amended"),
  APP("APP", "Approved"),
  DEL("DEL", "Deleted"),
  REJ("REJ", "Rejected"),
  RET("RET", "Retired"),
  RMD("RMD", "Removed"),
  SUB("SUB", "Submitted");

  private final String code;
  private final String description;

  /**
   * Parse the audit action code enum from the given {@link String} code.
   *
   * @param code the code
   * @return the audit action code enum
   */
  public static AuditActionCodeEnum of(String code) {
    for (AuditActionCodeEnum value : AuditActionCodeEnum.values()) {
      if (value.getCode().equals(code)) {
        return value;
      }
    }
    return null;
  }
}
