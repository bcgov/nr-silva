export interface StepDefinition {
  regex: RegExp;
  argCount: number;
  generate: (args: string[]) => string;
}

export type QuantityAttribute = "at least" | "at most" | "exactly";

export const validAttributes = new Set<QuantityAttribute>(["at least", "at most", "exactly"]);