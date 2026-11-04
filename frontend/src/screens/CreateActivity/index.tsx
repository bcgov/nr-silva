import { useCallback, useEffect, useRef, useState } from "react";
import { Button, Column, Grid, InlineNotification, Loading, Modal } from '@carbon/react'
import { generatePath, useBlocker, useNavigate, useParams } from "react-router-dom";
import { getAddActivityCrumbs, DefaultActivityForm } from "./constants";
import PageTitle from "@/components/PageTitle";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import API from "@/services/API";
import { PLACE_HOLDER } from "@/constants";
import AddActivityForm from "@/components/AddActivityForm";
import LeavePageModal from "@/components/Modals/LeavePageModal";
import { ApiError } from "@/services/OpenApi";
import { scrollToSection } from "@/utils/InputUtils";
import { showToast } from "@/utils/Toast";
import { OPENING_DETAILS_PATH } from "@/routes/paths";
import { DEEP_LINK_PARAMS } from "@/constants/deepLinkConstants";
import { ActivityFormType } from "./definitions";
import { buildCreateActivityPayload, getApiErrorMessage, hasActivityFormData, validateActivityForm } from "./utils";

import "./styles.scss";

const CreateActivity = () => {
  const param = useParams();
  const openingId = param.openingId;
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const [form, setForm] = useState<ActivityFormType>(() => structuredClone(DefaultActivityForm));
  const [uploadError, setUploadError] = useState<string>();
  const [warnText, setWarnText] = useState<string>();
  const [pendingStatus, setPendingStatus] = useState<ActivityFormType['status']>();
  const [isStatusSwitchModalOpen, setIsStatusSwitchModalOpen] = useState(false);
  const [isLeavePageModalOpen, setIsLeavePageModalOpen] = useState(false);
  const [isNavigationBlocked, setIsNavigationBlocked] = useState(false);
  const bypassBlockerRef = useRef(false);
  const blocker = useBlocker(useCallback(() => isNavigationBlocked && !bypassBlockerRef.current, [isNavigationBlocked]));

  const openingDetailsActivitiesPath = openingId
    ? `${generatePath(OPENING_DETAILS_PATH, { openingId })}?tab=activities`
    : OPENING_DETAILS_PATH;


  const isFilePendingValidation = Boolean(form.file?.value && !form.file?.validatedObj);

  useEffect(() => {
    document.title = `Add Activity - Silva`;
    return () => {
      document.title = "Silva";
    };
  }, []);

  useEffect(() => {
    setIsNavigationBlocked(Boolean(form.status?.value) || hasActivityFormData(form));
  }, [form]);

  const crumbs = openingId ? getAddActivityCrumbs(openingId) : [];

  const openingDetailsTombstoneQuery = useQuery({
    queryKey: ["openings", openingId, "tombstone"],
    queryFn: () => API.OpeningEndpointService.getOpeningTombstone(Number(openingId)),
    enabled: !!openingId,
    refetchOnMount: "always",
  });

  const fileMutation = useMutation({
    mutationFn: (file: Blob) => API.ActivityEndpointService.uploadActivitySpatial(Number(openingId), { file }),
    onSuccess: (data) => {
      setUploadError(undefined);
      setForm((f) => ({
        ...f,
        file: f.file ? { ...f.file, validatedObj: data, isInvalid: false } : f.file,
      }));
    },
    onError: (err) => {
      const status = err instanceof ApiError ? err.status : 0;
      if (status === 401) return;
      setForm((f) => ({
        ...f,
        file: f.file ? { ...f.file, validatedObj: undefined, isInvalid: true } : f.file,
      }));
      setUploadError(getApiErrorMessage(err, 'File upload failed. Please try again.'));
    },
  });

  const createMutation = useMutation({
    mutationFn: () =>
      API.ActivityEndpointService.createActivity(Number(openingId), {
        data: buildCreateActivityPayload(form),
        file: form.file?.value,
      }),
    onSuccess: (res) => {
      bypassBlockerRef.current = true;
      showToast.success(`Activity ${res.activityId} created`);
      void queryClient.invalidateQueries({ queryKey: ['opening', openingId, 'activities'] });
      const deepLink = res.activityId
        ? `${openingDetailsActivitiesPath}&${DEEP_LINK_PARAMS.activityId}=${res.activityId}`
        : openingDetailsActivitiesPath;
      void navigate(deepLink);
    },
    onError: (err) => {
      const status = err instanceof ApiError ? err.status : 0;
      if (status === 401) return;

      const message = getApiErrorMessage(err, 'Creation failed. Please try again.');
      let userMessage = message;
      if (status === 400) {
        userMessage = `Form validation failed: ${message}`;
      } else if (status === 403) {
        userMessage = 'Not authorized to create an activity for this opening.';
      } else if (status === 404) {
        userMessage = `Opening not found: ${message}`;
      } else if (status === 422) {
        userMessage = `File rejected: ${message}`;
      } else if (status >= 500) {
        userMessage = 'Server error. Please try again or contact support.';
      }

      setWarnText(userMessage);
      scrollToSection('add-activity-title-col');
    },
  });

  const handleStatusChangeAttempt = (newStatus: string) => {
    const nextStatus = newStatus as NonNullable<ActivityFormType['status']>['value'];
    if (form.status?.value === nextStatus) return;

    if (!hasActivityFormData(form)) {
      setForm((prev) => ({ ...prev, status: { ...prev.status, value: nextStatus } }));
      return;
    }

    setPendingStatus({ value: nextStatus });
    setIsStatusSwitchModalOpen(true);
  };

  const handleConfirmStatusSwitch = () => {
    const nextStatus = pendingStatus?.value;
    setForm({ ...structuredClone(DefaultActivityForm), status: { ...DefaultActivityForm.status, value: nextStatus } });
    setIsStatusSwitchModalOpen(false);
    setPendingStatus(undefined);
  };

  const handleCancelStatusSwitch = () => {
    setIsStatusSwitchModalOpen(false);
    setPendingStatus(undefined);
  };

  const handleCancel = () => {
    if (!isNavigationBlocked) {
      bypassBlockerRef.current = true;
      void navigate(openingDetailsActivitiesPath);
      return;
    }
    setIsLeavePageModalOpen(true);
  };

  const handleLeaveConfirm = () => {
    bypassBlockerRef.current = true;
    setIsLeavePageModalOpen(false);

    if (blocker.state === 'blocked') {
      blocker.proceed?.();
      return;
    }
    void navigate(openingDetailsActivitiesPath);
  };

  const handleStay = () => {
    setIsLeavePageModalOpen(false);
    blocker.reset?.();
  };

  const isSubmitDisabled = fileMutation.isPending || createMutation.isPending || isFilePendingValidation;

  const handleSubmit = () => {
    if (isSubmitDisabled) return;

    const { isValid, form: validated } = validateActivityForm(form);
    if (!isValid) {
      setForm(validated);
      const firstInvalidId = Object.values(validated).find((field) => field?.isInvalid)?.id;
      scrollToSection(firstInvalidId);
      return;
    }
    setWarnText(undefined);
    createMutation.mutate();
  };

  return (
    <Grid className="default-grid add-activity-container-grid">
      <Column id="add-activity-title-col" sm={4} md={8} lg={16}>
        <PageTitle
          title={`Add Activity - Opening ${openingId}`}
          subtitle={
            openingDetailsTombstoneQuery.data ?
              `File ID ${openingDetailsTombstoneQuery.data.tombstone.fileId ?? PLACE_HOLDER}, Cut block ${openingDetailsTombstoneQuery.data.tombstone.cutBlockID ?? PLACE_HOLDER}`
              : undefined
          }
          breadCrumbs={crumbs}
        />
      </Column>

      {
        warnText
          ? (
            <Column sm={4} md={8} lg={16}>
              <InlineNotification lowContrast kind="warning" subtitle={warnText} onCloseButtonClick={() => setWarnText(undefined)} />
            </Column>
          )
          : null
      }

      <Column sm={4} md={8} lg={16} xlg={12} max={10}>
        <AddActivityForm
          form={form}
          setForm={setForm}
          uploadError={uploadError}
          onUploadErrorDismiss={() => setUploadError(undefined)}
          onFileAdded={(f) => { setUploadError(undefined); fileMutation.mutate(f); }}
          isUploading={fileMutation.isPending}
          onStatusChangeAttempt={handleStatusChangeAttempt}
        />
      </Column>

      <Column sm={4} md={8} lg={16} xlg={12} max={10}>
        <Grid className="default-button-grid">
          <Column sm={4} md={4} lg={6} max={4} className="default-button-grid-column">
            <Button className="default-button" kind="secondary" onClick={handleCancel}>
              Cancel
            </Button>
          </Column>
          <Column sm={4} md={4} lg={6} max={4} className="default-button-grid-column">
            <Button
              className="default-button"
              kind="primary"
              onClick={handleSubmit}
              renderIcon={createMutation.isPending ? Loading : undefined}
            >
              Add activity
            </Button>
          </Column>
        </Grid>
      </Column>

      <LeavePageModal
        open={isLeavePageModalOpen || blocker.state === 'blocked'}
        helperTop="Add activity"
        onRequestClose={handleStay}
        onLeave={handleLeaveConfirm}
        onStay={handleStay}
      />

      <Modal
        open={isStatusSwitchModalOpen}
        className="default-confirm-switch-modal"
        modalHeading="Switch activity status?"
        primaryButtonText="Proceed"
        secondaryButtonText="Cancel"
        onRequestSubmit={handleConfirmStatusSwitch}
        onRequestClose={handleCancelStatusSwitch}
        onSecondarySubmit={handleCancelStatusSwitch}
        size="sm"
      >
        <p>
          Switching activity status will clear all information entered on this form. Are you sure
          you want to continue?
        </p>
      </Modal>
    </Grid>
  );
};

export default CreateActivity;

