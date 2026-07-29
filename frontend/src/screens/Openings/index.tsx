import React, { useEffect, useState, lazy, Suspense } from "react";
import { env } from "@/env";
import { Add, ArrowRight } from "@carbon/icons-react";
import { Button, Column, Grid, TextInput, Tabs, TabList, Tab, TabPanel, TabPanels } from "@carbon/react";
import { useNavigate, useSearchParams } from "react-router-dom";
import PageTitle from "@/components/PageTitle";
import { sanitizeDigits } from "@/utils/InputUtils";
import { useModal } from "@/contexts/ModalContext";
import TableSkeleton from "@/components/TableSkeleton";
import { recentOpeningsHeaders } from "@/components/RecentOpenings/constants";

import { OpeningsTabs } from "./constants";
import './styles.scss';

const RecentOpenings = lazy(() => import("@/components/RecentOpenings"));
const MyOpenings = lazy(() => import("@/components/MyOpenings"));

const Openings = () => {
  const navigate = useNavigate();
  const { openModal } = useModal();
  const [openingId, setOpeningId] = useState<string>("");
  const [searchParams, setSearchParams] = useSearchParams();

  useEffect(() => {
    document.title = `Openings - Silva`;
    return () => {
      document.title = "Silva";
    };
  }, []);

  const [activeTab, setActiveTab] = useState<number>(() => {
    const tabName = (searchParams.get("tab") ?? "recent") as typeof OpeningsTabs[number];
    const index = OpeningsTabs.indexOf(tabName);
    return Math.max(0, index);
  });

  // Re-sync activeTab when URL changes (e.g., browser back/forward)
  useEffect(() => {
    const tabName = (searchParams.get("tab") ?? "recent") as typeof OpeningsTabs[number];
    const index = OpeningsTabs.indexOf(tabName);
    setActiveTab(Math.max(0, index));
  }, [searchParams]);

  const isActive = (index: number) => activeTab === index;

  const handleTabChange = (selectedTabIndex: number) => {
    setActiveTab(selectedTabIndex);
    const tabName = OpeningsTabs[selectedTabIndex];
    const newSearchParams = new URLSearchParams(searchParams.toString());
    newSearchParams.set("tab", String(tabName));
    setSearchParams(newSearchParams, { replace: true });
  };

  const handleAddNewOpening = () => {
    openModal('CREATE_OPENING');
  };

  const handleNavById = () => {
    if (openingId.length > 0) {
      navigate(`/openings/${openingId}`)
    }
  }

  const handleOpeningIdChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setOpeningId(sanitizeDigits(e.target.value ?? ''));
  };

  const handleOpeningIdPaste = (e: React.ClipboardEvent<HTMLInputElement>) => {
    e.preventDefault();
    const paste = e.clipboardData.getData('text') ?? '';
    const digitsOnly = sanitizeDigits(paste);
    if (digitsOnly.length) {
      setOpeningId(digitsOnly);
    }
  };

  return (
    <Grid className="default-grid">
      <Column sm={4} md={8} lg={16}>
        <PageTitle title="Openings" />
      </Column>

      {/* Button subgrid */}
      {
        env.VITE_ZONE !== 'prod' && env.VITE_DEPLOYMENT_MODEL === 'postgres'
          ? (
            <Column sm={4} md={8} lg={16}>
              <Grid>
                <Column sm={4} md={8} lg={6} max={4}>
                  <Button renderIcon={Add} onClick={handleAddNewOpening}>Create new</Button>
                </Column>
              </Grid>
            </Column>
          )
          : null
      }

      <Column sm={4} md={8} lg={16}>
        <div className="opening-id-nav-container">
          <TextInput
            id="opening-id-input"
            name="opening-id"
            labelText="Opening ID"
            hideLabel
            placeholder="View Opening by ID"
            value={openingId}
            onChange={handleOpeningIdChange}
            onPaste={handleOpeningIdPaste}
            onKeyDown={(e) => {
              if (e.key === 'Enter') {
                handleNavById();
              }
            }}
          />
          <Button
            onClick={handleNavById}
            size="md"
            aria-label="Navigate to opening"
            renderIcon={ArrowRight}
          >
            Go
          </Button>
        </div>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <Tabs
          selectedIndex={activeTab}
          onChange={(state) => handleTabChange(state.selectedIndex)}
        >
          <TabList aria-label="List of opening tabs">
            <Tab>Recent openings</Tab>
            <Tab>My openings</Tab>
          </TabList>
          <TabPanels>
            <TabPanel className="openings-tabs">
              {isActive(0) ? (
                <Suspense fallback={<TableSkeleton headers={recentOpeningsHeaders} showToolbar={false} showHeader={false} />}>
                  <RecentOpenings defaultMapOpen />
                </Suspense>
              ) : null}
            </TabPanel>
            <TabPanel className="openings-tabs">
              {isActive(1) ? (
                <Suspense fallback={<TableSkeleton headers={recentOpeningsHeaders} showToolbar={false} showHeader={false} />}>
                  <MyOpenings defaultMapOpen={false} />
                </Suspense>
              ) : null}
            </TabPanel>
          </TabPanels>
        </Tabs>
      </Column>
    </Grid>
  )
}

export default Openings;
