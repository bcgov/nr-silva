import { FavouriteCardProps } from "@/components/FavouriteCard/definitions";
import { gatePostgresFeature } from "@/utils/featureFlags";

export const getFavouriteCardsConfig = (): FavouriteCardProps[] => [
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
    hidden: gatePostgresFeature()
  }
]
