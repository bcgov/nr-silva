import { Column, FileUploaderDropContainer, FileUploaderItem, Stack, TextInput } from "@carbon/react";
import { useAuth } from "../../../contexts/AuthProvider";
import { useQuery } from "@tanstack/react-query";
import API from "../../../services/API";
import { formatForestClient } from "../../../utils/ForestClientUtils";
import { useEffect, useState } from "react";

import proj4 from "proj4";

import "../styles.scss";
import "./styles.scss";
import { MAX_FILE_SIZE, ACCEPTED_FILE_TYPES } from "./constants";
import MapPreview from "@/components/MapPreview";
import { parseToGeoJSON } from "../../../utils/SpatialUtils";


const FileUpload = () => {
  const { user, selectedClient } = useAuth();

  const [file, setFile] = useState<File | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [geojson, setGeojson] = useState<GeoJSON.FeatureCollection | null>(null);

  const validate = (f: File) => {
    if (f.size > MAX_FILE_SIZE) return `"${f.name}" exceeds 25 MB.`;
    const lc = f.name.toLowerCase();
    const byExt = [".geojson", ".gml", ".xml", ".json"].some((ext) => lc.endsWith(ext));
    if (!byExt)
      return "File not supported. Please upload a valid file type: GeoJSON, GML, XML(ESF).";
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
    select: (data) => data.find((client) => client.clientNumber === selectedClient),
  });

  const handleAddFile = async (addedFiles: File[]) => {
    setError(null);
    setGeojson(null);
    if (!addedFiles?.length) return;

    const f = addedFiles[0];
    if (!f) return;

    const err = validate(f);
    if (err) {
      setFile(null);
      setError(err);
      return;
    }

    try {
      const fc = await parseToGeoJSON(f);
      setFile(f);
      setGeojson(fc);
    } catch (e: any) {
      setFile(null);
      setGeojson(null);
      setError(e?.message || "Failed to parse file. Please upload a valid GeoJSON or GML/XML file.");
    }
  };

  const handleFileDelete = () => {
    setFile(null);
    setError(null);
    setGeojson(null);
  };

  // A11y ids to associate label and helper text with the drop container
  const labelId = "opening-map-label";
  const helpId = "opening-map-help";

  return (
    <>
      <Column sm={4} md={8} lg={16}>
        <h2 className="create-opening-step-title">File Upload</h2>
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

      <Column sm={4} md={8} lg={16}>
        <Stack gap={5} className="file-uploader-container">
          <div className="file-uploader-title">
            <label id={labelId} className="default-label" htmlFor="opening-map-file-drop-container">
              Upload opening map geometry
            </label>
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

          {(file || error) && (
            <FileUploaderItem
              id="opening-file-uploader-item"
              name={file?.name ?? "Invalid file"}
              status="edit"
              onDelete={handleFileDelete}
              invalid={!!error}
              errorSubject={error ?? undefined}
            />
          )}

          {/* Map preview */}
          {
            geojson ? <MapPreview geojson={geojson} /> : null
          }
        </Stack>
      </Column>
    </>
  );
};

export default FileUpload;
