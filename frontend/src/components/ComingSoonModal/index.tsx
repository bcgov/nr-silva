import { Modal } from "@carbon/react";
interface IComingSoonModal {
  openingDetails: string;
  setOpeningDetails: (openingDetails: string) => void;
}

const ComingSoonModal: React.FC<IComingSoonModal> = ({
  openingDetails,
  setOpeningDetails,
}) => {
  return (
    <Modal
      open={openingDetails.length > 0}
      onRequestClose={() => setOpeningDetails("")}
      passiveModal
      modalHeading="Coming Soon"
      modalLabel={`Opening ID: ${openingDetails}`}
    >
      <p className="modal-subtext">An opening details page is in development.</p> <br/>
      <p className="modal-subtext">
        {` Until it's available, search for ${openingDetails} in RESULTS to view the opening details.`}
      </p>
    </Modal>
  );
};
export default ComingSoonModal;
