import { Column, InlineNotification } from "@carbon/react";
import { CreateOpeningFormType } from "@/screens/CreateOpening/definitions";
import { TenureValidationResponseDto } from "@/services/OpenApi";
import TenureListInput from "@/components/TenureListInput";

import './styles.scss';

type StepTwoProps = {
  form: CreateOpeningFormType;
  setForm: React.Dispatch<React.SetStateAction<CreateOpeningFormType>>;
  validationResult?: TenureValidationResponseDto | null;
  showNoPrimaryError?: boolean;
  fieldErrors?: Array<{ fileId?: boolean; cutBlock?: boolean }>;
  onTenuresChange?: () => void;
}

const StepTwo = ({ form, setForm, validationResult, showNoPrimaryError, fieldErrors, onTenuresChange }: StepTwoProps) => {
  return (
    <>
      <Column sm={4} md={8} lg={16}>
        <h2 className="default-heading-28px">Tenure information</h2>
      </Column>

      {showNoPrimaryError ? (
        <Column sm={4} md={8} lg={16}>
          <InlineNotification
            kind="error"
            title="Primary tenure required"
            subtitle="At least one tenure must be set as primary."
            lowContrast
            hideCloseButton
          />
        </Column>
      ) : null}

      <Column sm={4} md={8} lg={16}>
        <TenureListInput
          tenures={form.tenureInfo?.value || []}
          setTenures={(tenures) => setForm(f => ({ ...f, tenureInfo: { ...f.tenureInfo, value: tenures } }))}
          validationResult={validationResult}
          onTenuresChange={onTenuresChange}
          fieldErrors={fieldErrors}
        />
      </Column>
    </>
  );
}

export default StepTwo;
