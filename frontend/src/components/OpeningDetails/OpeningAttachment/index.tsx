import React from "react";
import {
  Button, Column,
  Grid, Table, TableBody, TableCell,
  TableHead, TableHeader, TableRow,
} from "@carbon/react";
import { Download } from "@carbon/icons-react";
import { env } from "@/env";
import axios from "axios";
import { getAuthIdToken } from "@/services/AuthService";
import { useQuery } from "@tanstack/react-query";
import { pluralize } from "@/utils/StringUtils";
import { PLACE_HOLDER } from "@/constants";
import API from "@/services/API";
import { OpeningDetailsAttachmentMetaDto } from "@/services/OpenApi";

import EmptySection from "../../EmptySection";
import TableSkeleton from "../../TableSkeleton";
import { AttachmentTableHeaders } from "./constants";
import { formatLocalDate } from "@/utils/DateUtils";

import './styles.scss';

type OpeningAttachmentProps = {
  openingId: number;
}

const OpeningAttachment = ({ openingId }: OpeningAttachmentProps) => {
  let API_BASE_URL = env.VITE_BACKEND_URL ?? "http://localhost:8080";


  const attachmentListQuery = useQuery({
    queryKey: ['opening', openingId, 'attachment'],
    queryFn: () => API.OpeningEndpointService.getAttachments(openingId)
  });

  const handleDownload = async (guid: string) => {
    try {
      const { data: s3Url } = await axios.get<string>(
        `${API_BASE_URL}/api/openings/${openingId}/attachments/${guid}`,
        {
          headers: {
            Authorization: `Bearer ${getAuthIdToken()}`,
          },
        }
      );

      const link = document.createElement("a");
      link.href = s3Url;
      link.target = "_blank";
      link.rel = "noopener noreferrer";
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (error) {
      console.error("File download failed:", error);
    }
  };

  const renderCellContent = (headerKey: keyof OpeningDetailsAttachmentMetaDto, row: OpeningDetailsAttachmentMetaDto) => {
    switch (headerKey) {
      case "updateTimestamp":
        return formatLocalDate(row.updateTimestamp);

      case "attachmentGuid":
        return (
          <Button
            kind="ghost"
            size="sm"
            renderIcon={Download}
            iconDescription="Download file"
            aria-label={`Download ${row.attachmentName}`}
            onClick={() => handleDownload(row.attachmentGuid)}
          >
            Download
          </Button>
        );

      default:
        if (row[headerKey]) {
          return String(row[headerKey]);
        }
        return PLACE_HOLDER;
    }
  }

  if (attachmentListQuery.data?.length === 0) {
    return (
      <Grid className="opening-tenure-id-grid default-grid">
        <Column sm={4} md={8} lg={16}>
          <EmptySection
            pictogram="UserSearch"
            title="Nothing to show yet!"
            description="No attachments have been added to this opening yet."
          />
        </Column>
      </Grid>
    )
  }

  return (
    <Grid className="opening-tenure-id-grid default-grid">
      <Column sm={4} md={8} lg={16}>
        <div className="tab-title-container">
          <h3 className="default-tab-content-title">
            {attachmentListQuery.isLoading ? '...' : attachmentListQuery.data?.length}
            {' '}
            {
              pluralize('attachment', attachmentListQuery.data?.length ?? 0)
            }
            {' '}
            in this opening
          </h3>
        </div>
      </Column>

      <Column sm={4} md={8} lg={16}>
        {/* Table skeleton */}
        {
          attachmentListQuery.isLoading
            ? <TableSkeleton
              headers={AttachmentTableHeaders}
              showToolbar={false}
              showHeader={false}
              rowCount={10}
            />
            : (
              <Table
                className="default-zebra-table-with-border"
                aria-label="Tenure identification table"
                useZebraStyles
              >
                {/* Loaded Table section */}
                <TableHead>
                  <TableRow>
                    {
                      AttachmentTableHeaders
                        .map((header) => (
                          <TableHeader key={header.key}>
                            {header.header}
                          </TableHeader>
                        ))
                    }
                  </TableRow>
                </TableHead>
                <TableBody>
                  {
                    attachmentListQuery.data?.map((row) => (
                      <TableRow key={row.attachmentGuid}>
                        {
                          AttachmentTableHeaders
                            .map((header) => (
                              <TableCell key={header.key} className="tenure-table-cell">
                                <span className="cell-content">
                                  {renderCellContent(header.key, row)}
                                </span>
                              </TableCell>
                            ))
                        }
                      </TableRow>
                    ))
                  }
                </TableBody>
              </Table>
            )
        }
      </Column>
    </Grid >
  );
};


export default OpeningAttachment;
