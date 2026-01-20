import { FavouriteCardProps } from "@/components/FavouriteCard/definitions";
import { env } from "@/env";

export const FavouriteCardsConfig: FavouriteCardProps[] = [
  {
    index: 0,
    title: 'Openings search',
    link: '/openings-search',
    icon: 'IbmSoftwareWatsonxDataAnalyzeAndProcess'
  },
  {
    index: 1,
    title: 'Create opening',
    link: '/',
    icon: 'MapBoundary',
    opensModal: true,
    hidden: env.VITE_ZONE?.toLowerCase() !== 'test' && env.VITE_ZONE?.toLowerCase() !== 'dev' && !Number.isInteger(Number(env.VITE_ZONE))
  }
]
