import { useState } from "react";
import MapPreview from "@/components/MapPreview";
import {
  Button, Column, ComboBox, FileUploaderItem,
  Grid, Modal, RadioButton, Stack, Table, TableBody,
  TableCell, TableContainer, TableHead, TableHeader,
  TableRow, TableToolbar, TextInput,
  Toggle
} from "@carbon/react";
import { CreateOpeningFormType, TenureInfoDto } from "@/screens/CreateOpening/definitions";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";

import './styles.scss';
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import RequiredLabel from "../../RequiredLabel";
import { cutBlockId, cuttingPermitId, forestFileId, getNewTenureForm, TenureHeaderConfig } from "./constants";
import { Add } from "@carbon/icons-react";
import ModalHead from "../../Modals/ModalHead";

import FormInputType from "@/types/FormInputType";

type DataFormProps = {
  isReview: boolean;
  form: CreateOpeningFormType;
  setForm: React.Dispatch<React.SetStateAction<CreateOpeningFormType>>;
}

const DataForm = ({ isReview, form }: DataFormProps) => {
  const [isTenureModalOpen, setIsTenureModalOpen] = useState<boolean>(false);
  const [tenureForm, setTenureForm] = useState<TenureInfoDto>(() => getNewTenureForm());

  const categoryQuery = useQuery({
    queryKey: ["codes", "opening-categories", "active"],
    queryFn: () => API.CodesEndpointService.getOpeningCategories(false),
  });

  const orgUnitQuery = useQuery({
    queryKey: ["codes", "org-units"],
    queryFn: API.CodesEndpointService.getOpeningOrgUnits,
  });

  const renderCellContent = (
    tenure: TenureInfoDto,
    colKey: keyof TenureInfoDto | "action"
  ) => {
    if (colKey === 'isPrimary') {
      return <RadioButton labelText="" checked={tenure.isPrimary} />
    }
    if (colKey === 'action') {
      return (
        <Stack orientation="horizontal" className="default-equal-split-stack">

        </Stack>
      )
    }

    return String((tenure[colKey] as FormInputType<string>).value)
  }



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
          titleText={<RequiredLabel htmlFor={form.orgUnit?.id!}>Org unit</RequiredLabel>}
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
          titleText={<RequiredLabel htmlFor={form.category?.id!}>Opening category</RequiredLabel>}
          onChange={() => { }}
          onInputChange={() => { }}
          itemToString={codeDescriptionToDisplayText}
        />
      </Column>

      <Column sm={4} md={8} lg={16}>
        <h3 className="default-heading-20px">General information</h3>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <Stack orientation="horizontal" gap={2} className="default-equal-split-stack">
          <TextInput
            type="number"
            className="default-number-input"
            labelText={<RequiredLabel htmlFor={form.openingGrossArea?.id!}>Opening gross area (ha)</RequiredLabel>}
            id={form.openingGrossArea?.id!}
            helperText="Operating area including all SUs"
          />

          <TextInput
            type="number"
            className="default-number-input"
            labelText={<RequiredLabel htmlFor={form.maxAllowablePermAccess?.id!}>Maximum allowable permanent access (%)</RequiredLabel>}
            id={form.maxAllowablePermAccess?.id!}
            helperText="Gross percentage area a permanent access structure can occupy in an opening"
          />
        </Stack>
      </Column>


      <Column sm={4} md={8} lg={16}>
        <h3 className="default-heading-20px">Tenure information</h3>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <TableContainer className="default-table-container tenure-table-container">
          <TableToolbar>
            <div className="table-toolbar-container">
              <p>Total licenses: {form.tenureInfo?.value?.length ?? 0}</p>
              <Button
                iconDescription="Document Download"
                kind="ghost"
                renderIcon={Add}
                onClick={() => setIsTenureModalOpen(true)}
                size="lg"
              >
                Add licence
              </Button>

            </div>
          </TableToolbar>

          <Table
            className="species-table-container default-zebra-table"
            aria-label="Species and layer table"
            useZebraStyles
          >
            <TableHead>
              <TableRow>
                {
                  TenureHeaderConfig
                    .map((header) => (
                      <TableHeader key={header.key}>
                        {header.header}
                      </TableHeader>
                    ))
                }
              </TableRow>
            </TableHead>
            <TableBody>
              {form.tenureInfo?.value?.map((row, index) => (
                <TableRow key={`tenure-info-table-row-${index}-${row.forestFileId}`}>
                  {
                    TenureHeaderConfig.map((header) => {
                      return (
                        <TableCell className="tenure-table-cell" key={header.key}>
                          {renderCellContent(row, header.key)}
                        </TableCell>
                      )
                    })
                  }
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Column>

      <Modal
        passiveModal
        open={isTenureModalOpen}
        modalHeading={<ModalHead title="Add licence" />}
        onRequestClose={() => setIsTenureModalOpen(false)}
        className="add-tenure-licence-modal"
        preventCloseOnClickOutside
        size="sm"
      >
        <Grid className="tenure-licence-grid">
          <Column sm={4} md={8} lg={16}>
            <TextInput
              labelText={<RequiredLabel htmlFor={forestFileId}>Forest file ID</RequiredLabel>}
              id={tenureForm.forestFileId.id!}
              helperText="Apply the unique ID to identify the opening or cross reference program records. It will be set as the primary licence"
              placeholder="Ex. A20913"
            />
          </Column>

          <Column sm={4} md={8} lg={16}>
            <TextInput
              labelText={<RequiredLabel htmlFor={cutBlockId}>Cut block</RequiredLabel>}
              id={tenureForm.cutBlock.id!}
              helperText="The approved cut block identifier for the opening"
              placeholder="Ex. 20-98"
            />
          </Column>

          <Column sm={4} md={8} lg={16}>
            <TextInput
              labelText={<RequiredLabel htmlFor={cuttingPermitId}>Cutting permit</RequiredLabel>}
              id={tenureForm.cuttingPermit.id!}
              helperText="Corresponds to licence in cutting permit document"
              placeholder="Ex. 16"
            />
          </Column>

          <Column sm={4} md={8} lg={16}>
            <Toggle
              id="tenure-primary-toggle"
              hideLabel
              labelText="Set as primary"
            />
          </Column>
        </Grid>
      </Modal>
    </>
  )

}

export default DataForm;
