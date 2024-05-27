import { env } from '../env';

const serverHost = env.VITE_BACKEND_URL;

const ApiConfig = {
  baseMapLayer: `${serverHost}/api/map-view/base-map-layer`,
};

export default ApiConfig;
