export interface StepDefinition {
  regex: RegExp;
  argCount: number;
  generate: (args: string[]) => string;
}