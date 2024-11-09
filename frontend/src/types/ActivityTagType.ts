export enum ActivityTagTypeEnum {
  OPENING_DETAILS = 'Opening details',
  OPENING_REPORT = 'Opening report',
  OPENING_SPATIAL = 'Opening spatial',
  UPDATE = 'Update',
  CORRECTION = 'Correction',
  ORIGINAL = 'Original',
  SECTION_197 = 'Section 197',
  AMALGAMATE = 'Amalgamate',
  E_SUBMISSION = 'E-submission',
  MILESTONE = 'Milestone',
  AMENDED_MINOR = 'Amended (Minor)',
  SITE_PLAN_AMENDMENT = 'Site Plan Amendment',
  VARIATION = 'Variation',
  AMENDED = 'Amended',
  APPROVED = 'Approved',
  DELETED = 'Deleted',
  REJECTED = 'Rejected',
  RETIRED = 'Retired',
  REMOVED = 'Removed',
  SUBMITTED = 'Submitted',
  UNKNOWN = 'Unknown'
}

export type ActivityTagType = keyof typeof ActivityTagTypeEnum;

export enum ActivityTagFileFormatEnum {
  PDF_FILE = 'PDF file',
  CSV_FILE = 'CSV file',
  EXCEL_FILE = 'Excel file',
  UNKNOWN = 'Unknown'
}




