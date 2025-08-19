import MapPreview from "@/components/MapPreview";
import { Column } from "@carbon/react";


type DataFormProps = {
  isReview: boolean;
  form: CreateOpeningFormType;
  setForm: React.Dispatch<React.SetStateAction<CreateOpeningFormType>>;
}

const DataForm = ({ isReview, form }: DataFormProps) => {

  return (
    <Column sm={4} md={8} lg={16}>
      {form.geojson ? <MapPreview geojson={form.geojson} /> : null}
    </Column>
  )

}

export default DataForm;
