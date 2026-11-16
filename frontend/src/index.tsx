window.global ||= window;
import React from "react";
import "./index.css";
import { ClassPrefix } from "@carbon/react";
import App from "./App";
import { ThemePreference } from "./utils/ThemePreference";
import { createRoot } from "react-dom/client";
import { AuthProvider } from "./contexts/AuthProvider";
import ToastProvider from "./contexts/ToastProvider";
import { PreferenceProvider } from "@/contexts/PreferenceProvider";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { queryClientConfig } from "./constants/tanstackConfig";
import { CARBON_CLASS_PREFIX } from "./constants";

const queryClient = new QueryClient(queryClientConfig);
const container: HTMLElement | null = document.getElementById("root");
if (container) {
  const root = createRoot(container);

  root.render(
    <React.StrictMode>
      <ClassPrefix prefix={CARBON_CLASS_PREFIX}>
        <AuthProvider>
          <QueryClientProvider client={queryClient}>
            <ToastProvider>
              <PreferenceProvider>
                <ThemePreference>
                  <App />
                </ThemePreference>
              </PreferenceProvider>
            </ToastProvider>
          </QueryClientProvider>
        </AuthProvider>
      </ClassPrefix>
    </React.StrictMode>
  );
}
