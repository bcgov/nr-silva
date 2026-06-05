import { BreadCrumbType } from "@/types/BreadCrumbTypes";
import { STOCKING_STANDARDS_SEARCH_PATH, STOCKING_STANDARDS_COMMENT_SEARCH_PATH } from "@/routes/paths";

export const SS_COMMENT_SEARCH_BREADCRUMB: BreadCrumbType[] = [
  {
    name: 'Stocking standards search',
    path: STOCKING_STANDARDS_SEARCH_PATH
  },
  {
    name: 'Stocking standards keywords search',
    path: STOCKING_STANDARDS_COMMENT_SEARCH_PATH
  }
]
