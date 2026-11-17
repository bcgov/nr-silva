import { ActivityFormType, SpecModule, SpecFieldsProps } from "@/screens/CreateActivity/definitions";
import WholeNumberInput, { isValidWholeNumber } from "./WholeNumberInput";

const MAX_PLOTS = 9999;
const MAX_PLOTS_PER_STRATUM = 9999999999;

const Fields = ({ form, setForm }: SpecFieldsProps) => (
  <>
    <WholeNumberInput
      field="surveyPlannedNumPlots"
      label="Planned number of plots"
      placeholder="Enter planned number of plots"
      max={MAX_PLOTS}
      form={form}
      setForm={setForm}
    />
    {form.status?.value === "COMPLETED" ? (
      <WholeNumberInput
        field="surveyActualNumPlots"
        label="Actual number of plots"
        placeholder="Enter actual number of plots"
        max={MAX_PLOTS}
        form={form}
        setForm={setForm}
      />
    ) : null}
    <WholeNumberInput
      field="surveyMinPlotsPerStratum"
      label="Minimum plots per stratum"
      placeholder="Enter minimum plots per stratum"
      max={MAX_PLOTS_PER_STRATUM}
      form={form}
      setForm={setForm}
    />
  </>
);

const isCompleted = (form: ActivityFormType) => form.status?.value === "COMPLETED";

const SurveySpecs: SpecModule = {
  Fields,
  validate: (form) => {
    const checks = [
      { input: form.surveyPlannedNumPlots, max: MAX_PLOTS },
      { input: form.surveyMinPlotsPerStratum, max: MAX_PLOTS_PER_STRATUM },
      ...(isCompleted(form) ? [{ input: form.surveyActualNumPlots, max: MAX_PLOTS }] : []),
    ];
    let valid = true;
    checks.forEach(({ input, max }) => {
      if (input?.value && !isValidWholeNumber(input.value, max)) {
        input.isInvalid = true;
        valid = false;
      }
    });
    return valid;
  },
  // Actual plots are rejected by the backend for planned activities, so only send when completed.
  toPayload: (form) => ({
    surveyPlannedNumPlots: form.surveyPlannedNumPlots?.value ? Number(form.surveyPlannedNumPlots.value) : undefined,
    surveyMinPlotsPerStratum: form.surveyMinPlotsPerStratum?.value ? Number(form.surveyMinPlotsPerStratum.value) : undefined,
    surveyActualNumPlots:
      isCompleted(form) && form.surveyActualNumPlots?.value ? Number(form.surveyActualNumPlots.value) : undefined,
  }),
  hasData: (form) =>
    Boolean(
      form.surveyPlannedNumPlots?.value || form.surveyActualNumPlots?.value || form.surveyMinPlotsPerStratum?.value,
    ),
};

export default SurveySpecs;
