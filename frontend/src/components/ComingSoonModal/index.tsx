import React, {useEffect, useState} from "react";
import { Modal } from "@carbon/react";
interface IComingSoonModal {
  openingDetails: string;
  setOpeningDetails: (openingDetails: string) => void;
}

const ComingSoonModal: React.FC<IComingSoonModal> = ({
  openingDetails,
  setOpeningDetails
}) => {

  const [open, setOpen] = useState<boolean>(false);
  const [details, setDetails] = useState<string>(openingDetails || "");

  useEffect(() => {
    setDetails(openingDetails);
    if(openingDetails) {
      setOpen(true);
    }
  }, [openingDetails]);

  const close = () => {
    setOpen(false);
    setOpeningDetails("");
    setDetails("");
  }

  return (
    <Modal
      open={open}
      onRequestClose={() => close()}
      passiveModal
      modalHeading="Coming Soon"
      modalLabel={`Opening ID: ${details}`}
    >
      <p className="modal-subtext">An opening details page is in development.</p> <br/>
      <p className="modal-subtext">
        {` Until it's available, search for ${details} in RESULTS to view the opening details.`}
      </p>
    </Modal>
  );
};
export default ComingSoonModal;
