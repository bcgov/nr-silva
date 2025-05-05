import { TableHeaderType } from "@/types/TableHeader";
import { MockedDirectSeedingSpecies, MockedPlantingDetail, MockedPlantingSpecies } from "../definitions";

export const DirectSeedingHeaders: TableHeaderType<keyof MockedDirectSeedingSpecies>[] = [
    { key: "speciesType", header: "Species", selected: true },
    { key: "numberPlanted", header: "Number planted", selected: true },
    { key: "numberBeyondTransferLimit", header: "Number beyond transfer limit", selected: true },
    { key: "cbst", header: "CBST", selected: true },
    { key: "lot", header: "Lot", selected: true }
];

export const PlantingHeaders: TableHeaderType<keyof MockedPlantingSpecies>[] = [
    { key: "speciesType", header: "Species", selected: true },
    { key: "numberPlanted", header: "Number planted", selected: true },
    { key: "numberBeyondTransferLimit", header: "Number beyond transfer limit", selected: true },
    { key: "cbst", header: "CBST", selected: true },
    { key: "requestId", header: "Request ID", selected: true },
    { key: "lot", header: "Lot", selected: true },
    { key: "bidPricePerTree", header: "Bid price per tree($)", selected: true }
];

