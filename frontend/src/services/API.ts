import axios from "axios";
import { TEN_SECONDS } from "@/constants/TimeUnits";
import { env } from "@/env";
import { getAuthIdToken } from "@/services/AuthService";
import { OpenAPI } from "./OpenApi/core/OpenAPI";

// All service imports (static classes)
import { CodesEndpointService } from "./OpenApi/services/CodesEndpointService";
import { ForestClientEndpointService } from "./OpenApi/services/ForestClientEndpointService";
import { OpeningEndpointService } from "./OpenApi/services/OpeningEndpointService";
import { OpeningFavoriteEndpointService } from "./OpenApi/services/OpeningFavoriteEndpointService";
import { OpeningMapsEndpointService } from "./OpenApi/services/OpeningMapsEndpointService";
import { UserActionsEndpointService } from "./OpenApi/services/UserActionsEndpointService";
import { UserRecentOpeningEndpointService } from "./OpenApi/services/UserRecentOpeningEndpointService";

// Setup global axios defaults
axios.defaults.headers.common["Content-Type"] = "application/json";
axios.defaults.timeout = TEN_SECONDS;

// Clean baseURL
let API_BASE_URL = env.VITE_BACKEND_URL ?? "http://localhost:8080";
if (API_BASE_URL.endsWith("/api")) {
    API_BASE_URL = API_BASE_URL.slice(0, -4);
}

// Configure global OpenAPI settings
OpenAPI.BASE = API_BASE_URL;
OpenAPI.WITH_CREDENTIALS = true;
OpenAPI.HEADERS = {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": window.location.origin,
};

// Provide token dynamically before each request
OpenAPI.TOKEN = async () => {
    return getAuthIdToken() ?? '';
};

// Static service class map (no instantiation)
const serviceConstructors = {
    CodesEndpointService,
    ForestClientEndpointService,
    OpeningEndpointService,
    OpeningFavoriteEndpointService,
    OpeningMapsEndpointService,
    UserActionsEndpointService,
    UserRecentOpeningEndpointService,
} as const;

type SilvaApiType = {
    [K in keyof typeof serviceConstructors]: typeof serviceConstructors[K];
};

const API: SilvaApiType = serviceConstructors;

export default API;
