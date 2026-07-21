import { FavouriteCardProps } from "@/components/FavouriteCard/definitions";
import { env } from "@/env";

export const FavouriteCardsConfig: FavouriteCardProps[] = [
  {
    index: 0,
    title: 'Openings search',
    link: '/openings-search',
    icon: 'MapBoundaryVegetation'
  },
  {
    index: 1,
    title: 'Create opening',
    link: '/',
    icon: 'MapBoundary',
    opensModal: true,
    hidden: env.VITE_ZONE === 'prod' && env.VITE_DEPLOYMENT_MODEL !== 'postgres'
  }
]
