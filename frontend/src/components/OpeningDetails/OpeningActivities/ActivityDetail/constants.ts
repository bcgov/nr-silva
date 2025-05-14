import { TableHeaderType } from "@/types/TableHeader";
import { OpeningActivitySpeciesDetailsDto } from "@/types/OpeningTypes";

export const DirectSeedingHeaders: TableHeaderType<keyof OpeningActivitySpeciesDetailsDto>[] = [
    { key: "species", header: "Species", selected: true },
    { key: "plantedNumber", header: "Number planted", selected: true },
    { key: "numberBeyondTransferLimit", header: "Number beyond transfer limit", selected: true },
    { key: "cbst", header: "CBST", selected: true },
    { key: "lot", header: "Lot", selected: true }
];

export const PlantingHeaders: TableHeaderType<keyof OpeningActivitySpeciesDetailsDto>[] = [
    { key: "species", header: "Species", selected: true },
    { key: "plantedNumber", header: "Number planted", selected: true },
    { key: "numberBeyondTransferLimit", header: "Number beyond transfer limit", selected: true },
    { key: "cbst", header: "CBST", selected: true },
    { key: "requestId", header: "Request ID", selected: true },
    { key: "lot", header: "Lot", selected: true },
    { key: "bidPricePerTree", header: "Bid price per tree($)", selected: true }
];

