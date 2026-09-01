import { Button, Stack } from "@carbon/react";
import { TenureRequestDto, TenureValidationResponseDto, TenureValidationResultDto } from "@/services/OpenApi";
import TenureItemInput, { TenureItemError } from "./TenureItemInput";
import TenureListItemSkeleton from "./TenureListItemSkeleton";
import { useEffect, useRef, useState } from "react";
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
const LOADING_ITEM_COUNT = 3;

type TenureListInputProps<T extends TenureRequestDto> = {
  tenures: T[];
  setTenures: (tenures: T[]) => unknown;
  validationResult?: TenureValidationResponseDto | null;
  onTenuresChange?: () => void;
  fieldErrors?: Array<{ fileId?: boolean; cutBlock?: boolean }>;
  loading?: boolean;
  initializeEmptyTenure?: boolean;
}

const emptyTenure: TenureRequestDto = {
  fileId: '',
  cuttingPermit: '',
  cutBlock: '',
  isPrimary: false,
};

const TenureListInput = <T extends TenureRequestDto>({
  tenures,
  setTenures,
  validationResult,
  onTenuresChange,
  fieldErrors,
  loading = false,
  initializeEmptyTenure = true,
}: TenureListInputProps<T>) => {
  const rowIdCounter = useRef(0);
  const [rowIds, setRowIds] = useState<string[]>(() =>
    Array.from({ length: Math.max(1, tenures.length) }, () => `tenure-${rowIdCounter.current++}`)
  );

  const ensureRowIds = (length: number) => {
    setRowIds((current) => {
      if (current.length === length) return current;
      const nextIds = current.slice(0, length);
      while (nextIds.length < length) {
        nextIds.push(`tenure-${rowIdCounter.current++}`);
      }
      return nextIds;
    });
  };

  useEffect(() => {
    if (loading || !initializeEmptyTenure) return;
    if (tenures.length === 0) setTenures([structuredClone(emptyTenure) as T]);
  }, [initializeEmptyTenure, loading]);

  useEffect(() => {
    ensureRowIds(Math.max(1, tenures.length));
  }, [tenures.length]);

  const addTenure = () => {
    setRowIds((current) => [...current, `tenure-${rowIdCounter.current++}`]);
    setTenures([...tenures, structuredClone(emptyTenure) as T]);
    onTenuresChange?.();
  };

  const updateTenure = (index: number, updated: TenureRequestDto) => {
    setTenures(tenures.map((tenure, itemIndex) =>
      itemIndex === index ? { ...tenure, ...updated } : tenure
    ));
    onTenuresChange?.();
  };

  const setPrimary = (index: number) => {
    setTenures(tenures.map((tenure, itemIndex) => ({ ...tenure, isPrimary: itemIndex === index })));
    onTenuresChange?.();
  };

  const deleteTenure = (index: number) => {
    setRowIds((current) => current.filter((_, itemIndex) => itemIndex !== index));
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
          subtitle:
            result.errorMessage ??
            'The combination of File ID, cutting permit, and cut block is incorrect. Please enter the details again.',
        };
      }
      if (EXISTING_OPENING_ERROR_CODES.has(code)) {
        return {
          kind: 'combo',
          subtitle: result.errorMessage ?? 'This tenure is already linked to an existing opening.',
        };
      }
      if (
        code === TenureValidationResultDto.errorCode.STALE_TENURE ||
        code === TenureValidationResultDto.errorCode.TENURE_NOT_ASSOCIATED
      ) {
        return {
          kind: 'combo',
          subtitle: result.errorMessage ?? 'This tenure changed before it could be saved. Reload and try again.',
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
      {loading ? (
        Array.from({ length: LOADING_ITEM_COUNT }, (_, index) => <TenureListItemSkeleton key={index} />)
      ) : (
        <>
          {tenures.map((tenure, index) => (
            <TenureItemInput
              key={rowIds[index] ?? `pending-tenure-${index}`}
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
        </>
      )}
    </Stack>
  );
};

export default TenureListInput;
