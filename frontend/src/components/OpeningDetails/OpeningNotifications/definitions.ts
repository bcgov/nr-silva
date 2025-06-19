export const statusToKind = {
  ERROR: "error",
  WARNING: "warning",
  INFO: "info",
  SUCCESS: "success",
} as const;

export const statusOrder = {
  ERROR: 0,
  WARNING: 1,
  INFO: 2,
  SUCCESS: 3,
};
