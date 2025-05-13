import CodeDescriptionDto from "@/types/CodeDescriptionType";
import {
    OpeningActivityDetail,
} from "@/types/OpeningTypes";

export type ActivityDetailProps = {
    activityDetail?: OpeningActivityDetail;
    base?: CodeDescriptionDto;
    isPlanning?: boolean;
    isComplex?: boolean;
    isLoading?: boolean;
};
