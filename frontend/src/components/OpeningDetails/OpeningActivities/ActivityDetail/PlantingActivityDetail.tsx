import { Column, Grid, SkeletonText, Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@carbon/react";
import CodeDescriptionDto from "@/types/CodeDescriptionType";
import { PLACE_HOLDER, UNIQUE_CHARACTERS_UNICODE } from "@/constants";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import TableSkeleton from "../../../TableSkeleton";
import EmptySection from "../../../EmptySection";
import { DirectSeedingHeaders, PlantingHeaders } from "./constants";
import { ActivityDetailProps } from "./definitions";

const PlantingActivityDetail = ({ activityDetail, isLoading }: ActivityDetailProps) => {
    const renderCellContent = (
        data: CodeDescriptionDto | number | boolean | null,
        columnKey: string,
    ) => {
        if (!data) { return PLACE_HOLDER; }

        if (columnKey === "species") {
            return codeDescriptionToDisplayText(data as CodeDescriptionDto);
        } else if (columnKey === "cbst") {
            return data as boolean ? "Yes" : "No";
        }

        return String(data);
    }

    return (
        <Grid className="activity-detail-content-grid">
            <Column sm={4} md={4} lg={16}>
                {isLoading ? (
                    <SkeletonText className="activity-detail-content-title" />
                ) : (
                    <h6 className="activity-detail-content-title">
                        Planting specifications
                    </h6>
                )}
            </Column>

            <Column sm={16} md={16} lg={16}>
                <div className="species-table-container">
                    <div className="species-table-title-section">
                        <p className="species-table-title-section-body">{`Total planting: ${activityDetail?.totalPlanting ?? PLACE_HOLDER}`}</p>
                        <p className="species-table-title-section-body">{`${UNIQUE_CHARACTERS_UNICODE.PIPE}`}</p>
                        <p className="species-table-title-section-body">{`Total species: ${activityDetail?.species?.length}`}</p>
                    </div>
                    {/* Table Skeleton */}
                    {
                        isLoading ? (
                            <TableSkeleton
                                headers={PlantingHeaders}
                                showToolbar={false}
                                showHeader={false} />
                        ) : null
                    }
                    {/* Empty Table */}
                    {
                        !isLoading && !activityDetail?.species?.length ? (
                            <EmptySection
                                pictogram="Magnify"
                                title="There are no species to show yet"
                                description=""
                            />
                        ) : null
                    }
                    {/* Loaded table content */}
                    {
                        !isLoading && activityDetail?.species?.length ?
                            (
                                <Table
                                    className="default-zebra-table species-table"
                                    aria-label="Planting species table"
                                    useZebraStyles>
                                    <TableHead>
                                        <TableRow>
                                            {
                                                PlantingHeaders.map((header) => (
                                                    <TableHeader key={header.key}>{header.header}</TableHeader>
                                                ))
                                            }
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {
                                            activityDetail?.species.map((row) => {
                                                return (
                                                    <TableRow key={`${row.species.code}-${row.requestId}`}>
                                                        {
                                                            PlantingHeaders.map((header) => {
                                                                return (
                                                                    <TableCell className="species-table-cell" key={header.key}>
                                                                        {renderCellContent(row[header.key], header.key)}
                                                                    </TableCell>
                                                                )
                                                            })
                                                        }
                                                    </TableRow>
                                                )
                                            })
                                        }
                                    </TableBody>
                                </Table>
                            )
                            : null
                    }
                </div>
            </Column>
        </Grid>
    );
};

export default PlantingActivityDetail;
