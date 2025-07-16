import { validPaths } from "@/routes";

/**
 * Checks if a given path is a valid in-app redirect target.
 * Allows exact matches or paths that extend from known base routes.
 */
export const isValidRedirect = (pathToCheck: string | null): pathToCheck is string =>
  !!pathToCheck &&
  validPaths.some((valid) =>
    pathToCheck === valid || pathToCheck.startsWith(valid + '/') || pathToCheck.startsWith(valid + '?')
  );
