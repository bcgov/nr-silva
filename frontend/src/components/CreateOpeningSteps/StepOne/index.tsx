import { useState } from "react";
import { Column, Dropdown, DropdownSkeleton, FileUploaderDropContainer, FileUploaderItem, Grid, InlineNotification, Stack, TextInput } from "@carbon/react";
import { useAuth } from "@/contexts/AuthProvider";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import { formatForestClient, getClientLocationLabel, sortLocationOptions } from "@/utils/ForestClientUtils";
import { MAX_FILE_SIZE, ACCEPTED_FILE_TYPES, MAX_FILE_MB } from "./constants";
import { CreateOpeningFormType } from "@/screens/CreateOpening/definitions";

import RequiredLabel from "../../RequiredLabel";

import "./styles.scss";

type StepOneProps = {
  form: CreateOpeningFormType;
  setForm: React.Dispatch<React.SetStateAction<CreateOpeningFormType>>;
  uploadError?: string;
  onUploadErrorDismiss?: () => void;
}

const StepOne = ({
  form, setForm, uploadError, onUploadErrorDismiss
}: StepOneProps) => {
  const { user } = useAuth();
  const [error, setError] = useState<string | null>(null);

  const validate = (f: File) => {
    if (f.size > MAX_FILE_SIZE) return `"${f.name}" exceeds ${MAX_FILE_MB} MB.`;
    const lc = f.name.toLowerCase();
    const byExt = [".geojson", ".gml", ".json"].some((ext) => lc.endsWith(ext));
    if (!byExt)
      return "File not supported. Please upload a valid file type: GeoJSON, GML.";
    return null;
  };

  const userClientQuery = useQuery({
    queryKey: ["forest-clients", "search", user?.associatedClients],
    queryFn: () =>
      API.ForestClientEndpointService.searchByClientNumbers(
        user!.associatedClients,
        0,
        user!.associatedClients.length
      ),
    enabled: !!user?.associatedClients.length,
    select: (data) => data?.map((client) => ({
      id: client.clientNumber,
      label: formatForestClient(client),
    })) ?? [],
  });

  const clientLocationQuery = useQuery({
    queryKey: ["clients", form.client?.value, "locations"],
    queryFn: () =>
      API.ForestClientEndpointService.getForestClientLocations(
        form.client?.value!
      ),
    enabled: !!form.client?.value,
    select: (data) => sortLocationOptions(data),
  });

  const handleClientChange = (selectedItem: { id: string; label: string } | null | undefined) => {
    setForm((prev) => ({
      ...prev,
      client: {
        ...prev.client,
        value: selectedItem?.id,
        isInvalid: false,
      },
      locationCode: {
        ...prev.locationCode,
        value: undefined,
      },
    }));
  };

  const orgUnitQuery = useQuery({
    queryKey: ["codes", "org-units"],
    queryFn: () => API.CodesEndpointService.getOpeningOrgUnits(),
    select: (data) => data?.map((unit) => ({
      id: unit.code,
      label: getClientLocationLabel(unit),
    })) ?? [],
  });

  const categoryQuery = useQuery({
    queryKey: ["codes", "opening-categories"],
    queryFn: () => API.CodesEndpointService.getOpeningCategories(),
    select: (data) => data?.map((category) => ({
      id: category.code,
      label: getClientLocationLabel(category),
    })) ?? [],
  });


  const handleAddFile = async (addedFiles: File[]) => {
    setError(null);

    if (!addedFiles?.length) return;

    const f = addedFiles[0];
    if (!f) return;

    const err = validate(f);
    if (err) {
      setForm((prev) => ({
        ...prev,
        file: {
          ...prev.file,
          value: undefined
        }
      }));
      setError(err);
      return;
    }

    setForm((prev) => ({
      ...prev,
      file: {
        ...prev.file,
        value: f,
        validatedObj: undefined,
      }
    }));
  };

  const handleFileDelete = () => {
    setError(null);
    setForm((prev) => ({
      ...prev,
      file: {
        ...prev.file,
        value: undefined,
        validatedObj: undefined,
      }
    }));
  };

  // A11y ids to associate label and helper text with the drop container
  const labelId = "opening-map-label";
  const helpId = "opening-map-help";

  return (
    <>
      <Column sm={4} md={8} lg={16}>
        <h2 className="default-heading-28px">Opening information</h2>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <Stack gap={6} className="file-uploader-container">

          <h3 className="default-heading-20px">Spatial information</h3>

          {
            uploadError
              ? (
                <InlineNotification
                  kind="error"
                  lowContrast
                  title="File upload failed"
                  subtitle={uploadError}
                  onCloseButtonClick={onUploadErrorDismiss}
                />
              )
              : null
          }

          <div className="file-uploader-title">
            <RequiredLabel id={labelId} htmlFor="opening-map-file-drop-container">
              Upload opening map geometry
            </RequiredLabel>
            <p id={helpId} className="file-type-p">
              Supported file types are .GeoJSON and .GML
            </p>
          </div>

          <FileUploaderDropContainer
            id="opening-map-file-drop-container"
            accept={ACCEPTED_FILE_TYPES}
            multiple={false}
            labelText="Drag and drop the map file here or click to upload (max 25 MB)"
            onAddFiles={(_evt, { addedFiles }) => handleAddFile(addedFiles)}
            aria-labelledby={labelId}
            aria-describedby={helpId}
          />

          {
            (form.file?.value || error)
              ? (
                <FileUploaderItem
                  className="default-file-uploader-item"
                  name={form.file?.value?.name ?? "Invalid file"}
                  status="edit"
                  onDelete={handleFileDelete}
                  invalid={!!error}
                  errorSubject={error ?? undefined}
                />
              )
              : null
          }

        </Stack>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <Stack gap={6} className="general-info-container">
          <h3 className="default-heading-20px">General information</h3>

          <Grid className="default-sub-grid">
            <Column sm={4} md={8} lg={8}>
              {userClientQuery.isLoading ? (
                <DropdownSkeleton />
              ) : (
                <Dropdown
                  id={form.client?.id ?? ''}
                  titleText={
                    <RequiredLabel id="selected-client-label" htmlFor={form.client?.id ?? ''}>
                      Client
                    </RequiredLabel>
                  }
                  label="Choose an option"
                  items={userClientQuery.data ?? []}
                  selectedItem={userClientQuery.data?.find((item) => item.id === form.client?.value)}
                  itemToString={(item) => item?.label ?? ''}
                  onChange={({ selectedItem }) => handleClientChange(selectedItem)}
                  invalid={form.client?.isInvalid}
                  invalidText="Select a client"
                />
              )}
            </Column>

            <Column sm={4} md={8} lg={8}>
              {clientLocationQuery.isLoading ? (
                <DropdownSkeleton />
              ) : (
                <Dropdown
                  id={form.locationCode?.id ?? ''}
                  titleText={
                    <RequiredLabel id="selected-location-label" htmlFor={form.locationCode?.id ?? ''}>
                      Location code
                    </RequiredLabel>
                  }
                  disabled={!form.client?.value}
                  label="Choose an option"
                  items={clientLocationQuery.data ?? []}
                  selectedItem={clientLocationQuery.data?.find((item) => item.id === form.locationCode?.value)}
                  itemToString={(item) => item?.label ?? ''}
                  onChange={({ selectedItem }) =>
                    setForm((prev) => ({
                      ...prev,
                      locationCode: {
                        ...prev.locationCode,
                        value: selectedItem?.id ?? undefined,
                        isInvalid: false,
                      },
                    }))
                  }
                  invalid={form.locationCode?.isInvalid}
                  invalidText="Select a location code"
                />
              )}
            </Column>

            <Column sm={4} md={8} lg={8}>
              {orgUnitQuery.isLoading ? (
                <DropdownSkeleton />
              ) : (
                <Dropdown
                  id={form.orgUnit?.id ?? ''}
                  titleText={
                    <RequiredLabel id="selected-org-unit-label" htmlFor={form.orgUnit?.id ?? ''}>
                      Org unit
                    </RequiredLabel>
                  }
                  label="Choose an option"
                  items={orgUnitQuery.data ?? []}
                  selectedItem={orgUnitQuery.data?.find((item) => item.id === form.orgUnit?.value)}
                  itemToString={(item) => item?.label ?? ''}
                  onChange={({ selectedItem }) =>
                    setForm((prev) => ({
                      ...prev,
                      orgUnit: {
                        ...prev.orgUnit,
                        value: selectedItem?.id ?? undefined,
                        isInvalid: false,
                      },
                    }))
                  }
                  invalid={form.orgUnit?.isInvalid}
                  invalidText="Select an org unit"
                />
              )}
            </Column>

            <Column sm={4} md={8} lg={8}>
              {categoryQuery.isLoading ? (
                <DropdownSkeleton />
              ) : (
                <Dropdown
                  id={form.category?.id ?? ''}
                  titleText={
                    <RequiredLabel id="selected-category-label" htmlFor={form.category?.id ?? ''}>
                      Opening category
                    </RequiredLabel>
                  }
                  label="Choose an option"
                  items={categoryQuery.data ?? []}
                  selectedItem={categoryQuery.data?.find((item) => item.id === form.category?.value)}
                  itemToString={(item) => item?.label ?? ''}
                  onChange={({ selectedItem }) =>
                    setForm((prev) => ({
                      ...prev,
                      category: {
                        ...prev.category,
                        value: selectedItem?.id ?? undefined,
                        isInvalid: false,
                      },
                    }))
                  }
                  invalid={form.category?.isInvalid}
                  invalidText="Select an opening category"
                />
              )}
            </Column>

            <Column sm={4} md={8} lg={8}>
              <TextInput
                id={form.licenseeOpeningId?.id ?? ''}
                labelText="Licensee opening ID"
                placeholder="Enter licensee opening ID"
                value={form.licenseeOpeningId?.value ?? ''}
                invalid={form.licenseeOpeningId?.isInvalid}
                invalidText="Must be fewer than 30 characters"
                onBlur={(e) =>
                  setForm((prev) => ({
                    ...prev,
                    licenseeOpeningId: {
                      ...prev.licenseeOpeningId,
                      value: e.target.value || undefined,
                      isInvalid: false,
                    },
                  }))
                }
              />
            </Column>
          </Grid>
        </Stack>
      </Column>

    </>
  );
};

export default StepOne;
