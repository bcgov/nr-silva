import { ONE_SECOND } from "@/constants/TimeUnits";

/**
 * Simulates a delayed asynchronous response for mocking purposes.
 *
 * @template T The type of the data to be returned.
 * @param {T} data - The data to resolve after the delay.
 * @param {number} [timeout=ONE_SECOND] - The delay duration in milliseconds (default is ONE_SECOND).
 * @returns {Promise<T>} A Promise that resolves with the provided data after the specified delay.
 */
export const delayMock = <T>(data: T, timeout = ONE_SECOND): Promise<T> => (
  new Promise((resolve) => setTimeout(() => resolve(data), timeout))
);
