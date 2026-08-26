import { Button, Checkbox, Column, Grid, InlineNotification, Stack, TextInput } from "@carbon/react";
import { TenureRequestDto } from "@/services/OpenApi";
import RequiredLabel from "../RequiredLabel";
import { TrashCan } from "@carbon/icons-react";
import { handleAutoUpperInput, handleAutoUpperPaste } from "@/utils/InputUtils";

import './styles.scss';

const FILE_ID_MAX = 10;
const CUTTING_PERMIT_MAX = 3;
const CUT_BLOCK_MAX = 10;

export type TenureItemError = {
  kind: 'combo' | 'duplicate';
  subtitle: string;
};

type props = {
  index: number;
  tenure: TenureRequestDto;
  setTenure: (tenure: TenureRequestDto) => unknown;
  onSetPrimary: () => unknown;
  deleteDisabled?: boolean;
  deleteTenure: () => unknown;
  fieldErrors?: { fileId?: boolean; cutBlock?: boolean };
  itemError?: TenureItemError;
}

const TenureItemInput = ({ index, tenure, setTenure, onSetPrimary, deleteDisabled, deleteTenure, fieldErrors, itemError }: props) => {
  return (
    <Stack gap={6} className="tenure-item-input-container">
      {tenure.isPrimary ? <h4>Primary tenure</h4> : null}
      <Grid className="tenure-item-grid">
        <Column sm={4} md={4} lg={8} className="tenure-column">
          <TextInput
            id={`file-id-input-${index}`}
            name="fileId"
            className="default-white-input"
            value={tenure.fileId ?? ''}
            invalid={fieldErrors?.fileId}
            invalidText="File ID is required."
            onChange={(e) => setTenure({ ...tenure, fileId: e.target.value.trim() })}
            onInput={(e) => handleAutoUpperInput(e, FILE_ID_MAX)}
            onPaste={(e) => {
              handleAutoUpperPaste(e, FILE_ID_MAX);
              setTenure({ ...tenure, fileId: e.currentTarget.value.trim() });
            }}
            labelText={
              <RequiredLabel id={`file-id-label-${index}`} htmlFor={`file-id-input-${index}`}>
                File ID
              </RequiredLabel>
            }
          />
        </Column>

        <Column sm={4} md={4} lg={8} className="tenure-column">
          <TextInput
            id={`cutting-permit-input-${index}`}
            name="cuttingPermit"
            className="default-white-input"
            value={tenure.cuttingPermit ?? ''}
            onChange={(e) => setTenure({ ...tenure, cuttingPermit: e.target.value.trim() })}
            onInput={(e) => handleAutoUpperInput(e, CUTTING_PERMIT_MAX)}
            onPaste={(e) => {
              handleAutoUpperPaste(e, CUTTING_PERMIT_MAX);
              setTenure({ ...tenure, cuttingPermit: e.currentTarget.value.trim() });
            }}
            labelText="Cutting permit"
          />
        </Column>

        <Column sm={4} md={4} lg={8} className="tenure-column">
          <TextInput
            id={`cut-block-input-${index}`}
            name="cutBlock"
            className="default-white-input"
            value={tenure.cutBlock ?? ''}
            invalid={fieldErrors?.cutBlock}
            invalidText="Cut block is required."
            onChange={(e) => setTenure({ ...tenure, cutBlock: e.target.value.trim() })}
            onInput={(e) => handleAutoUpperInput(e, CUT_BLOCK_MAX)}
            onPaste={(e) => {
              handleAutoUpperPaste(e, CUT_BLOCK_MAX);
              setTenure({ ...tenure, cutBlock: e.currentTarget.value.trim() });
            }}
            labelText={
              <RequiredLabel id={`cut-block-label-${index}`} htmlFor={`cut-block-input-${index}`}>
                Cut block
              </RequiredLabel>
            }
          />
        </Column>
      </Grid>

      <Checkbox
        id={`isPrimary-${index}`}
        labelText="Set as primary tenure"
        checked={tenure.isPrimary ?? false}
        onChange={() => onSetPrimary()}
      />

      {itemError ? (
        <InlineNotification
          kind="error"
          title={itemError.kind === 'combo' ? 'Incorrect information:' : 'Duplicate tenure'}
          subtitle={itemError.subtitle}
          lowContrast
          hideCloseButton
        />
      ) : null}

      {deleteDisabled ? null : (
        <Button kind="danger--tertiary" onClick={deleteTenure} renderIcon={TrashCan}>Remove</Button>
      )}
    </Stack>
  );
};

export default TenureItemInput;
