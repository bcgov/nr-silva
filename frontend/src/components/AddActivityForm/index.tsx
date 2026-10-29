
import { useState } from "react";
import {
  Column,
  Dropdown,
  DropdownSkeleton,
  Form,
  Grid,
  RadioButton,
  RadioButtonGroup,
  Stack,
} from "@carbon/react";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import { CodeDescriptionDto } from "@/services/OpenApi";
import RequiredLabel from "@/components/RequiredLabel";
import EmptySection from "@/components/EmptySection";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import { NON_BASIC_SILV_BASE_CODES } from "@/screens/CreateActivity/constants";

type AddActivityFormProps = {
  openingId?: string;
};

const AddActivityForm = ({ openingId: _openingId }: AddActivityFormProps) => {
  const [activityStatus, setActivityStatus] = useState<string>();
  const [selectedBase, setSelectedBase] = useState<CodeDescriptionDto | null>(null);

  const isNoneBasicCode = selectedBase
    ? NON_BASIC_SILV_BASE_CODES.has(selectedBase.code ?? "")
    : null;

  const silvBaseCodeQuery = useQuery({
    queryKey: ["codes", "silv-base"],
    queryFn: API.CodesEndpointService.getSilvBaseCodes,
    select: (data) => data.filter((base) => base.code !== "DN"),
  });

  const silvTechniqueCodeQuery = useQuery({
    queryKey: ["codes", "silv-technique"],
    queryFn: API.CodesEndpointService.getSilvTechniqueCodes,
  });

  const silvMethodCodeQuery = useQuery({
    queryKey: ["codes", "silv-method"],
    queryFn: API.CodesEndpointService.getSilvMethodCodes,
  });

  const silvObjectiveCodeQuery = useQuery({
    queryKey: ["codes", "silv-objective"],
    queryFn: API.CodesEndpointService.getSilvObjectiveCodes,
  });

  const silvFundSourceCodeQuery = useQuery({
    queryKey: ["codes", "silv-fund-source"],
    queryFn: API.CodesEndpointService.getSilvFundSourceCodes,
  });

  return (
    <Grid className="add-activity-form-grid">
      {/* Activity status */}
      <Column sm={4} md={8} lg={16}>
        <Stack orientation="vertical" gap={6}>
          <h3 className="default-heading-20px">Activity status</h3>
          <RadioButtonGroup
            id="activity-status-input"
            legendText={
              <RequiredLabel id="activity-status-label" htmlFor="activity-status-input">
                Select activity status
              </RequiredLabel>
            }
            name="activity-status-radio-group"
            valueSelected={activityStatus}
            onChange={(selection) => setActivityStatus(String(selection))}
          >
            <RadioButton
              id="activity-status-planned-radio"
              labelText="Planned"
              value="planned"
            />
            <RadioButton
              id="activity-status-completed-radio"
              labelText="Completed"
              value="completed"
            />
          </RadioButtonGroup>
        </Stack>
      </Column>

      {activityStatus ? (
        <>
          {/* Activity base */}
          <Column sm={4} md={8} lg={16} max={8}>
            <Stack orientation="vertical" gap={6}>
              <h3 className="default-heading-20px">Activity base</h3>
              {silvBaseCodeQuery.isLoading ? (
                <DropdownSkeleton />
              ) : (
                <Dropdown
                  id="silv-base-code-dropdown"
                  titleText={
                    <RequiredLabel id="silv-base-code-label" htmlFor="silv-base-code-dropdown">
                      Activity base
                    </RequiredLabel>
                  }
                  label="Choose an option"
                  items={silvBaseCodeQuery.data ?? []}
                  itemToString={(item) => codeDescriptionToDisplayText(item)}
                  selectedItem={selectedBase}
                  onChange={({ selectedItem }) => setSelectedBase(selectedItem ?? null)}
                />
              )}
            </Stack>
          </Column>

          {/* Base form */}
          {selectedBase !== null ? (
            <Column sm={4} md={8} lg={16}>
              {isNoneBasicCode ? (
                <EmptySection
                  title="This base code is not yet supported"
                  description="Please choose a different base code."
                  icon="Construction"
                />
              ) : (
                // Activity information
                <Form></Form>
              )}
            </Column>
          ) : null}
        </>
      ) : null}
    </Grid>
  );
};

export default AddActivityForm;

