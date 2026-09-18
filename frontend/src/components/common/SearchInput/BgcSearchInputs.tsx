import { useRef } from 'react';
import { Column, Grid, TextInput } from '@carbon/react';
import useRefWithSearchParam from '@/hooks/useRefWithSearchParam';
import { handleAutoUpperInput, handleAutoUpperPaste } from '@/utils/InputUtils';
import {
  BEC_SITE_PHASE_MAX_LENGTH,
  BEC_SITE_SERIES_MAX_LENGTH,
  BGC_PHASE_MAX_LENGTH,
  BGC_SUBZONE_MAX_LENGTH,
  BGC_VARIANT_MAX_LENGTH,
  BGC_ZONE_MAX_LENGTH,
} from '@/constants';

export type BgcParams = {
  bgcZone?: string;
  bgcSubZone?: string;
  bgcVariant?: string;
  bgcPhase?: string;
  becSiteSeries?: string;
  becSiteType?: string;
};

export type BgcSearchInputsProps = {
  values?: BgcParams;
  onFieldChange: (field: keyof BgcParams, value?: string) => void;
  wrapInRow?: boolean;
};

export const BgcSearchInputs = ({ values, onFieldChange, wrapInRow = false }: BgcSearchInputsProps) => {
  const bgcZoneInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(bgcZoneInputRef, values?.bgcZone);

  const bgcSubZoneInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(bgcSubZoneInputRef, values?.bgcSubZone);

  const bgcVariantInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(bgcVariantInputRef, values?.bgcVariant);

  const bgcPhaseInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(bgcPhaseInputRef, values?.bgcPhase);

  const becSiteSeriesInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(becSiteSeriesInputRef, values?.becSiteSeries);

  const becSitePhaseInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(becSitePhaseInputRef, values?.becSiteType);

  const content = (
    <>
      {/* BGC Zone */}
      <Column sm={4} md={4} lg={6} max={3}>
        <TextInput
          ref={bgcZoneInputRef}
          id="bgc-zone-input"
          name="bgc-zone"
          labelText="BGC zone"
          placeholder="Enter BGC zone"
          onInput={(e) => handleAutoUpperInput(e, BGC_ZONE_MAX_LENGTH)}
          onPaste={(e) => handleAutoUpperPaste(e, BGC_ZONE_MAX_LENGTH)}
          onBlur={(e) => onFieldChange('bgcZone', e.target.value ? e.target.value : undefined)}
        />
      </Column>

      {/* BGC Sub Zone */}
      <Column sm={4} md={4} lg={6} max={3}>
        <TextInput
          ref={bgcSubZoneInputRef}
          id="bgc-sub-zone-input"
          name="bgc-sub-zone"
          labelText="BGC sub zone"
          placeholder="Enter BGC sub zone"
          onBlur={(e) => onFieldChange('bgcSubZone', e.target.value ? e.target.value : undefined)}
          maxLength={BGC_SUBZONE_MAX_LENGTH}
        />
      </Column>

      {/* Variant */}
      <Column sm={4} md={4} lg={6} max={2}>
        <TextInput
          ref={bgcVariantInputRef}
          id="bgc-variant-input"
          name="bgc-variant"
          labelText="Variant"
          placeholder="Enter variant"
          onBlur={(e) => onFieldChange('bgcVariant', e.target.value ? e.target.value : undefined)}
          maxLength={BGC_VARIANT_MAX_LENGTH}
        />
      </Column>

      {/* Phase */}
      <Column sm={4} md={4} lg={6} max={2}>
        <TextInput
          ref={bgcPhaseInputRef}
          id="bgc-phase-input"
          name="bgc-phase"
          labelText="Phase"
          placeholder="Enter phase"
          onBlur={(e) => onFieldChange('bgcPhase', e.target.value ? e.target.value : undefined)}
          maxLength={BGC_PHASE_MAX_LENGTH}
        />
      </Column>

      {/* Site Series */}
      <Column sm={4} md={4} lg={6} max={3}>
        <TextInput
          ref={becSiteSeriesInputRef}
          id="bec-site-series-input"
          name="bec-site-series"
          labelText="Site series"
          placeholder="Enter site series"
          onBlur={(e) => onFieldChange('becSiteSeries', e.target.value ? e.target.value : undefined)}
          maxLength={BEC_SITE_SERIES_MAX_LENGTH}
        />
      </Column>

      {/* Site Phase */}
      <Column sm={4} md={4} lg={6} max={3}>
        <TextInput
          ref={becSitePhaseInputRef}
          id="bec-site-phase-input"
          name="bec-site-phase"
          labelText="Site phase"
          placeholder="Enter site phase"
          onBlur={(e) => onFieldChange('becSiteType', e.target.value ? e.target.value : undefined)}
          maxLength={BEC_SITE_PHASE_MAX_LENGTH}
        />
      </Column>
    </>
  );

  if (wrapInRow) {
    return (
      <Column sm={4} md={8} lg={16}>
        <Grid className="default-search-input-grid">
          {content}
        </Grid>
      </Column>
    );
  }

  return content;
};

export default BgcSearchInputs;
