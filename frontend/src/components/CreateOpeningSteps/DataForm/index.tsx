import { useState } from "react";
import MapPreview from "@/components/MapPreview";
import {
  Button, Column, ComboBox, FileUploaderItem,
  Grid, InlineNotification, Modal, RadioButton, Stack, Table, TableBody,
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
import { Add, Edit, Save, TrashCan } from "@carbon/icons-react";
import ModalHead from "../../Modals/ModalHead";

import FormInputType from "@/types/FormInputType";
import { PLACE_HOLDER } from "@/constants";
import EmptySection from "../../EmptySection";

type DataFormProps = {
  isReview: boolean;
  form: CreateOpeningFormType;
  setForm: React.Dispatch<React.SetStateAction<CreateOpeningFormType>>;
}

const DataForm = ({ isReview, form, setForm }: DataFormProps) => {
  const [isEditTenure, setIsEditTenure] = useState<boolean>(false);
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

  const handlePrimaryChange = (displayId: string) => {
    setForm(prev => ({
      ...prev,
      tenureInfo: {
        ...prev.tenureInfo,
        value: prev.tenureInfo?.value?.map(t =>
          t.displayId === displayId
            ? { ...t, isPrimary: true }
            : { ...t, isPrimary: false }
        ) ?? []
      }
    }));
  };

  const editTenure = (displayId: string) => {
    const tenureToEdit = form.tenureInfo?.value?.find(t => t.displayId === displayId);
    if (!tenureToEdit) return;

    setIsEditTenure(true);
    setTenureForm(tenureToEdit)
    setIsTenureModalOpen(true);
  }

  const deleteTenure = (displayId: string) => {
    setForm(prev => {
      const updatedTenures = prev.tenureInfo?.value?.filter(t => t.displayId !== displayId) ?? [];
      const deletedWasPrimary = prev.tenureInfo?.value?.find(t => t.displayId === displayId)?.isPrimary;

      const normalized: TenureInfoDto[] = deletedWasPrimary
        ? updatedTenures.map((t, i) => ({ ...t, isPrimary: i === 0 })) // first becomes primary, others false
        : updatedTenures;

      return {
        ...prev,
        tenureInfo: {
          ...prev.tenureInfo,
          value: normalized
        }
      };
    });
  }

  const renderCellContent = (
    tenure: TenureInfoDto,
    colKey: keyof TenureInfoDto | "action"
  ) => {
    if (colKey === 'isPrimary') {
      return (
        <div className="primary-ind-cell">
          <RadioButton
            disabled={isReview}
            labelText=""
            checked={tenure.isPrimary}
            onChange={() => handlePrimaryChange(tenure.displayId)}
          />
        </div>
      );
    }
    if (colKey === 'action') {
      return (
        <Stack orientation="horizontal" className="default-equal-split-stack">
          <Button
            className="edit-tenure-button"
            iconDescription="Edit tenure"
            tooltipAlignment="end"
            kind="ghost"
            hasIconOnly
            renderIcon={Edit}
            onClick={() => editTenure(tenure.displayId)}
            size="lg"
          />

          <Button
            className="delete-tenure-button"
            iconDescription="Delete tenure"
            tooltipAlignment="end"
            kind="danger--ghost"
            hasIconOnly
            renderIcon={TrashCan}
            onClick={() => deleteTenure(tenure.displayId)}
            size="lg"
          />
        </Stack>
      )
    }

    return String((tenure[colKey] as FormInputType<string>).value ?? PLACE_HOLDER);
  };

  const handleTenureModalClose = () => {
    setTenureForm(getNewTenureForm());
    setIsEditTenure(false);
    setIsTenureModalOpen(false);
  }

  const normalizeTenures = (
    tenures: TenureInfoDto[],
    edited: TenureInfoDto,
    isEdit: boolean
  ): TenureInfoDto[] => {
    if (!tenures.length) return tenures;

    if (edited.isPrimary) {
      // edited set to primary, all others false
      return tenures.map(t => ({ ...t, isPrimary: t.displayId === edited.displayId }));
    } else if (isEdit) {
      // edited switched from primary to not primary
      const wasPrimaryBefore = form.tenureInfo?.value?.find(
        t => t.displayId === edited.displayId
      )?.isPrimary;

      if (wasPrimaryBefore) {
        const hasOtherPrimary = tenures.some(t => t.isPrimary);

        if (!hasOtherPrimary) {
          // keep it primary if it was the only one
          return tenures.map(t =>
            t.displayId === edited.displayId ? { ...t, isPrimary: true } : t
          );
        }

        // otherwise assign first as primary
        return tenures.map((t, idx) => ({ ...t, isPrimary: idx === 0 }));
      }
    }

    return tenures;
  };

  /**
   * Attempts to add a new tenure entry to the form state.
   *
   * - Validates that required fields (forestFileId, cutBlock, cuttingPermit) are provided.
   *   If any are missing, marks those fields as invalid and prevents addition.
   * - If valid:
   *   - If there are no existing tenures, marks the new tenure as primary.
   *   - If the new tenure is marked as primary, clears the primary flag from all existing tenures.
   *   - Appends the tenure to the tenureInfo array in the form state.
   * - Resets and closes the tenure modal on success, otherwise updates local state with validation errors.
   */
  const addTenure = () => {
    let isTenureValid = true;
    const tempTenure = structuredClone(tenureForm);

    if (!tempTenure.forestFileId.value) {
      isTenureValid = false;
      tempTenure.forestFileId.isInvalid = true;
    }
    if (!tempTenure.cutBlock.value) {
      isTenureValid = false;
      tempTenure.cutBlock.isInvalid = true;
    }
    if (!tempTenure.cuttingPermit.value) {
      isTenureValid = false;
      tempTenure.cuttingPermit.isInvalid = true;
    }

    if (isTenureValid) {
      setForm(prev => {
        let updatedTenures = [...(prev.tenureInfo?.value ?? [])];

        if (updatedTenures.length === 0) {
          tempTenure.isPrimary = true;
        }

        updatedTenures = [...updatedTenures, tempTenure];
        updatedTenures = normalizeTenures(updatedTenures, tempTenure, false);

        return {
          ...prev,
          tenureInfo: {
            ...prev.tenureInfo,
            value: updatedTenures,
            isInvalid: false
          },
        };
      });
      handleTenureModalClose();
    } else {
      setTenureForm(tempTenure);
    }
  }
  const saveEditedTenure = () => {
    const tempTenure = structuredClone(tenureForm);
    setForm(prev => {
      let updatedTenures =
        prev.tenureInfo?.value?.map(t =>
          t.displayId === tempTenure.displayId ? tempTenure : t
        ) ?? [];

      updatedTenures = normalizeTenures(updatedTenures, tempTenure, true);

      return {
        ...prev,
        tenureInfo: {
          ...prev.tenureInfo,
          value: updatedTenures,
          isInvalid: false
        },
      };
    });
    handleTenureModalClose();
  };

  return (
    <>
      <Column sm={4} md={8} lg={16} className="opening-detail-title">
        <h2 className="default-heading-28px">
          {isReview ? 'Review & Create' : 'Opening Details'}
        </h2>
        <p>Please confirm all the information below is correct before creating the opening or make any changes by using the "Edit" button.</p>
      </Column>

      <Column sm={4} md={8} lg={16} className={isReview ? 'review-form-wrapper' : undefined}>
        <Grid className="create-opening-subgrid">
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
              onChange={(data) => setForm((prev) => ({ ...prev, orgUnit: { ...prev.orgUnit, value: data.selectedItem ?? undefined, isInvalid: false } }))}
              itemToString={codeDescriptionToDisplayText}
              readOnly={isReview}
              invalid={form.orgUnit?.isInvalid}
            />
          </Column>

          <Column sm={4} md={8} lg={16}>
            <ComboBox
              id={form.category?.id!}
              helperText="Use the code to identify specific program area and silviculture responsibility"
              items={categoryQuery.data ?? []}
              titleText={<RequiredLabel htmlFor={form.category?.id!}>Opening category</RequiredLabel>}
              onChange={(data) => setForm((prev) => ({ ...prev, category: { ...prev.category, value: data.selectedItem ?? undefined, isInvalid: false } }))}
              itemToString={codeDescriptionToDisplayText}
              readOnly={isReview}
              invalid={form.category?.isInvalid}
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
                maxLength={13}
                readOnly={isReview}
                invalid={form.openingGrossArea?.isInvalid}
                onBlur={(e) => setForm((prev) => ({ ...prev, openingGrossArea: { ...prev.openingGrossArea, value: e.target.value, isInvalid: false } }))}
              />

              <TextInput
                type="number"
                className="default-number-input"
                labelText={<RequiredLabel htmlFor={form.maxAllowablePermAccess?.id!}>Maximum allowable permanent access (%)</RequiredLabel>}
                id={form.maxAllowablePermAccess?.id!}
                helperText="Gross percentage area a permanent access structure can occupy in an opening"
                maxLength={5}
                readOnly={isReview}
                invalid={form.maxAllowablePermAccess?.isInvalid}
                onBlur={(e) => setForm((prev) => ({ ...prev, maxAllowablePermAccess: { ...prev.maxAllowablePermAccess, value: e.target.value, isInvalid: false } }))}
              />
            </Stack>
          </Column>

          <Column sm={4} md={8} lg={16}>
            <h3 className="default-heading-20px">Tenure information</h3>
          </Column>

          {
            form.tenureInfo.isInvalid
              ? (
                <Column sm={4} md={8} lg={16}>
                  <InlineNotification
                    title="At least one tenure licence is required"
                    kind="error"
                    lowContrast
                    className="inline-notification"
                    hideCloseButton
                    role="alert"
                  />
                </Column>

              )
              : null
          }

          <Column sm={4} md={8} lg={16}>
            <TableContainer className="default-table-container tenure-table-container">
              <TableToolbar>
                <div className="table-toolbar-container">
                  <p>Total licenses: {form.tenureInfo?.value?.length ?? 0}</p>
                  {
                    isReview
                      ? null
                      : (
                        <Button
                          iconDescription="Document Download"
                          kind="ghost"
                          renderIcon={Add}
                          onClick={() => setIsTenureModalOpen(true)}
                          size="lg"
                        >
                          Add licence
                        </Button>
                      )
                  }
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
                        .filter(header => !(isReview && header.key === 'action'))
                        .map(header => (
                          <TableHeader key={header.key}>{header.header}</TableHeader>
                        ))
                    }
                  </TableRow>
                </TableHead>
                <TableBody>
                  {form.tenureInfo?.value?.map((row) => (
                    <TableRow key={`tenure-info-table-row-${row.displayId}`}>
                      {
                        TenureHeaderConfig
                          .filter(header => !(isReview && header.key === 'action'))
                          .map((header) => {
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
              {
                form.tenureInfo?.value?.length ?? 0 > 1
                  ? null
                  : (
                    <EmptySection
                      pictogram="UserSearch"
                      title="Tenure information required"
                      description="No tenures have been added to this opening yet."
                    />
                  )
              }
            </TableContainer>
          </Column>
        </Grid>
      </Column>


      <Modal
        passiveModal
        open={isTenureModalOpen}
        modalHeading={<ModalHead title={isEditTenure ? 'Edit licence' : 'Add licence'} />}
        onRequestClose={handleTenureModalClose}
        className="add-tenure-licence-modal"
        preventCloseOnClickOutside
        size="sm"
        key={isTenureModalOpen ? tenureForm.displayId : "closed"}
        selectorPrimaryFocus={tenureForm.forestFileId.id}
      >
        <Grid className="tenure-licence-grid">
          <Column sm={4} md={8} lg={16}>
            <TextInput
              defaultValue={tenureForm.forestFileId.value}
              labelText={<RequiredLabel htmlFor={forestFileId}>Forest file ID</RequiredLabel>}
              id={tenureForm.forestFileId.id!}
              helperText="Apply the unique ID to identify the opening or cross reference program records. It will be set as the primary licence"
              placeholder="Ex. A20913"
              onBlur={(e) => setTenureForm((prev) => ({ ...prev, forestFileId: { ...prev.forestFileId, value: e.target.value, isInvalid: false } }))}
              maxLength={10}
              invalid={tenureForm.forestFileId.isInvalid}
              invalidText="The forest file id value is invalid."
            />
          </Column>

          <Column sm={4} md={8} lg={16}>
            <TextInput
              defaultValue={tenureForm.cutBlock.value}
              labelText={<RequiredLabel htmlFor={cutBlockId}>Cut block</RequiredLabel>}
              id={tenureForm.cutBlock.id!}
              helperText="The approved cut block identifier for the opening"
              placeholder="Ex. 20-98"
              onBlur={(e) => setTenureForm((prev) => ({ ...prev, cutBlock: { ...prev.cutBlock, value: e.target.value, isInvalid: false } }))}
              maxLength={10}
              invalid={tenureForm.cutBlock.isInvalid}
              invalidText="The cut block value is invalid."
            />
          </Column>

          <Column sm={4} md={8} lg={16}>
            <TextInput
              defaultValue={tenureForm.cuttingPermit.value}
              labelText={<RequiredLabel htmlFor={cuttingPermitId}>Cutting permit</RequiredLabel>}
              id={tenureForm.cuttingPermit.id!}
              helperText="Corresponds to licence in cutting permit document"
              placeholder="Ex. 16"
              onBlur={(e) => setTenureForm((prev) => ({ ...prev, cuttingPermit: { ...prev.cuttingPermit, value: e.target.value, isInvalid: false } }))}
              maxLength={3}
              invalid={tenureForm.cuttingPermit.isInvalid}
              invalidText="The cutting permit value is invalid."
            />
          </Column>

          <Column sm={4} md={8} lg={16}>
            <Toggle
              defaultToggled={tenureForm.isPrimary}
              id="tenure-primary-toggle"
              hideLabel
              labelText="Set as primary"
              onToggle={(checked) => setTenureForm((prev) => ({ ...prev, isPrimary: checked }))}
            />
          </Column>

          <Column sm={4} md={8} lg={16}>
            <Stack orientation="horizontal" gap={2} className="default-equal-split-stack">
              <Button className="modal-button" kind="ghost" onClick={handleTenureModalClose}>
                Cancel
              </Button>

              <Button className="modal-button" kind="primary" onClick={isEditTenure ? saveEditedTenure : addTenure} renderIcon={isEditTenure ? Save : Add}>
                {
                  isEditTenure ? 'Save changes' : ' Create an opening'
                }
              </Button>
            </Stack>
          </Column>
        </Grid>
      </Modal>
    </>
  )
}

export default DataForm;
