import { Column, TextInput } from "@carbon/react";
import { enforceDecimalInputOnKeyDown, enforceDecimalInputOnPaste } from "@/utils/InputUtils";
import { ActivityFormType } from "@/screens/CreateActivity/definitions";

export const isValidWholeNumber = (value: string, max: number) =>
  /^\d+$/.test(value) && Number(value) <= max;

type WholeNumberKey =
  | "targetPreparedSpots"
  | "surveyPlannedNumPlots"
  | "surveyActualNumPlots"
  | "surveyMinPlotsPerStratum";

type WholeNumberInputProps = {
  field: WholeNumberKey;
  label: string;
  placeholder: string;
  /** Inclusive upper bound; the backend allows whole numbers from 0 to max. */
  max: number;
  form: ActivityFormType;
  setForm: React.Dispatch<React.SetStateAction<ActivityFormType>>;
};

const WholeNumberInput = ({ field, label, placeholder, max, form, setForm }: WholeNumberInputProps) => {
  const input = form[field];
  const maxDigits = String(max).length;

  return (
    <Column sm={4} md={4} lg={8}>
      <TextInput
        id={input?.id ?? field}
        labelText={label}
        placeholder={placeholder}
        defaultValue={input?.value ?? ""}
        invalid={input?.isInvalid}
        invalidText={`Must be a whole number between 0 and ${max}`}
        onKeyDown={(e) => enforceDecimalInputOnKeyDown(e, maxDigits, 0)}
        onPaste={(e) => enforceDecimalInputOnPaste(e.currentTarget, e, maxDigits, 0)}
        onBlur={(e) => {
          const value = e.target.value;
          setForm((prev) => ({
            ...prev,
            [field]: {
              ...prev[field],
              value: value || undefined,
              isInvalid: !!value && !isValidWholeNumber(value, max),
            },
          }));
        }}
      />
    </Column>
  );
};

export default WholeNumberInput;
