import { Stack } from "@carbon/react";
import { PLACE_HOLDER } from "@/constants";

import './styles.scss';

type props = {
  label: string;
  value: string | number | null;
  required?: boolean;
}

const DisplayField = ({ label, value, required }: props) => {
  return (
    <Stack gap={2} className="display-field-container">
      <span className="display-field-label">
        {required ? <span className="display-field-required">*</span> : null}
        {label}
      </span>
      <span className="display-field-value">{value ?? PLACE_HOLDER}</span>
    </Stack>
  );
};

export default DisplayField;
