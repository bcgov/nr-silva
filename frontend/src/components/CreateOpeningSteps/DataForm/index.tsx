import MapPreview from "@/components/MapPreview";
import { Column, ComboBox, FileUploaderItem, Stack, TextInput } from "@carbon/react";
import { CreateOpeningFormType } from "@/screens/CreateOpening/definitions";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";

import './styles.scss';
import { codeDescriptionToDisplayText } from "../../../utils/multiSelectUtils";
import RequiredLabel from "../../RequiredLabel";

type DataFormProps = {
  isReview: boolean;
  form: CreateOpeningFormType;
  setForm: React.Dispatch<React.SetStateAction<CreateOpeningFormType>>;
}

const DataForm = ({ isReview, form }: DataFormProps) => {

  const categoryQuery = useQuery({
    queryKey: ["codes", "opening-categories", "active"],
    queryFn: () => API.CodesEndpointService.getOpeningCategories(false),
  });

  const orgUnitQuery = useQuery({
    queryKey: ["codes", "org-units"],
    queryFn: API.CodesEndpointService.getOpeningOrgUnits,
  });

  return (
    <>
      <Column sm={4} md={8} lg={16} className="opening-detail-title">
        <h2 className="default-heading-28px">Opening Details</h2>
        <p>Refine the opening's information. Review extracted details or complete/edit the manual entry form to ensure accuracy.</p>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <h3 className="default-heading-20px">Preview</h3>
      </Column>

      <Column sm={4} md={8} lg={16}>
        {
          form.geojson
            ? (
              <Stack className="spatial-file-stack">
                <MapPreview geojson={form.geojson.value} />
                <FileUploaderItem
                  className="default-file-uploader-item"
                  name={form.file?.value?.name ?? "Invalid file"}
                  size="lg"
                  status="complete"
                />
              </Stack>
            )
            : null
        }
      </Column>

      <Column sm={4} md={8} lg={16}>
        <h3 className="default-heading-20px">Opening information</h3>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <ComboBox
          id={form.orgUnit?.id!}
          helperText="Select which organization you're representing"
          items={orgUnitQuery.data ?? []}
          titleText={<RequiredLabel>Org unit</RequiredLabel>}
          onChange={() => { }}
          onInputChange={() => { }}
          itemToString={codeDescriptionToDisplayText}
        />
      </Column>

      <Column sm={4} md={8} lg={16}>
        <ComboBox
          id={form.category?.id!}
          helperText="Use the code to identify specific program area and silviculture responsibility"
          items={categoryQuery.data ?? []}
          titleText={<RequiredLabel>Opening category</RequiredLabel>}
          onChange={() => { }}
          onInputChange={() => { }}
          itemToString={codeDescriptionToDisplayText}
        />
      </Column>

      <Column sm={4} md={8} lg={16}>
        <h3 className="default-heading-20px">General information</h3>
      </Column>

      <Column sm={4} md={8} xlg={6} max={5}>
        <TextInput
          type="number"
          className="default-number-input"
          labelText={<RequiredLabel>Opening gross area (ha)</RequiredLabel>}
          id={form.openingGrossArea?.id!}
          helperText="Operating area including all SUs"
        />
      </Column>

      <Column sm={4} md={8} xlg={6} max={5}>
        <TextInput
          type="number"
          className="default-number-input"
          labelText={<RequiredLabel>Maximum allowable permanent access (%)</RequiredLabel>}
          id={form.maxAllowablePermAccess?.id!}
          helperText="Gross percentage area a permanent access structure can occupy in an opening"
        />
      </Column>


      <Column sm={4} md={8} lg={16}>
        <h3 className="default-heading-20px">Tenure information</h3>
      </Column>

      <Column sm={4} md={8} lg={16}>

      </Column>
    </>
  )

}

export default DataForm;
