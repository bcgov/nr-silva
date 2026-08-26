
import { Column, Grid, Stack, FileUploaderItem, Button, TableContainer, Table, TableHead, TableRow, TableHeader, TableBody, TableCell, Tag } from "@carbon/react";
import MapPreview from "@/components/MapPreview";
import DisplayField from "@/components/DisplayField";
import { CreateOpeningFormType } from "@/screens/CreateOpening/definitions";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import { getClientLabel } from "@/utils/ForestClientUtils";
import { Edit } from "@carbon/icons-react";
import { PLACE_HOLDER } from "@/constants";
import { TenureHeaderConfig } from "./constants";

import './styles.scss';

type props = {
  form: CreateOpeningFormType;
  setStep: React.Dispatch<React.SetStateAction<number>>;
}

const StepThree = ({ form, setStep }: props) => {

  const clientQuery = useQuery({
    queryKey: ["forest-clients", "byNameAcronymNumber", form.client?.value],
    queryFn: () => API.ForestClientEndpointService.searchForestClients(form.client?.value ?? ''),
    enabled: !!form.client?.value,
    select: (data) => {
      return getClientLabel(data.find((c) => c.id === form.client?.value));
    }
  });

  const orgUnitQuery = useQuery({
    queryKey: ["codes", "org-units"],
    queryFn: () => API.CodesEndpointService.getOpeningOrgUnits(),
    select: (data) => {
      const orgUnit = data.find((o) => o.code === form.orgUnit?.value) ?? null;
      return `${orgUnit?.code} - ${orgUnit?.description}`;
    }
  });

  const categoryQuery = useQuery({
    queryKey: ["codes", "opening-categories"],
    queryFn: () => API.CodesEndpointService.getOpeningCategories(),
    select: (data) => {
      const category = data.find((c) => c.code === form.category?.value) ?? null;
      return `${category?.code} - ${category?.description}`;
    }
  });

  return (
    <>
      <Column sm={4} md={8} lg={16}>
        <h2 className="default-heading-28px">Review and create</h2>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <Grid className="opening-creation-review-grid">
          <Column sm={4} md={8} lg={16}>
            <h4>Opening information</h4>
          </Column>

          <Column sm={4} md={8} lg={16}>
            <h5>Spatial information</h5>
          </Column>

          <Column sm={4} md={8} lg={16}>
            <Stack gap={5}>
              <MapPreview geojson={form.file?.validatedObj?.geoJson as GeoJSON.FeatureCollection ?? null} />
              <FileUploaderItem
                className="default-file-uploader-item"
                name={form.file?.value?.name ?? "No file selected"}
                status="complete"
              />
            </Stack>
          </Column>

          <Column sm={4} md={8} lg={16}>
            <DisplayField
              label="Geometry area (ha)"
              value={form.file?.validatedObj?.geometryArea ?? null}
            />
          </Column>


          <Column sm={4} md={8} lg={16}>
            <DisplayField
              label="Opening gross area (ha)"
              value={form.openingGrossArea?.value ?? null}
              required
            />
          </Column>

          <Column sm={4} md={8} lg={16}>
            <DisplayField
              label="Maximum allowable permanent access (%)"
              value={form.maxAllowablePermAccess?.value ?? null}
              required
            />
          </Column>

          <Column sm={4} md={8} lg={16}>
            <hr className="default-hr" />
          </Column>

          <Column sm={4} md={8} lg={16}>
            <h5>General information</h5>
          </Column>

          <Column sm={4} md={8} lg={16}>
            <DisplayField
              label="Client"
              value={clientQuery.data ?? null}
              required
            />
          </Column>

          <Column sm={4} md={8} lg={16}>
            <DisplayField
              label="Org unit"
              value={orgUnitQuery.data ?? null}
              required
            />
          </Column>

          <Column sm={4} md={8} lg={16}>
            <DisplayField
              label="Category"
              value={categoryQuery.data ?? null}
              required
            />
          </Column>

          <Column sm={4} md={8} lg={16}>
            <DisplayField
              label="Licensee opening ID"
              value={form.licenseeOpeningId?.value ?? null}
            />
          </Column>

          <Column sm={4} md={8} lg={16}>
            <Button
              kind="tertiary"
              renderIcon={Edit}
              onClick={() => setStep(0)}
            >
              Edit opening information
            </Button>
          </Column>

          <Column className="full-width-col tenure-info-col" sm={4} md={8} lg={16}>
            <Stack gap={7}>
              <h4>Tenure information</h4>

              {
                form.tenureInfo?.validatedTenures && form.tenureInfo.validatedTenures.length > 0
                  ? (
                    <TableContainer className="default-table-container">
                      <Table className="default-zebra-table">
                        <TableHead>
                          <TableRow>
                            {TenureHeaderConfig.map((header) => (
                              <TableHeader key={header.key}>{header.header}</TableHeader>
                            ))}
                          </TableRow>
                        </TableHead>
                        <TableBody>
                          {form.tenureInfo.validatedTenures.map((tenure, index) => (
                            <TableRow key={`${tenure.fileId}-${index}`}>
                              {TenureHeaderConfig.map((header) => (
                                <TableCell key={header.key} className="tenure-table-cell">
                                  <span className="cell-content">
                                    {header.key === 'fileId' ? (
                                      <>
                                        {tenure.fileId}
                                        {tenure.isPrimary ? (
                                          <Tag size="sm" type="purple" title="Primary tenure">
                                            Primary tenure
                                          </Tag>
                                        ) : null}
                                      </>
                                    ) : (
                                      String(tenure[header.key] ?? PLACE_HOLDER)
                                    )}
                                  </span>
                                </TableCell>
                              ))}
                            </TableRow>
                          ))}
                        </TableBody>
                      </Table>
                    </TableContainer>
                  )
                  : null
              }

              <Button
                kind="tertiary"
                renderIcon={Edit}
                onClick={() => setStep(1)}
              >
                Edit tenure information
              </Button>
            </Stack>
          </Column>
        </Grid>
      </Column>
    </>
  );
};

export default StepThree;
