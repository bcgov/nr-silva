package ca.bc.gov.restapi.results.oracle.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@RegisterReflectionForBinding
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

  public static AuditActionCodeEnum of(String code) {
    for (AuditActionCodeEnum value : AuditActionCodeEnum.values()) {
      if (value.getCode().equals(code)) {
        return value;
      }
    }
    return null;
  }
}
