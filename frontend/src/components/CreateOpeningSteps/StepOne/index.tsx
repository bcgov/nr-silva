import { useState } from "react";
import { Column, FileUploaderDropContainer, FileUploaderItem, Stack, TextInput } from "@carbon/react";
import { useAuth } from "@/contexts/AuthProvider";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import { formatForestClient } from "@/utils/ForestClientUtils";
import { MAX_FILE_SIZE, ACCEPTED_FILE_TYPES, MAX_FILE_MB } from "./constants";
import { CreateOpeningFormType } from "@/screens/CreateOpening/definitions";

import RequiredLabel from "../../RequiredLabel";

import "./styles.scss";

type StepOneProps = {
  form: CreateOpeningFormType;
  setForm: React.Dispatch<React.SetStateAction<CreateOpeningFormType>>;
}

const StepOne = ({
  form, setForm
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
    select: (data) => data.find((client) => client.clientNumber === form.client?.value),
  });

  const handleAddFile = async (addedFiles: File[]) => {
    setError(null);
    setForm((prev) => ({
      ...prev,
      geojson: {
        ...prev.geojson,
        value: undefined
      }
    }));

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
      },
      geojson: {
        ...prev.geojson,
        value: undefined
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
      },
      geojson: {
        ...prev.geojson,
        value: undefined
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
        <Stack gap={5} className="file-uploader-container">

          <h3 className="default-heading-20px">Spatial information</h3>

          <div className="file-uploader-title">
            <RequiredLabel id={labelId} htmlFor="opening-map-file-drop-container">
              Upload opening map geometry
            </RequiredLabel>
            <p id={helpId} className="file-type-p">
              Acceptable file types: GeoJSON, GML, XML(ESF)
            </p>
          </div>

          <FileUploaderDropContainer
            id="opening-map-file-drop-container"
            accept={ACCEPTED_FILE_TYPES}
            multiple={false}
            labelText="Click to upload or drag and drop the map file here (max 25 MB)"
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
        <TextInput
          labelText="Client"
          id="selected-client"
          readOnly
          required
          defaultValue={formatForestClient(userClientQuery.data)}
        />
      </Column>


    </>
  );
};

export default StepOne;
