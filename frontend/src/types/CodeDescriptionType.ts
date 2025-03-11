/**
 * Represents a code-description pair commonly used for dropdowns or filters.
 *
 * @template T - A string literal type representing valid codes. Defaults to `string` for general use.
 *
 * @example
 * // Generic usage with default string
 * const item: CodeDescriptionDto = { code: "any-string", description: "Description" };
 *
 * @example
 * // Strictly typed usage
 * type Status = "open" | "closed";
 * const statusItem: CodeDescriptionDto<Status> = { code: "open", description: "Open status" };
 */
type CodeDescriptionDto<T extends string = string> = {
  code: T;
  description: string;
};

export default CodeDescriptionDto;
