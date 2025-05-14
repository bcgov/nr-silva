import { TableHeaderType } from "@/types/TableHeader";
import { ActivityFilterType, MockedActivityDetailType, MockedActivityResponseType, MockedDisturbanceType } from "./definitions";
import { OpeningDetailsActivitiesActivitiesDto, OpeningDetailsActivitiesDisturbanceDto } from "@/types/OpeningTypes";
import { DEFAULT_PAGE_NUM, OddPageSizesConfig } from "../../../constants/tableConstants";

export const COMPLEX_ACTIVITY_CODE = [
  'DS', 'JS', 'PL', 'PR', 'SP', 'SU'
] as const;

export const DisturbanceTableHeaders: TableHeaderType<keyof OpeningDetailsActivitiesDisturbanceDto>[] = [
  { key: "atuId", header: "Activity treatment unit ID" },
  { key: "disturbance", header: "Disturbance code" },
  { key: "system", header: "Silviculture system" },
  { key: "variant", header: "Variant" },
  { key: "cutPhase", header: "Cut phase" },
  { key: "disturbanceArea", header: "Disturbance area (ha)" },
  { key: "lastUpdatedOn", header: "Last updated" },
  { key: "startDate", header: "Start date" },
  { key: "endDate", header: "End date" },
];

export const ActivityTableHeaders: TableHeaderType<keyof OpeningDetailsActivitiesActivitiesDto | string>[] = [
  { key: "atuId", header: "Activity treatment unit ID", sortable: true },
  { key: "status", header: "Status", sortable: true },
  { key: "base", header: "Base", sortable: true },
  { key: "tech", header: "Tech", sortable: true },
  { key: "method", header: "Method", sortable: true },
  { key: "objective1", header: "Objective", sortable: true },
  { key: "area", header: "Area", sortable: true },
  { key: "funding", header: "Funding Source", sortable: true },
  { key: "projectId", header: "Project ID", sortable: true },
  { key: "lastUpdate", header: "Last updated", sortable: true },
  { key: "plannedDate", header: "Planned date", sortable: true },
  { key: "endDate", header: "End date", sortable: true },
];

export const DefaultFilter: ActivityFilterType = {
  page: DEFAULT_PAGE_NUM,
  size: OddPageSizesConfig[0],
} as const;

export const MOCKED_DISTURBANCE_EVENTS: MockedDisturbanceType[] = [
  {
    activityId: 1001,
    disturbance: { code: 'FIRE', description: 'Wildfire' },
    silvicultureSystem: { code: 'SE', description: 'Selective' },
    variant: { code: 'IDF', description: 'Interior Douglas-fir' },
    cutPhase: { code: 'PH1', description: 'Phase 1' },
    disturbanceArea: 12.5,
    updateTimestamp: '2025-04-25T10:00:00Z',
    startDate: '2025-03-01',
    endDate: '2025-03-15',
    licenseeActivityId: 'LA-001',
    disturbanceLocation: 'North Ridge',
    licenceNumber: 'A12345',
    cuttingPermit: 'CP-001',
    cutBlock: 'CB-101',
    comment: 'Initial wildfire response area'
  },
  {
    activityId: 1002,
    disturbance: { code: 'WIND', description: 'Windthrow' },
    silvicultureSystem: { code: 'CC', description: 'Clearcut' },
    variant: null,
    cutPhase: { code: 'PH2', description: 'Phase 2' },
    disturbanceArea: 8.9,
    updateTimestamp: '2025-04-24T14:30:00Z',
    startDate: '2025-02-10',
    endDate: '2025-02-20',
    licenseeActivityId: 'LA-002',
    disturbanceLocation: 'Valley Edge',
    licenceNumber: 'B67890',
    cuttingPermit: 'CP-002',
    cutBlock: 'CB-102',
    comment: 'Blowdown from last winter storm'
  },
  {
    activityId: 1003,
    disturbance: { code: 'LOGGING', description: 'Harvesting' },
    silvicultureSystem: { code: 'SE', description: 'Selective' },
    variant: { code: 'ESSF', description: 'Engelmann Spruce-Subalpine Fir' },
    cutPhase: { code: 'PH1', description: 'Phase 1' },
    disturbanceArea: 15.3,
    updateTimestamp: '2025-04-23T09:15:00Z',
    startDate: '2025-01-15',
    endDate: '2025-01-30',
    licenseeActivityId: 'LA-003',
    disturbanceLocation: 'Mountain Base',
    licenceNumber: 'C13579',
    cuttingPermit: 'CP-003',
    cutBlock: 'CB-103',
    comment: 'Initial harvest complete'
  },
  {
    activityId: 1004,
    disturbance: { code: 'PEST', description: 'Pest Infestation' },
    silvicultureSystem: { code: 'CC', description: 'Clearcut' },
    variant: { code: 'CWH', description: 'Coastal Western Hemlock' },
    cutPhase: { code: 'PH3', description: 'Phase 3' },
    disturbanceArea: 6.7,
    updateTimestamp: '2025-04-22T11:45:00Z',
    startDate: null,
    endDate: '2025-01-10',
    licenseeActivityId: 'LA-004',
    disturbanceLocation: 'East Slope',
    licenceNumber: 'D24680',
    cuttingPermit: 'CP-004',
    cutBlock: 'CB-104',
    comment: 'Infestation managed and monitored'
  },
  {
    activityId: 1005,
    disturbance: { code: 'FLOOD', description: 'Flood Damage' },
    silvicultureSystem: { code: 'SE', description: 'Selective' },
    variant: { code: 'BWBS', description: 'Boreal White and Black Spruce' },
    cutPhase: { code: 'PH2', description: 'Phase 2' },
    disturbanceArea: 10.2,
    updateTimestamp: '2025-04-21T16:10:00Z',
    startDate: '2025-02-01',
    endDate: null,
    licenseeActivityId: 'LA-005',
    disturbanceLocation: 'Lower Basin',
    licenceNumber: 'E11223',
    cuttingPermit: 'CP-005',
    cutBlock: 'CB-105',
    comment: 'Area flooded and later accessed'
  },
];

