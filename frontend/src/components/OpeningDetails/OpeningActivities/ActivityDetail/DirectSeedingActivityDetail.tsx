import { Column, Grid, SkeletonText, Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@carbon/react";
import { ActivityDetailProps, MockedActivityDetailType } from "../definitions";
import SectionTitle from "../../../SectionTitle";
import TableSkeleton from "../../../TableSkeleton";
import { DirectSeedingHeaders } from "./constants";
import EmptySection from "../../../EmptySection";
import CodeDescriptionDto from "@/types/CodeDescriptionType";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import { PLACE_HOLDER } from "@/constants";


const DirectSeedingActivityDetail = ({activityDetail, isLoading} : ActivityDetailProps) => {
    const renderCellContent = (
        data: CodeDescriptionDto | number | boolean | null,
        columnKey: string,
    ) => {
        if (!data) {return PLACE_HOLDER;}

        if (columnKey === "speciesType") {
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
                        Direct seeding specifications
                    </h6>
                )}
            </Column>

            <Column sm={16} md={16} lg={16}>
                <div className="species-table-container">
                    <div className="species-table-title-section">
                        <p className="species-table-title-section-body">{`Total planting: ${activityDetail?.directSeedingSpecification?.totalPlanting}`}</p>
                        <p className="species-table-title-section-body">{'\u007c'}</p>
                        <p className="species-table-title-section-body">{`Total species: ${activityDetail?.directSeedingSpecification?.totalSpecies}`}</p>
                    </div>
                    {/* Table Skeleton */}
                    {
                        isLoading ? (
                            <TableSkeleton
                                headers={DirectSeedingHeaders}
                                showToolbar={false}
                                showHeader={false}/>
                        ) : null
                    }
                    {/* Empty Table */}
                    {
                        !isLoading && !activityDetail?.directSeedingSpecification?.species.length ? (
                            <EmptySection
                                pictogram="Magnify"
                                title="There are no species to show yet"
                                description=""
                                fill="#0073E6"
                            />
                        ) : null
                    }
                    {/* Loaded table content */}
                    {
                        !isLoading && activityDetail?.directSeedingSpecification?.species.length ?
                            (
                                <Table
                                    className="default-zebra-table species-table"
                                    aria-label="Direct seeding species table"
                                    useZebraStyles>
                                        <TableHead>
                                            <TableRow>
                                                {
                                                    DirectSeedingHeaders.map((header) => (
                                                        <TableHeader key={header.key}>{header.header}</TableHeader>
                                                    ))
                                                }
                                            </TableRow>
                                        </TableHead>
                                        <TableBody>
                                            {
                                                activityDetail?.directSeedingSpecification?.species.map((row) => {
                                                    return (
                                                        <TableRow key={row.speciesType.code}>
                                                            {
                                                                DirectSeedingHeaders.map((header) => {
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

export default DirectSeedingActivityDetail;
