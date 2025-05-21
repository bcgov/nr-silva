import type { OpeningDetailsStockingLayerDto, OpeningDetailsStockingSpeciesDto } from '@/types/OpeningTypes';

/**
 * Counts the number of unique species by `species.code` from two species arrays.
 *
 * @param preferred - Array of preferred species
 * @param acceptable - Array of acceptable species
 * @returns The count of unique species codes
 */
export const countUniqueSpeciesByCode = (
  preferred: OpeningDetailsStockingSpeciesDto[],
  acceptable: OpeningDetailsStockingSpeciesDto[]
): number => {
  const codeSet = new Set<string>();

  [...preferred, ...acceptable].forEach((item) => {
    if (item.species.code) {
      codeSet.add(item.species.code);
    }
  });

  return codeSet.size;
};

/**
 * Checks if there is only one stocking layer.
 *
 * @param layers - Array of stocking layer DTOs.
 * @returns True if there is exactly one layer, otherwise false.
 */
export const isSingleLayer = (layers: OpeningDetailsStockingLayerDto[]): boolean => (
  layers.length === 1
);
