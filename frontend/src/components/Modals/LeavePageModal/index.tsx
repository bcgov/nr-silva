import { Button, Column, Grid, Modal, Stack } from '@carbon/react';
import ModalHead from '@/components/Modals/ModalHead';

interface LeavePageModalProps {
  open: boolean;
  onRequestClose: () => void;
  onLeave: () => void;
  onStay: () => void;
}

const LeavePageModal = ({ open, onRequestClose, onLeave, onStay }: LeavePageModalProps) => (
  <Modal
    passiveModal
    danger
    open={open}
    modalHeading={<ModalHead title="Unsaved changes" helperTop="Create new opening" />}
    onRequestClose={onRequestClose}
    className="default-modal"
    preventCloseOnClickOutside
    size="sm"
  >
    <Grid>
      <Column sm={4} md={8} lg={16}>
        <p className="cancel-content">
          If you leave this page, all information entered on this form will be lost.
        </p>
      </Column>
      <Column sm={4} md={8} lg={16}>
        <Stack orientation="horizontal" gap={2} className="default-equal-split-stack">
          <Button className="modal-button" kind="secondary" onClick={onStay}>
            Stay on this page
          </Button>
          <Button className="modal-button" kind="danger" onClick={onLeave}>
            Leave page
          </Button>
        </Stack>
      </Column>
    </Grid>
  </Modal>
);

export default LeavePageModal;
