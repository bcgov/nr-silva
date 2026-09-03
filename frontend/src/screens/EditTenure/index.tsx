import { useCallback, useEffect, useRef, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { Button, Column, Grid, InlineNotification, InlineLoading, Tag } from '@carbon/react';
import { Save } from '@carbon/icons-react';
import { useBlocker, useNavigate, useParams } from 'react-router-dom';
import EmptySection from '@/components/EmptySection';
import PageTitle from '@/components/PageTitle';
import LeavePageModal from '@/components/Modals/LeavePageModal';
import TenureListInput from '@/components/TenureListInput';
import API from '@/services/API';
import {
  ApiError,
  TenureUpdateItemDto,
  TenureUpdateValidationResponseDto,
  TenureValidationResponseDto,
} from '@/services/OpenApi';
import { TenureFieldErrors, validateTenureList } from '@/utils/TenureUtils';
import { showToast } from '@/utils/Toast';

import { EDIT_TENURE_TOAST_MESSAGES, getEditTenureCrumbs } from './constants';
import {
  buildTenureUpdatePayload,
  EditTenureItem,
  getApiErrorMessage,
  hasTenureChanges,
  toEditTenure,
} from './utils';

const EditTenure = () => {
  const { openingId: openingIdParam } = useParams();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const openingId = Number(openingIdParam);
  const initialTenuresRef = useRef<EditTenureItem[] | null>(null);
  const bypassBlockerRef = useRef(false);
  const [tenures, setTenures] = useState<EditTenureItem[]>([]);
  const [isDirty, setIsDirty] = useState(false);
  const [isLeavePageModalOpen, setIsLeavePageModalOpen] = useState(false);
  const [fieldErrors, setFieldErrors] = useState<TenureFieldErrors[]>();
  const [showNoPrimaryError, setShowNoPrimaryError] = useState(false);
  const [validationResult, setValidationResult] = useState<TenureValidationResponseDto | null>(null);
  const [formError, setFormError] = useState<string>();
  const blocker = useBlocker(useCallback(() => isDirty && !bypassBlockerRef.current, [isDirty]));

  useEffect(() => {
    document.title = 'Edit Tenure - Silva';
    return () => {
      document.title = 'Silva';
    };
  }, []);

  const tenureQuery = useQuery({
    queryKey: ['opening', openingId, 'tenure', { all: true }],
    queryFn: () => API.OpeningEndpointService.getTenures(openingId, undefined, true),
    enabled: !!openingIdParam,
    refetchOnMount: 'always'
  });

  const tombstoneQuery = useQuery({
    queryKey: ["openings", openingId, "tombstone"],
    queryFn: () => API.OpeningEndpointService.getOpeningTombstone(openingId),
    enabled: !!openingIdParam,
    refetchOnMount: 'always'
  });

  // Hydrates the form and captures an immutable baseline once the current tenures load.
  useEffect(() => {
    if (!tenureQuery.data || tenureQuery.isFetching || initialTenuresRef.current) return;
    const loadedTenures = tenureQuery.data.content.map(toEditTenure);
    initialTenuresRef.current = structuredClone(loadedTenures);
    setTenures(loadedTenures);
    setIsDirty(false);
  }, [tenureQuery.data, tenureQuery.isFetching]);

  // Marks the form dirty only when its current values differ from the loaded baseline.
  useEffect(() => {
    if (!initialTenuresRef.current) return;
    setIsDirty(hasTenureChanges(tenures, initialTenuresRef.current));
  }, [tenures]);

  // Warns before closing or refreshing the browser while there are unsaved changes.
  useEffect(() => {
    if (!isDirty) return;
    const handleBeforeUnload = (event: BeforeUnloadEvent) => {
      event.preventDefault();
    };
    window.addEventListener('beforeunload', handleBeforeUnload);
    return () => window.removeEventListener('beforeunload', handleBeforeUnload);
  }, [isDirty]);

  const crumbs = openingIdParam ? getEditTenureCrumbs(openingIdParam) : [];
  const openingDetailsPath = crumbs[1]?.path ?? '/openings';

  const saveMutation = useMutation({
    mutationFn: (payload: TenureUpdateItemDto[]) =>
      API.TenureEndpointService.updateTenures(
        openingId,
        tombstoneQuery.data?.tombstone.client.clientNumber ?? '',
        payload
      ),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ['opening', openingId, 'tenure'] });
      bypassBlockerRef.current = true;
      showToast.success(EDIT_TENURE_TOAST_MESSAGES.SAVED);
      navigate(openingDetailsPath);
    },
    onError: (error: unknown) => {
      if (error instanceof ApiError && error.status === 422) {
        const response = error.body as TenureUpdateValidationResponseDto;
        setValidationResult(response.tenureValidation ?? null);
        const removalMessages = (response.removalErrors ?? [])
          .map((removalError) => removalError.errorMessage)
          .filter((message): message is string => Boolean(message));
        setFormError(removalMessages.length ? removalMessages.join(' ') : undefined);
        return;
      }
      setFormError(getApiErrorMessage(error, 'Unable to save tenure information. Please try again.'));
    },
  });

  const clearValidation = () => {
    setFieldErrors(undefined);
    setShowNoPrimaryError(false);
    setValidationResult(null);
    setFormError(undefined);
  };

  const handleSave = () => {
    if (!isReady || saveMutation.isPending) return;

    if (!hasTenureChanges(tenures, initialTenuresRef.current ?? [])) {
      bypassBlockerRef.current = true;
      showToast.success(EDIT_TENURE_TOAST_MESSAGES.NO_CHANGES);
      navigate(openingDetailsPath);
      return;
    }

    clearValidation();
    const validation = validateTenureList(tenures);
    setTenures(validation.trimmed);
    if (!validation.isValid) {
      setFieldErrors(validation.errors);
      return;
    }
    if (!validation.hasPrimary) {
      setShowNoPrimaryError(true);
      return;
    }
    saveMutation.mutate(buildTenureUpdatePayload(validation.trimmed, initialTenuresRef.current ?? []));
  };

  const handleCancel = () => {
    if (!isReady) return;

    if (!isDirty) {
      bypassBlockerRef.current = true;
      navigate(openingDetailsPath);
      return;
    }
    setIsLeavePageModalOpen(true);
  };

  const handleLeave = () => {
    bypassBlockerRef.current = true;
    setIsLeavePageModalOpen(false);
    if (blocker.state === 'blocked') {
      blocker.proceed();
      return;
    }
    navigate(openingDetailsPath);
  };

  const handleStay = () => {
    setIsLeavePageModalOpen(false);
    blocker.reset?.();
  };

  if (!openingIdParam) {
    return <EmptySection icon="BreakingChange" title="Opening ID is missing" description="Unable to edit tenure information." />;
  }

  const isLoading = tenureQuery.isFetching || tombstoneQuery.isFetching;
  const hasLoadError = tenureQuery.isError || tombstoneQuery.isError;
  const isOpeningNotFound = tombstoneQuery.error instanceof ApiError && tombstoneQuery.error.status === 404;
  const isReady = !isLoading && !hasLoadError && initialTenuresRef.current !== null;

  return (
    <>
      <Grid className="default-grid">
        <PageTitle
          title="Edit Tenure information"
          subtitle={<Tag type="blue" className="default-tag">Opening ID #{openingId}</Tag>}
          breadCrumbs={crumbs}
        />

        {hasLoadError ? (
          <Column sm={4} md={8} lg={16}>
            <InlineNotification
              kind="error"
              title={isOpeningNotFound ? 'Opening does not exist' : 'Unable to load tenure information'}
              subtitle={
                isOpeningNotFound
                  ? 'The opening you are trying to edit could not be found.'
                  : 'Refresh the page and try again.'
              }
              lowContrast
              hideCloseButton
            />
          </Column>
        ) : null}

        {formError ? (
          <Column sm={4} md={8} lg={16}>
            <InlineNotification
              kind="error"
              title="Unable to save tenure information"
              subtitle={formError}
              lowContrast
              hideCloseButton
              onCloseButtonClick={() => setFormError(undefined)}
            />
          </Column>
        ) : null}

        {showNoPrimaryError ? (
          <Column sm={4} md={8} lg={16}>
            <InlineNotification
              kind="error"
              title="Primary tenure required"
              subtitle="At least one tenure must be set as primary."
              lowContrast
              hideCloseButton
            />
          </Column>
        ) : null}

        <Column sm={4} md={8} lg={16}>
          {!hasLoadError ? (
            <TenureListInput
              tenures={tenures}
              setTenures={setTenures}
              validationResult={validationResult}
              fieldErrors={fieldErrors}
              onTenuresChange={clearValidation}
              loading={isLoading}
              initializeEmptyTenure={false}
            />
          ) : null}
        </Column>

        <Column sm={4} md={8} lg={16}>
          <Grid className="default-button-grid">
            <Column sm={4} md={4} lg={6} max={4} className="default-button-grid-column">
              <Button kind="secondary" onClick={handleCancel}>
                Cancel
              </Button>
            </Column>
            <Column sm={4} md={4} lg={6} max={4} className="default-button-grid-column">
              <Button
                kind="primary"
                onClick={handleSave}
                className={saveMutation.isPending ? 'default-loading-button' : undefined}
                renderIcon={saveMutation.isPending ? InlineLoading : Save}
              >
                Save tenure information
              </Button>
            </Column>
          </Grid>
        </Column>

        <LeavePageModal
          open={isLeavePageModalOpen || blocker.state === 'blocked'}
          helperTop="Edit tenure information"
          onRequestClose={handleStay}
          onLeave={handleLeave}
          onStay={handleStay}
        />
      </Grid>
    </>
  );
};

export default EditTenure;
