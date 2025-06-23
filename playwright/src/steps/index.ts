import { navigationSteps } from './navigation.steps';
import { assertionSteps } from './assertions.steps';
import { formSteps } from './form.steps';

const allSteps = [...navigationSteps, ...assertionSteps,...formSteps];

const generateCodeFromStep = (text: string): string => {
  for (const step of allSteps) {
    const match = text.match(step.regex);
    if (match) {
      const args = match.slice(1);
      if (typeof step.argCount === 'number' && args.length !== step.argCount) {
        return `throw new Error('Step matched but received ${args.length} args, expected ${step.argCount}: "${text}"');`;
      }

      try {
        return step.generate(args);
      } catch (e) {
        return `throw new Error('Step matched but failed to generate: ${e.message}');`;
      }
    }
  }
  return `throw new Error('Unhandled step: ${text}')`;
}

export default generateCodeFromStep;