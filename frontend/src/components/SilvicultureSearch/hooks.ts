import { useSearchParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { SilvicultureSearchParams } from "./definitions";
import { DATE_TYPE_LIST } from "../../constants";
import { DATE_TYPES } from "../../types/DateTypes";

/**
 * Parses a comma-separated string into an array of trimmed, non-empty strings.
 *
 * @param {string} param - The comma-separated string to parse.
 * @returns {string[]} An array of trimmed strings with empty entries removed.
 *
 * @example
 * parseCommaSeparated("A, B, , C") // returns ["A", "B", "C"]
 */
const parseCommaSeparated = (param: string) => {
  return param.split(",").map((s) => s.trim()).filter(Boolean);
};

const useSilvicultureSearchParams = () => {
  const [searchParams] = useSearchParams();
  const [params, setParams] = useState<SilvicultureSearchParams | null>(null);

  useEffect(() => {
    const tab = searchParams.get("tab") as "openings" | null;
    const dateType = searchParams.get("dateType") as DATE_TYPES;
    const dateStart = searchParams.get("dateStart") || "";
    const dateEnd = searchParams.get("dateEnd") || "";

    const orgUnitRaw = searchParams.get("orgUnit");
    const statusRaw = searchParams.get("status");

    const orgUnit = orgUnitRaw ? parseCommaSeparated(orgUnitRaw) : [];
    const status = statusRaw ? parseCommaSeparated(statusRaw) : [];


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
      dateStart,
      dateEnd,
      orgUnit: orgUnit.length > 0 ? orgUnit : undefined,
      status: status.length > 0 ? status : undefined,
    };

    setParams(parsedParams);
  }, [searchParams]);

  return params;
};

export default useSilvicultureSearchParams;
