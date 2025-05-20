import React from 'react';
import { DefinitionTooltip } from '@carbon/react';
import { PLACE_HOLDER, UNIQUE_CHARACTERS_UNICODE } from '@/constants';
import { OpeningDetailsStockingSpeciesDto } from '@/types/OpeningTypes';

const SpeciesTooltipList: React.FC<{
  speciesList: OpeningDetailsStockingSpeciesDto[];
  layerCode: string;
}> = ({ speciesList, layerCode }) => {
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
              definition={
                `${species.species.description}` +
                (species.minHeight
                  ? ` ${UNIQUE_CHARACTERS_UNICODE.BULLET} ${species.minHeight}`
                  : '')
              }
            >
              {`${species.species.code}` +
                (species.minHeight
                  ? ` ${UNIQUE_CHARACTERS_UNICODE.BULLET} ${species.minHeight}`
                  : '')}
            </DefinitionTooltip>
          ) : (
            PLACE_HOLDER
          )
        )}
    </>
  );
};

export default SpeciesTooltipList;
