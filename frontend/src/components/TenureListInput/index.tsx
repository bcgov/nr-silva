import { Button, Stack } from "@carbon/react";
import { TenureRequestDto, TenureValidationResponseDto, TenureValidationResultDto } from "@/services/OpenApi";
import TenureItemInput, { TenureItemError } from "./TenureItemInput";
import { useEffect } from "react";
import { Add } from "@carbon/icons-react";
import './styles.scss';

const COMBO_ERROR_CODES = new Set([
  TenureValidationResultDto.errorCode.FIELD_INVALID,
  TenureValidationResultDto.errorCode.TENURE_NOT_FOUND,
  TenureValidationResultDto.errorCode.CLIENT_NOT_LICENSEE,
]);

const EXISTING_OPENING_ERROR_CODES = new Set([
  TenureValidationResultDto.errorCode.TENURE_DUPLICATE_OPENING,
]);

type props = {
  tenures: TenureRequestDto[];
  setTenures: (tenures: TenureRequestDto[]) => unknown;
  validationResult?: TenureValidationResponseDto | null;
  onTenuresChange?: () => void;
  fieldErrors?: Array<{ fileId?: boolean; cutBlock?: boolean }>;
}

const emptyTenure: TenureRequestDto = {
  fileId: '',
  cuttingPermit: '',
  cutBlock: '',
  isPrimary: false,
};

const TenureListInput = ({ tenures, setTenures, validationResult, onTenuresChange, fieldErrors }: props) => {
  useEffect(() => {
    if (tenures.length === 0) setTenures([structuredClone(emptyTenure)]);
  }, []);

  const addTenure = () => setTenures([...tenures, structuredClone(emptyTenure)]);

  const updateTenure = (index: number, updated: TenureRequestDto) => {
    setTenures(tenures.map((t, i) => (i === index ? updated : t)));
    onTenuresChange?.();
  };

  const setPrimary = (index: number) =>
    setTenures(tenures.map((t, i) => ({ ...t, isPrimary: i === index })));

  const deleteTenure = (index: number) => {
    setTenures(tenures.filter((_, i) => i !== index));
    onTenuresChange?.();
  };

  const getItemError = (index: number): TenureItemError | undefined => {
    if (!validationResult) return undefined;
    const result = validationResult.validationResults?.[index];
    if (result && !result.isValid && result.errorCode) {
      const code = result.errorCode as TenureValidationResultDto.errorCode;
      if (COMBO_ERROR_CODES.has(code)) {
        return {
          kind: 'combo',
          subtitle: 'The combination of File ID, cutting permit, and cut block is incorrect. Please enter the details again.',
        };
      }
      if (EXISTING_OPENING_ERROR_CODES.has(code)) {
        return {
          kind: 'combo',
          subtitle: 'This tenure is already linked to an existing opening.',
        };
      }
    }
    const dupGroup = validationResult.duplicateConflicts?.find(g => g.duplicateIndices?.includes(index));
    if (dupGroup) {
      const others = (dupGroup.duplicateIndices ?? [])
        .filter(i => i !== index)
        .map(i => `#${i + 1}`)
        .join(', ');
      return { kind: 'duplicate', subtitle: `This tenure is a duplicate of tenure ${others}.` };
    }
    // DUPLICATE_IN_REQUEST fallback when duplicateConflicts is missing
    if (result && !result.isValid && result.errorCode === TenureValidationResultDto.errorCode.DUPLICATE_IN_REQUEST) {
      return { kind: 'duplicate', subtitle: 'This tenure is a duplicate of another tenure in this request.' };
    }
    return undefined;
  };

  return (
    <Stack gap={6} className="tenure-list-stack">
      {tenures.map((tenure, index) => (
        <TenureItemInput
          key={index}
          index={index}
          tenure={tenure}
          setTenure={(updated) => updateTenure(index, updated)}
          onSetPrimary={() => setPrimary(index)}
          deleteDisabled={tenures.length === 1}
          deleteTenure={() => deleteTenure(index)}
          fieldErrors={fieldErrors?.[index]}
          itemError={getItemError(index)}
        />
      ))}
      <Button kind="tertiary" onClick={addTenure} renderIcon={Add}>Add multi-tenure</Button>
    </Stack>
  );
};

export default TenureListInput;
