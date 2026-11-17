import { Column, Grid } from "@carbon/react";
import { SpecFieldsProps, SpecModule } from "@/screens/CreateActivity/definitions";
import SitePreparationSpecs from "./SitePreparationSpecs";
import SurveySpecs from "./SurveySpecs";

/** Base code -> module owning that base's fields, validation and payload. Add new bases here. */
const SPEC_MODULES: Record<string, SpecModule> = {
  SP: SitePreparationSpecs,
  SU: SurveySpecs,
};

export const getSpecModule = (baseCode?: string): SpecModule | undefined =>
  baseCode ? SPEC_MODULES[baseCode] : undefined;

const ActivitySpecifications = ({ baseCode, form, setForm }: SpecFieldsProps & { baseCode: string }) => {
  const Fields = getSpecModule(baseCode)?.Fields;
  if (!Fields) return null;

  return (
    <Grid className="activity-information-grid">
      <Column sm={4} md={8} lg={16}>
        <h3 className="default-heading-20px">Activity specifications</h3>
      </Column>
      <Fields form={form} setForm={setForm} />
    </Grid>
  );
};

export default ActivitySpecifications;
