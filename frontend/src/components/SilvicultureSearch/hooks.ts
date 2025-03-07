import { useSearchParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { SilvicultureSearchParams } from "./definitions";
import { DATE_TYPE_LIST } from "../../constants";

const useSilvicultureSearchParams = () => {
  const [searchParams] = useSearchParams();
  const [params, setParams] = useState<SilvicultureSearchParams | null>(null);

  useEffect(() => {
    const tab = searchParams.get("tab") as "openings" | null;
    const dateType = searchParams.get("dateType") as SilvicultureSearchParams["dateType"];
    const updateDateStart = searchParams.get("updateDateStart") || "";
    const updateDateEnd = searchParams.get("updateDateEnd") || "";

    const orgUnit = searchParams.getAll("orgUnit"); // Handles array values
    const status = searchParams.getAll("status"); // Handles array values

    if (!tab || !dateType) {
      return;
    }

    if (!DATE_TYPE_LIST.some((d) => d.code === dateType)) {
      console.warn(`Invalid dateType: ${dateType}`);
      return;
    }

    const parsedParams: SilvicultureSearchParams = {
      tab,
      dateType,
      updateDateStart,
      updateDateEnd,
      orgUnit: orgUnit.length > 0 ? orgUnit : undefined,
      status: status.length > 0 ? status : undefined,
    };

    setParams(parsedParams);
  }, [searchParams]);

  return params;
};

export default useSilvicultureSearchParams;
