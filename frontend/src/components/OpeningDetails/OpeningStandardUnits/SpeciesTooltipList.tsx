import React from 'react';
import { DefinitionTooltip } from '@carbon/react';
import { PLACE_HOLDER, UNIQUE_CHARACTERS_UNICODE } from '@/constants';
import { OpeningDetailsStockingSpeciesDto } from '@/types/OpeningTypes';
import { codeDescriptionToDisplayText } from '../../../utils/multiSelectUtils';

const SpeciesTooltipList: React.FC<{
  speciesList: OpeningDetailsStockingSpeciesDto[];
  layerCode: string;
}> = ({ speciesList, layerCode }) => {

  const minHeightSuffix = (ssuSpecies: OpeningDetailsStockingSpeciesDto) => (
    ssuSpecies.minHeight
      ? ` ${UNIQUE_CHARACTERS_UNICODE.BULLET} ${ssuSpecies.minHeight} m`
      : ''
  );

  return (
    <>
      {speciesList
        .filter((species) => species.layer === layerCode)
        .map((species, index) =>
          species.species.code ? (
            <DefinitionTooltip
              key={`${species.species.code}-${index}`}
              openOnHover
              className="default-cell-definition-tooltip"
              align="right-bottom"
              definition={`${codeDescriptionToDisplayText(species.species)}${minHeightSuffix(species)}`}
            >
              {`${species.species.code}${minHeightSuffix(species)}`}
            </DefinitionTooltip>
          ) : (
            PLACE_HOLDER
          )
        )}
    </>
  );
};

export default SpeciesTooltipList;
