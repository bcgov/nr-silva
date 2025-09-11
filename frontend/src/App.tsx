import { Loading } from "@carbon/react";
import {
  createBrowserRouter,
  RouterProvider,
  type RouteObject,
} from "react-router-dom";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import { useAuth } from "@/contexts/AuthProvider";
import "./styles/theme.scss";
import "./styles/default-components.scss";
import { protectedRoutes, publicRoutes } from "./routes";
import { ClientSelectionRoute } from "./routes/config";
import { ModalProvider } from "./contexts/ModalContext";
import ModalRenderer from "./components/Modals/ModalRenderer";

const App: React.FC = () => {
  const auth = useAuth();

  if (auth.isLoading) {
    return <Loading withOverlay={true} />;
  }

  // Wrap all route elements with modal context
  const wrapWithModal = (routes: RouteObject[]): RouteObject[] =>
    routes.map((route) => ({
      ...route,
      element: (
        <ModalProvider>
          <ModalRenderer />
          {route.element}
        </ModalProvider>
      ),
    }));

  const selectRouter = (): RouteObject[] => {
    if (
      auth.user?.associatedClients &&
      auth.user?.associatedClients.length > 1 &&
      !auth.selectedClient
    ) {
      return [ClientSelectionRoute];
    }
    return auth.isLoggedIn
      ? wrapWithModal(protectedRoutes)
      : publicRoutes;
  };

  const browserRouter = createBrowserRouter(selectRouter());

  return (
    <>
      <ReactQueryDevtools initialIsOpen={false} />
      <RouterProvider router={browserRouter} />
    </>
  );
};

export default App;
