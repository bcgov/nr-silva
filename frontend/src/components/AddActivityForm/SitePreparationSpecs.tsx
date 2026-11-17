import { ActivityFormType, SpecModule, SpecFieldsProps } from "@/screens/CreateActivity/definitions";
import WholeNumberInput, { isValidWholeNumber } from "./WholeNumberInput";

const MAX = 9999;

const Fields = ({ form, setForm }: SpecFieldsProps) => (
  <WholeNumberInput
    field="targetPreparedSpots"
    label="Target prepared spot"
    placeholder="Enter target prepared spot"
    max={MAX}
    form={form}
    setForm={setForm}
  />
);

const SitePreparationSpecs: SpecModule = {
  Fields,
  validate: (form: ActivityFormType) => {
    const value = form.targetPreparedSpots?.value;
    const valid = !value || isValidWholeNumber(value, MAX);
    if (!valid && form.targetPreparedSpots) form.targetPreparedSpots.isInvalid = true;
    return valid;
  },
  toPayload: (form) =>
    form.targetPreparedSpots?.value ? { targetPreparedSpots: Number(form.targetPreparedSpots.value) } : {},
  hasData: (form) => Boolean(form.targetPreparedSpots?.value),
};

export default SitePreparationSpecs;