export const MOCKED_ACTIVITY_RES: MockedActivityResponseType = {
  "content": [
    {
      "activityId": 1059,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "SU",
        "description": "Surveys"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 82.7,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": 1029,
      "updateTimestamp": "2025-03-07T21:39:41.145501",
      "plannedDate": "2022-08-25T21:39:41.145505",
      "endDate": "2023-07-06T21:39:41.145510"
    },
    {
      "activityId": 1081,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "DS",
        "description": "Direct Seeding"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 12.5,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2025-01-19T21:39:41.146323",
      "plannedDate": "2022-10-15T21:39:41.146329",
      "endDate": "2023-11-17T21:39:41.146335"
    },
    {
      "activityId": 1058,
      "status": {
        "code": "C",
        "description": "Completed"
      },
      "base": {
        "code": "HI",
        "description": "Habitat Installation"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 20.4,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2024-12-15T21:39:41.145461",
      "plannedDate": "2023-07-22T21:39:41.145468",
      "endDate": "2024-06-16T21:39:41.145475"
    },
    {
      "activityId": 1085,
      "status": {
        "code": "C",
        "description": "Completed"
      },
      "base": {
        "code": "PC",
        "description": "Pest Control"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 46.6,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2024-10-11T21:39:41.146420",
      "plannedDate": "2022-09-06T21:39:41.146423",
      "endDate": "2022-11-08T21:39:41.146427"
    },
    {
      "activityId": 1076,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "SX",
        "description": "Silviculture Trials"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 85.3,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2024-10-07T21:39:41.146181",
      "plannedDate": "2023-01-15T21:39:41.146185",
      "endDate": "2024-12-28T21:39:41.146189"
    },
    {
      "activityId": 1087,
      "status": {
        "code": "C",
        "description": "Completed"
      },
      "base": {
        "code": "HI",
        "description": "Habitat Installation"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 58.2,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2024-08-23T21:39:41.146485",
      "plannedDate": "2024-06-17T21:39:41.146490",
      "endDate": "2022-11-07T21:39:41.146494"
    },
    {
      "activityId": 1055,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "DS",
        "description": "Direct Seeding"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 13.0,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": 1205,
      "updateTimestamp": "2024-08-23T21:39:41.145356",
      "plannedDate": "2023-02-03T21:39:41.145360",
      "endDate": "2023-12-04T21:39:41.145366"
    },
    {
      "activityId": 1086,
      "status": {
        "code": "C",
        "description": "Completed"
      },
      "base": {
        "code": "EP",
        "description": "Experimental Activities"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 59.8,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": 1457,
      "updateTimestamp": "2024-08-15T21:39:41.146450",
      "plannedDate": "2023-03-26T21:39:41.146456",
      "endDate": "2025-03-08T21:39:41.146462"
    },
    {
      "activityId": 1074,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "RC",
        "description": "Recreation"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 75.8,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2024-07-04T21:39:41.146121",
      "plannedDate": "2023-07-01T21:39:41.146125",
      "endDate": "2025-02-21T21:39:41.146128"
    },
    {
      "activityId": 1063,
      "status": {
        "code": "C",
        "description": "Completed"
      },
      "base": {
        "code": "LB",
        "description": "Land Based Planning"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 28.9,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2024-07-04T21:39:41.145604",
      "plannedDate": "2023-10-21T21:39:41.145607",
      "endDate": "2025-04-14T21:39:41.145612"
    },
    {
      "activityId": 1060,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "HI",
        "description": "Habitat Installation"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 25.2,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2024-06-22T21:39:41.145530",
      "plannedDate": "2024-01-14T21:39:41.145533",
      "endDate": "2025-02-07T21:39:41.145537"
    },
    {
      "activityId": 1061,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "JS",
        "description": "Juvenile Spacing"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 13.9,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": 1379,
      "updateTimestamp": "2024-05-31T21:39:41.145554",
      "plannedDate": "2024-08-18T21:39:41.145558",
      "endDate": "2024-01-01T21:39:41.145561"
    },
    {
      "activityId": 1052,
      "status": {
        "code": "C",
        "description": "Completed"
      },
      "base": {
        "code": "PR",
        "description": "Pruning"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 7.7,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2024-04-17T21:39:41.145244",
      "plannedDate": "2024-03-26T21:39:41.145249",
      "endDate": "2022-08-24T21:39:41.145256"
    },
    {
      "activityId": 1079,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "DS",
        "description": "Direct Seeding"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 69.3,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2024-04-11T21:39:41.146266",
      "plannedDate": "2023-08-02T21:39:41.146270",
      "endDate": "2024-09-08T21:39:41.146274"
    },
    {
      "activityId": 1064,
      "status": {
        "code": "C",
        "description": "Completed"
      },
      "base": {
        "code": "PC",
        "description": "Pest Control"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 36.9,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": 1295,
      "updateTimestamp": "2024-03-23T21:39:41.145628",
      "plannedDate": "2023-11-23T21:39:41.145632",
      "endDate": "2024-01-17T21:39:41.145636"
    },
    {
      "activityId": 1068,
      "status": {
        "code": "C",
        "description": "Completed"
      },
      "base": {
        "code": "FE",
        "description": "Fertilization"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 19.9,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2024-02-19T21:39:41.145722",
      "plannedDate": "2024-11-18T21:39:41.145726",
      "endDate": "2024-02-25T21:39:41.145733"
    },
    {
      "activityId": 1062,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "ST",
        "description": "Species Translocation"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 76.5,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2023-11-29T21:39:41.145582",
      "plannedDate": "2022-11-04T21:39:41.145585",
      "endDate": "2024-06-11T21:39:41.145589"
    },
    {
      "activityId": 1065,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "JS",
        "description": "Juvenile Spacing"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 20.7,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2023-11-27T21:39:41.145651",
      "plannedDate": "2025-03-12T21:39:41.145654",
      "endDate": "2025-03-07T21:39:41.145658"
    },
    {
      "activityId": 1045,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "SU",
        "description": "Surveys"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 57.5,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": 1191,
      "updateTimestamp": "2023-11-03T21:39:41.145005",
      "plannedDate": "2022-07-29T21:39:41.145019",
      "endDate": "2024-04-18T21:39:41.145023"
    },
    {
      "activityId": 1080,
      "status": {
        "code": "C",
        "description": "Completed"
      },
      "base": {
        "code": "SP",
        "description": "Site Preparation"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 68.4,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": 1610,
      "updateTimestamp": "2023-11-01T21:39:41.146290",
      "plannedDate": "2024-01-21T21:39:41.146294",
      "endDate": "2023-10-30T21:39:41.146297"
    },
    {
      "activityId": 1069,
      "status": {
        "code": "C",
        "description": "Completed"
      },
      "base": {
        "code": "BR",
        "description": "Brushing"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 56.5,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2023-11-01T21:39:41.145926",
      "plannedDate": "2024-04-02T21:39:41.145939",
      "endDate": "2024-06-05T21:39:41.145943"
    },
    {
      "activityId": 1084,
      "status": {
        "code": "C",
        "description": "Planning"
      },
      "base": {
        "code": "SP",
        "description": "Site Preparation"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 12.7,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2023-10-29T21:39:41.146397",
      "plannedDate": "2024-12-13T21:39:41.146401",
      "endDate": "2023-04-08T21:39:41.146404"
    },
    {
      "activityId": 1089,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "DN",
        "description": "Denudation"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 56.7,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2023-09-30T21:39:41.146532",
      "plannedDate": "2023-10-28T21:39:41.146536",
      "endDate": "2024-12-06T21:39:41.146539"
    },
    {
      "activityId": 1070,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "SX",
        "description": "Silviculture Trials"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 20.0,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2023-09-29T21:39:41.145969",
      "plannedDate": "2024-06-21T21:39:41.145976",
      "endDate": "2024-09-04T21:39:41.145983"
    },
    {
      "activityId": 1082,
      "status": {
        "code": "C",
        "description": "Planning"
      },
      "base": {
        "code": "HI",
        "description": "Habitat Installation"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 26.2,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": 1003,
      "updateTimestamp": "2023-09-08T21:39:41.146354",
      "plannedDate": "2025-02-07T21:39:41.146358",
      "endDate": "2025-03-07T21:39:41.146362"
    },
    {
      "activityId": 1050,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "RD",
        "description": "Roads"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 73.0,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2023-08-15T21:39:41.145160",
      "plannedDate": "2023-01-01T21:39:41.145167",
      "endDate": "2024-10-20T21:39:41.145173"
    },
    {
      "activityId": 1051,
      "status": {
        "code": "C",
        "description": "Completed"
      },
      "base": {
        "code": "PL",
        "description": "Planting"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 35.8,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2023-08-02T21:39:41.145207",
      "plannedDate": "2024-02-02T21:39:41.145212",
      "endDate": "2024-07-15T21:39:41.145216"
    },
    {
      "activityId": 1054,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "DS",
        "description": "Direct Seeding"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 81.2,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2023-06-09T21:39:41.145332",
      "plannedDate": "2024-05-02T21:39:41.145335",
      "endDate": "2023-04-27T21:39:41.145339"
    },
    {
      "activityId": 1047,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "BR",
        "description": "Brushing"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 81.5,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2023-05-11T21:39:41.145073",
      "plannedDate": "2023-12-19T21:39:41.145078",
      "endDate": "2023-07-16T21:39:41.145084"
    },
    {
      "activityId": 1066,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "PC",
        "description": "Pest Control"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 51.2,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2023-04-28T21:39:41.145677",
      "plannedDate": "2023-09-24T21:39:41.145681",
      "endDate": "2024-06-20T21:39:41.145685"
    },
    {
      "activityId": 1073,
      "status": {
        "code": "C",
        "description": "Completed"
      },
      "base": {
        "code": "RD",
        "description": "Roads"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 6.2,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2023-04-19T21:39:41.146085",
      "plannedDate": "2023-12-26T21:39:41.146091",
      "endDate": "2025-01-10T21:39:41.146097"
    },
    {
      "activityId": 1067,
      "status": {
        "code": "C",
        "description": "Completed"
      },
      "base": {
        "code": "SX",
        "description": "Silviculture Trials"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 27.3,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": 1671,
      "updateTimestamp": "2023-04-15T21:39:41.145700",
      "plannedDate": "2025-03-01T21:39:41.145704",
      "endDate": "2023-09-03T21:39:41.145708"
    },
    {
      "activityId": 1083,
      "status": {
        "code": "C",
        "description": "Completed"
      },
      "base": {
        "code": "RC",
        "description": "Recreation"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 41.5,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": 1787,
      "updateTimestamp": "2023-02-28T21:39:41.146375",
      "plannedDate": "2023-07-06T21:39:41.146379",
      "endDate": "2023-04-07T21:39:41.146382"
    },
    {
      "activityId": 1053,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "DS",
        "description": "Direct Seeding"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 91.4,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2023-02-19T21:39:41.145304",
      "plannedDate": "2024-07-30T21:39:41.145308",
      "endDate": "2023-05-03T21:39:41.145312"
    },
    {
      "activityId": 1088,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "DS",
        "description": "Direct Seeding"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 5.6,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2023-02-08T21:39:41.146509",
      "plannedDate": "2023-11-02T21:39:41.146512",
      "endDate": "2024-05-31T21:39:41.146516"
    },
    {
      "activityId": 1075,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "DS",
        "description": "Direct Seeding"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 93.5,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2023-02-06T21:39:41.146142",
      "plannedDate": "2023-12-02T21:39:41.146146",
      "endDate": "2024-10-14T21:39:41.146152"
    },
    {
      "activityId": 1046,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "SX",
        "description": "Silviculture Trials"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 26.1,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2023-02-05T21:39:41.145047",
      "plannedDate": "2025-02-12T21:39:41.145051",
      "endDate": "2024-05-04T21:39:41.145055"
    },
    {
      "activityId": 1048,
      "status": {
        "code": "C",
        "description": "Completed"
      },
      "base": {
        "code": "BR",
        "description": "Brushing"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 32.2,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2023-01-09T21:39:41.145101",
      "plannedDate": "2023-10-14T21:39:41.145105",
      "endDate": "2023-12-25T21:39:41.145109"
    },
    {
      "activityId": 1056,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "HI",
        "description": "Habitat Installation"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 53.0,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2022-12-31T21:39:41.145390",
      "plannedDate": "2023-09-12T21:39:41.145396",
      "endDate": "2025-02-06T21:39:41.145403"
    },
    {
      "activityId": 1077,
      "status": {
        "code": "C",
        "description": "Completed"
      },
      "base": {
        "code": "HI",
        "description": "Habitat Installation"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 61.5,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2022-12-28T21:39:41.146204",
      "plannedDate": "2024-02-10T21:39:41.146209",
      "endDate": "2023-11-12T21:39:41.146215"
    },
    {
      "activityId": 1057,
      "status": {
        "code": "C",
        "description": "Completed"
      },
      "base": {
        "code": "SP",
        "description": "Site Preparation"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 43.8,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": 1932,
      "updateTimestamp": "2022-12-23T21:39:41.145429",
      "plannedDate": "2024-12-16T21:39:41.145436",
      "endDate": "2023-11-09T21:39:41.145440"
    },
    {
      "activityId": 1090,
      "status": {
        "code": "C",
        "description": "Completed"
      },
      "base": {
        "code": "CC",
        "description": "Cone Collection"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 27.6,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": 1689,
      "updateTimestamp": "2022-11-10T21:39:41.146571",
      "plannedDate": "2025-02-07T21:39:41.146578",
      "endDate": "2024-10-12T21:39:41.146584"
    },
    {
      "activityId": 1049,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "SX",
        "description": "Silviculture Trials"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 13.3,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2022-10-25T21:39:41.145125",
      "plannedDate": "2024-03-09T21:39:41.145129",
      "endDate": "2023-03-23T21:39:41.145133"
    },
    {
      "activityId": 1071,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "SP",
        "description": "Site Preparation"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 13.1,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2022-10-18T21:39:41.146012",
      "plannedDate": "2024-03-14T21:39:41.146019",
      "endDate": "2023-01-05T21:39:41.146028"
    },
    {
      "activityId": 1072,
      "status": {
        "code": "P",
        "description": "Planning"
      },
      "base": {
        "code": "HC",
        "description": "Habitat Connectivity"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 21.0,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2022-09-10T21:39:41.146059",
      "plannedDate": "2023-09-25T21:39:41.146063",
      "endDate": "2023-05-11T21:39:41.146067"
    },
    {
      "activityId": 1078,
      "status": {
        "code": "C",
        "description": "Completed"
      },
      "base": {
        "code": "FE",
        "description": "Fertilization"
      },
      "tech": {
        "code": "T1",
        "description": "Tech A"
      },
      "method": {
        "code": "M1",
        "description": "Method A"
      },
      "objective": {
        "code": "O1",
        "description": "Objective A"
      },
      "area": 39.0,
      "fundingSource": {
        "code": "F1",
        "description": "Fund A"
      },
      "projectId": null,
      "updateTimestamp": "2022-08-20T21:39:41.146238",
      "plannedDate": "2024-05-20T21:39:41.146243",
      "endDate": "2024-06-17T21:39:41.146246"
    }
  ],
  "page": {
    "size": 46,
    "page": 0,
    "totalElements": 46,
    "totalPages": 1
  }
};

export const MOCKED_ACTIVITY_DETAIL: MockedActivityDetailType[] = [
  {
    "activityId": 1059,
    "licenseeActivityId": "LIC-5336",
    "intraAgencyNumber": 766,
    "activityLocation": "Location-14",
    "plannedAmount": 79.0,
    "treatedAmount": 42.2,
    "plannedCost": 675.1,
    "actualCost": 548.2,
    "comment": null,
    "surveySpecification": {
      "numberOfPlots": 50,
      "minPlotsPerStratum": 13
    },
    "base": {
      "code": "SU",
      "description": "Surveys"
    }
  },
  {
    "activityId": 1081,
    "licenseeActivityId": "LIC-8298",
    "intraAgencyNumber": 928,
    "activityLocation": "Location-4",
    "plannedAmount": 50.8,
    "treatedAmount": 51.8,
    "plannedCost": 511.1,
    "actualCost": 152.0,
    "comment": "Survived a deer invasion. Barely.",
    "directSeedingSpecification": {
      "totalPlanting": 336,
      "totalSpecies": 3,
      "species": [
        {
          "speciesType": {
            "code": "S1",
            "description": "Species A"
          },
          "numberPlanted": 300,
          "numberBeyondTransferLimit": 50,
          "cbst": true,
          "lot": 60
        }
      ]
    },
    "base": {
      "code": "DS",
      "description": "Direct Seeding"
    }
  },
  {
    "activityId": 1058,
    "licenseeActivityId": "LIC-9740",
    "intraAgencyNumber": 195,
    "activityLocation": "Location-33",
    "plannedAmount": 67.7,
    "treatedAmount": 59.6,
    "plannedCost": 622.6,
    "actualCost": 254.1,
    "comment": null,
    "base": {
      "code": "HI",
      "description": "Habitat Installation"
    }
  },
  {
    "activityId": 1085,
    "licenseeActivityId": "LIC-2848",
    "intraAgencyNumber": 132,
    "activityLocation": "Location-43",
    "plannedAmount": 47.7,
    "treatedAmount": 76.8,
    "plannedCost": 895.4,
    "actualCost": 114.1,
    "comment": null,
    "base": {
      "code": "PC",
      "description": "Pest Control"
    }
  },
  {
    "activityId": 1076,
    "licenseeActivityId": "LIC-6417",
    "intraAgencyNumber": 266,
    "activityLocation": "Location-47",
    "plannedAmount": 51.3,
    "treatedAmount": 26.5,
    "plannedCost": 267.6,
    "actualCost": 763.0,
    "comment": null,
    "base": {
      "code": "SX",
      "description": "Silviculture Trials"
    }
  },
  {
    "activityId": 1087,
    "licenseeActivityId": "LIC-3347",
    "intraAgencyNumber": 776,
    "activityLocation": "Location-34",
    "plannedAmount": 29.9,
    "treatedAmount": 30.7,
    "plannedCost": 154.9,
    "actualCost": 953.7,
    "comment": null,
    "base": {
      "code": "HI",
      "description": "Habitat Installation"
    }
  },
  {
    "activityId": 1055,
    "licenseeActivityId": "LIC-1430",
    "intraAgencyNumber": 469,
    "activityLocation": "Location-36",
    "plannedAmount": 61.5,
    "treatedAmount": 47.3,
    "plannedCost": 984.7,
    "actualCost": 216.8,
    "comment": null,
    "directSeedingSpecification": {
      "totalPlanting": 144,
      "totalSpecies": 1,
      "species": [
        {
          "speciesType": {
            "code": "S1",
            "description": "Species A"
          },
          "numberPlanted": 300,
          "numberBeyondTransferLimit": 50,
          "cbst": true,
          "lot": 60
        }
      ]
    },
    "base": {
      "code": "DS",
      "description": "Direct Seeding"
    }
  },
  {
    "activityId": 1086,
    "licenseeActivityId": "LIC-7590",
    "intraAgencyNumber": 549,
    "activityLocation": "Location-15",
    "plannedAmount": 58.6,
    "treatedAmount": 75.7,
    "plannedCost": 798.5,
    "actualCost": 226.1,
    "comment": null,
    "base": {
      "code": "EP",
      "description": "Experimental Activities"
    }
  },
  {
    "activityId": 1074,
    "licenseeActivityId": "LIC-2465",
    "intraAgencyNumber": 767,
    "activityLocation": "Location-37",
    "plannedAmount": 30.9,
    "treatedAmount": 74.1,
    "plannedCost": 865.3,
    "actualCost": 133.6,
    "comment": null,
    "base": {
      "code": "RC",
      "description": "Recreation"
    }
  },
  {
    "activityId": 1063,
    "licenseeActivityId": "LIC-2761",
    "intraAgencyNumber": 541,
    "activityLocation": "Location-9",
    "plannedAmount": 89.4,
    "treatedAmount": 73.0,
    "plannedCost": 787.5,
    "actualCost": 429.3,
    "comment": null,
    "base": {
      "code": "LB",
      "description": "Land Based Planning"
    }
  },
  {
    "activityId": 1060,
    "licenseeActivityId": "LIC-1981",
    "intraAgencyNumber": 261,
    "activityLocation": "Location-29",
    "plannedAmount": 47.4,
    "treatedAmount": 47.0,
    "plannedCost": 406.2,
    "actualCost": 229.3,
    "comment": null,
    "base": {
      "code": "HI",
      "description": "Habitat Installation"
    }
  },
  {
    "activityId": 1061,
    "licenseeActivityId": "LIC-3007",
    "intraAgencyNumber": 373,
    "activityLocation": "Location-29",
    "plannedAmount": 32.3,
    "treatedAmount": 10.6,
    "plannedCost": 361.2,
    "actualCost": 445.7,
    "comment": null,
    "spacingSpecification": {
      "targetInterTreeDistance": 3.3,
      "allowableVariationInterTreeDistance": 0.4,
      "allowableTreesPerPlot": 7,
      "spacingPerHa": 85.1
    },
    "base": {
      "code": "JS",
      "description": "Juvenile Spacing"
    }
  },
  {
    "activityId": 1052,
    "licenseeActivityId": "LIC-1530",
    "intraAgencyNumber": 719,
    "activityLocation": "Location-10",
    "plannedAmount": 71.1,
    "treatedAmount": 94.9,
    "plannedCost": 434.8,
    "actualCost": 786.8,
    "comment": null,
    "pruningSpecification": {
      "totalStemsPerHa": 123.3,
      "stemsperHaToPrune": 84.1,
      "targetInterTreeDistance": 2.3,
      "minInterTreeDistance": 2.1,
      "heightAboveGround": 3.2,
      "minLiveCrown": 0.7
    },
    "base": {
      "code": "PR",
      "description": "Pruning"
    }
  },
  {
    "activityId": 1079,
    "licenseeActivityId": "LIC-4590",
    "intraAgencyNumber": 236,
    "activityLocation": "Location-31",
    "plannedAmount": 23.8,
    "treatedAmount": 75.0,
    "plannedCost": 645.2,
    "actualCost": 474.3,
    "comment": null,
    "directSeedingSpecification": {
      "totalPlanting": 381,
      "totalSpecies": 2,
      "species": [
        {
          "speciesType": {
            "code": "S1",
            "description": "Species A"
          },
          "numberPlanted": 300,
          "numberBeyondTransferLimit": 50,
          "cbst": true,
          "lot": 60
        }
      ]
    },
    "base": {
      "code": "DS",
      "description": "Direct Seeding"
    }
  },
  {
    "activityId": 1064,
    "licenseeActivityId": "LIC-5582",
    "intraAgencyNumber": 945,
    "activityLocation": "Location-40",
    "plannedAmount": 71.2,
    "treatedAmount": 12.8,
    "plannedCost": 977.6,
    "actualCost": 454.5,
    "comment": null,
    "base": {
      "code": "PC",
      "description": "Pest Control"
    }
  },
  {
    "activityId": 1068,
    "licenseeActivityId": "LIC-4340",
    "intraAgencyNumber": 520,
    "activityLocation": "Location-33",
    "plannedAmount": 79.7,
    "treatedAmount": 92.1,
    "plannedCost": 885.5,
    "actualCost": 762.3,
    "comment": null,
    "base": {
      "code": "FE",
      "description": "Fertilization"
    }
  },
  {
    "activityId": 1062,
    "licenseeActivityId": "LIC-9148",
    "intraAgencyNumber": 123,
    "activityLocation": "Location-6",
    "plannedAmount": 95.1,
    "treatedAmount": 51.0,
    "plannedCost": 970.0,
    "actualCost": 293.6,
    "comment": null,
    "base": {
      "code": "ST",
      "description": "Species Translocation"
    }
  },
  {
    "activityId": 1065,
    "licenseeActivityId": "LIC-1637",
    "intraAgencyNumber": 240,
    "activityLocation": "Location-46",
    "plannedAmount": 40.0,
    "treatedAmount": 47.9,
    "plannedCost": 511.3,
    "actualCost": 645.6,
    "comment": null,
    "spacingSpecification": {
      "targetInterTreeDistance": 2.8,
      "allowableVariationInterTreeDistance": 0.6,
      "allowableTreesPerPlot": 14,
      "spacingPerHa": 45.5
    },
    "base": {
      "code": "JS",
      "description": "Juvenile Spacing"
    }
  },
  {
    "activityId": 1045,
    "licenseeActivityId": "LIC-2108",
    "intraAgencyNumber": 638,
    "activityLocation": "Location-35",
    "plannedAmount": 55.7,
    "treatedAmount": 98.8,
    "plannedCost": 598.7,
    "actualCost": 451.4,
    "comment": null,
    "surveySpecification": {
      "numberOfPlots": 40,
      "minPlotsPerStratum": 5
    },
    "base": {
      "code": "SU",
      "description": "Surveys"
    }
  },
  {
    "activityId": 1080,
    "licenseeActivityId": "LIC-6927",
    "intraAgencyNumber": 179,
    "activityLocation": "Location-37",
    "plannedAmount": 20.1,
    "treatedAmount": 83.0,
    "plannedCost": 912.6,
    "actualCost": 281.9,
    "comment": null,
    "sitePrepSpecification": {
      "targetPreparedSpotPerHa": 58.3
    },
    "base": {
      "code": "SP",
      "description": "Site Preparation"
    }
  },
  {
    "activityId": 1069,
    "licenseeActivityId": "LIC-3837",
    "intraAgencyNumber": 570,
    "activityLocation": "Location-35",
    "plannedAmount": 40.8,
    "treatedAmount": 36.1,
    "plannedCost": 794.8,
    "actualCost": 748.8,
    "comment": null,
    "base": {
      "code": "BR",
      "description": "Brushing"
    }
  },
  {
    "activityId": 1084,
    "licenseeActivityId": "LIC-4648",
    "intraAgencyNumber": 954,
    "activityLocation": "Location-1",
    "plannedAmount": 78.1,
    "treatedAmount": 97.9,
    "plannedCost": 516.7,
    "actualCost": 747.2,
    "comment": null,
    "sitePrepSpecification": {
      "targetPreparedSpotPerHa": 48.2
    },
    "base": {
      "code": "SP",
      "description": "Site Preparation"
    }
  },
  {
    "activityId": 1089,
    "licenseeActivityId": "LIC-7363",
    "intraAgencyNumber": 348,
    "activityLocation": "Location-17",
    "plannedAmount": 28.3,
    "treatedAmount": 65.0,
    "plannedCost": 930.0,
    "actualCost": 862.4,
    "comment": null,
    "base": {
      "code": "DN",
      "description": "Denudation"
    }
  },
  {
    "activityId": 1070,
    "licenseeActivityId": "LIC-5876",
    "intraAgencyNumber": 889,
    "activityLocation": "Location-19",
    "plannedAmount": 73.4,
    "treatedAmount": 69.3,
    "plannedCost": 540.1,
    "actualCost": 531.8,
    "comment": null,
    "base": {
      "code": "SX",
      "description": "Silviculture Trials"
    }
  },
  {
    "activityId": 1082,
    "licenseeActivityId": "LIC-7941",
    "intraAgencyNumber": 452,
    "activityLocation": "Location-45",
    "plannedAmount": 15.7,
    "treatedAmount": 99.4,
    "plannedCost": 941.1,
    "actualCost": 162.1,
    "comment": null,
    "base": {
      "code": "HI",
      "description": "Habitat Installation"
    }
  },
  {
    "activityId": 1050,
    "licenseeActivityId": "LIC-9937",
    "intraAgencyNumber": 400,
    "activityLocation": "Location-21",
    "plannedAmount": 19.2,
    "treatedAmount": 33.5,
    "plannedCost": 366.1,
    "actualCost": 501.3,
    "comment": null,
    "base": {
      "code": "RD",
      "description": "Roads"
    }
  },
  {
    "activityId": 1051,
    "licenseeActivityId": "LIC-1939",
    "intraAgencyNumber": 176,
    "activityLocation": "Location-43",
    "plannedAmount": 67.4,
    "treatedAmount": 37.0,
    "plannedCost": 821.0,
    "actualCost": 711.5,
    "comment": null,
    "plantingSpecification": {
      "totalPlanting": 458,
      "totalSpecies": 3,
      "species": [
        {
          "speciesType": {
            "code": "S1",
            "description": "Species A"
          },
          "numberPlanted": 300,
          "numberBeyondTransferLimit": 50,
          "cbst": true,
          "lot": 60,
          "requestId": 56655,
          "bidPricePerTree": 1000,
        }
      ]
    },
    "base": {
      "code": "PL",
      "description": "Planting"
    }
  },
  {
    "activityId": 1054,
    "licenseeActivityId": "LIC-2063",
    "intraAgencyNumber": 663,
    "activityLocation": "Location-35",
    "plannedAmount": 36.1,
    "treatedAmount": 30.7,
    "plannedCost": 241.8,
    "actualCost": 738.6,
    "comment": null,
    "directSeedingSpecification": {
      "totalPlanting": 458,
      "totalSpecies": 3,
      "species": [
        {
          "speciesType": {
            "code": "S1",
            "description": "Species A"
          },
          "numberPlanted": 300,
          "numberBeyondTransferLimit": 50,
          "cbst": true,
          "lot": 60
        }
      ]
    },
    "base": {
      "code": "DS",
      "description": "Direct Seeding"
    }
  },
  {
    "activityId": 1047,
    "licenseeActivityId": "LIC-3986",
    "intraAgencyNumber": 930,
    "activityLocation": "Location-9",
    "plannedAmount": 15.7,
    "treatedAmount": 77.8,
    "plannedCost": 511.9,
    "actualCost": 364.1,
    "comment": "Survived a deer invasion. Barely.",
    "base": {
      "code": "BR",
      "description": "Brushing"
    }
  },
  {
    "activityId": 1066,
    "licenseeActivityId": "LIC-8780",
    "intraAgencyNumber": 935,
    "activityLocation": "Location-2",
    "plannedAmount": 43.2,
    "treatedAmount": 67.7,
    "plannedCost": 981.9,
    "actualCost": 625.3,
    "comment": null,
    "base": {
      "code": "PC",
      "description": "Pest Control"
    }
  },
  {
    "activityId": 1073,
    "licenseeActivityId": "LIC-3252",
    "intraAgencyNumber": 105,
    "activityLocation": "Location-36",
    "plannedAmount": 98.0,
    "treatedAmount": 16.6,
    "plannedCost": 687.6,
    "actualCost": 892.6,
    "comment": null,
    "base": {
      "code": "RD",
      "description": "Roads"
    }
  },
  {
    "activityId": 1067,
    "licenseeActivityId": "LIC-8871",
    "intraAgencyNumber": 274,
    "activityLocation": "Location-34",
    "plannedAmount": 66.6,
    "treatedAmount": 62.4,
    "plannedCost": 857.1,
    "actualCost": 233.0,
    "comment": null,
    "base": {
      "code": "SX",
      "description": "Silviculture Trials"
    }
  },
  {
    "activityId": 1083,
    "licenseeActivityId": "LIC-7887",
    "intraAgencyNumber": 483,
    "activityLocation": "Location-25",
    "plannedAmount": 50.4,
    "treatedAmount": 91.8,
    "plannedCost": 439.7,
    "actualCost": 172.2,
    "comment": "Survived a deer invasion. Barely.",
    "base": {
      "code": "RC",
      "description": "Recreation"
    }
  },
  {
    "activityId": 1053,
    "licenseeActivityId": "LIC-8311",
    "intraAgencyNumber": 319,
    "activityLocation": "Location-40",
    "plannedAmount": 35.7,
    "treatedAmount": 91.7,
    "plannedCost": 542.1,
    "actualCost": 210.4,
    "comment": null,
    "directSeedingSpecification": {
      "totalPlanting": 137,
      "totalSpecies": 2,
      "species": [
        {
          "speciesType": {
            "code": "S1",
            "description": "Species A"
          },
          "numberPlanted": 300,
          "numberBeyondTransferLimit": 50,
          "cbst": true,
          "lot": 60
        }
      ]
    },
    "base": {
      "code": "DS",
      "description": "Direct Seeding"
    }
  },
  {
    "activityId": 1088,
    "licenseeActivityId": "LIC-7821",
    "intraAgencyNumber": 518,
    "activityLocation": "Location-10",
    "plannedAmount": 37.0,
    "treatedAmount": 19.2,
    "plannedCost": 584.1,
    "actualCost": 316.9,
    "comment": null,
    "directSeedingSpecification": {
      "totalPlanting": 215,
      "totalSpecies": 2,
      "species": [
        {
          "speciesType": {
            "code": "S1",
            "description": "Species A"
          },
          "numberPlanted": 300,
          "numberBeyondTransferLimit": 50,
          "cbst": true,
          "lot": 60
        }
      ]
    },
    "base": {
      "code": "DS",
      "description": "Direct Seeding"
    }
  },
  {
    "activityId": 1075,
    "licenseeActivityId": "LIC-5742",
    "intraAgencyNumber": 997,
    "activityLocation": "Location-3",
    "plannedAmount": 10.8,
    "treatedAmount": 57.2,
    "plannedCost": 196.6,
    "actualCost": 984.7,
    "comment": "Survived a deer invasion. Barely.",
    "directSeedingSpecification": {
      "totalPlanting": 245,
      "totalSpecies": 2,
      "species": [
        {
          "speciesType": {
            "code": "S1",
            "description": "Species A"
          },
          "numberPlanted": 300,
          "numberBeyondTransferLimit": 50,
          "cbst": true,
          "lot": 60
        }
      ]
    },
    "base": {
      "code": "DS",
      "description": "Direct Seeding"
    }
  },
  {
    "activityId": 1046,
    "licenseeActivityId": "LIC-4950",
    "intraAgencyNumber": 850,
    "activityLocation": "Location-43",
    "plannedAmount": 66.5,
    "treatedAmount": 77.4,
    "plannedCost": 761.7,
    "actualCost": 399.3,
    "comment": null,
    "base": {
      "code": "SX",
      "description": "Silviculture Trials"
    }
  },
  {
    "activityId": 1048,
    "licenseeActivityId": "LIC-5150",
    "intraAgencyNumber": 137,
    "activityLocation": "Location-49",
    "plannedAmount": 68.2,
    "treatedAmount": 29.3,
    "plannedCost": 875.9,
    "actualCost": 143.0,
    "comment": null,
    "base": {
      "code": "BR",
      "description": "Brushing"
    }
  },
  {
    "activityId": 1056,
    "licenseeActivityId": "LIC-3053",
    "intraAgencyNumber": 424,
    "activityLocation": "Location-42",
    "plannedAmount": 16.7,
    "treatedAmount": 47.1,
    "plannedCost": 565.6,
    "actualCost": 215.5,
    "comment": null,
    "base": {
      "code": "HI",
      "description": "Habitat Installation"
    }
  },
  {
    "activityId": 1077,
    "licenseeActivityId": "LIC-6372",
    "intraAgencyNumber": 188,
    "activityLocation": "Location-26",
    "plannedAmount": 70.2,
    "treatedAmount": 19.3,
    "plannedCost": 226.6,
    "actualCost": 391.2,
    "comment": null,
    "base": {
      "code": "HI",
      "description": "Habitat Installation"
    }
  },
  {
    "activityId": 1057,
    "licenseeActivityId": "LIC-3507",
    "intraAgencyNumber": 411,
    "activityLocation": "Location-11",
    "plannedAmount": 24.6,
    "treatedAmount": 93.5,
    "plannedCost": 302.9,
    "actualCost": 952.7,
    "comment": null,
    "sitePrepSpecification": {
      "targetPreparedSpotPerHa": 56.7
    },
    "base": {
      "code": "SP",
      "description": "Site Preparation"
    }
  },
  {
    "activityId": 1090,
    "licenseeActivityId": "LIC-4555",
    "intraAgencyNumber": 590,
    "activityLocation": "Location-14",
    "plannedAmount": 88.3,
    "treatedAmount": 34.0,
    "plannedCost": 961.3,
    "actualCost": 113.8,
    "comment": "Survived a deer invasion. Barely.",
    "base": {
      "code": "CC",
      "description": "Cone Collection"
    }
  },
  {
    "activityId": 1049,
    "licenseeActivityId": "LIC-8204",
    "intraAgencyNumber": 495,
    "activityLocation": "Location-22",
    "plannedAmount": 97.6,
    "treatedAmount": 50.1,
    "plannedCost": 547.8,
    "actualCost": 931.9,
    "comment": null,
    "base": {
      "code": "SX",
      "description": "Silviculture Trials"
    }
  },
  {
    "activityId": 1071,
    "licenseeActivityId": "LIC-6654",
    "intraAgencyNumber": 891,
    "activityLocation": "Location-10",
    "plannedAmount": 36.1,
    "treatedAmount": 29.5,
    "plannedCost": 742.7,
    "actualCost": 411.7,
    "comment": null,
    "sitePrepSpecification": {
      "targetPreparedSpotPerHa": 49.8
    },
    "base": {
      "code": "SP",
      "description": "Site Preparation"
    }
  },
  {
    "activityId": 1072,
    "licenseeActivityId": "LIC-5450",
    "intraAgencyNumber": 240,
    "activityLocation": "Location-7",
    "plannedAmount": 65.4,
    "treatedAmount": 59.1,
    "plannedCost": 318.2,
    "actualCost": 702.9,
    "comment": "Survived a deer invasion. Barely.",
    "base": {
      "code": "HC",
      "description": "Habitat Connectivity"
    }
  },
  {
    "activityId": 1078,
    "licenseeActivityId": "LIC-1187",
    "intraAgencyNumber": 367,
    "activityLocation": "Location-42",
    "plannedAmount": 42.4,
    "treatedAmount": 24.4,
    "plannedCost": 701.0,
    "actualCost": 518.9,
    "comment": null,
    "base": {
      "code": "FE",
      "description": "Fertilization"
    }
  }
];
