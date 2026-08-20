import { env } from "@/env";
import { OpenAPI } from "./OpenApi/core/OpenAPI";
import { getCookie } from "@/utils/CookieUtils";
import { ACCESS_TOKEN_KEY } from "@/constants";

// All service imports (static classes)
import {
    CodesEndpointService,
    ForestClientEndpointService,
    OpeningEndpointService,
    OpeningMapsEndpointService,
    UserActionsEndpointService,
    UserRecentOpeningEndpointService,
    SearchEndpointService,
    OpeningCreateEndpointService,
    TenureEndpointService
} from "./OpenApi";

// Clean baseURL
let API_BASE_URL = env.VITE_BACKEND_URL ?? "http://localhost:8080";
if (API_BASE_URL.endsWith("/api")) {
    API_BASE_URL = API_BASE_URL.slice(0, -4);
}

// Configure global OpenAPI settings
OpenAPI.BASE = API_BASE_URL;
OpenAPI.WITH_CREDENTIALS = false;

// Provide token dynamically before each request
OpenAPI.TOKEN = async () => {
    const storedToken = getCookie(ACCESS_TOKEN_KEY);
    return storedToken ?? '';
};

// Static service class map (no instantiation)
const serviceConstructors = {
    CodesEndpointService,
    ForestClientEndpointService,
    OpeningEndpointService,
    OpeningMapsEndpointService,
    UserActionsEndpointService,
    UserRecentOpeningEndpointService,
    SearchEndpointService,
    OpeningCreateEndpointService,
    TenureEndpointService
} as const;

type SilvaApiType = {
    [K in keyof typeof serviceConstructors]: typeof serviceConstructors[K];
};

const API: SilvaApiType = serviceConstructors;

export default API;
