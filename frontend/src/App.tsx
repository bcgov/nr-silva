import { useMemo, type FC } from "react";
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
import { ModalProvider } from "./contexts/ModalContext";
import ModalRenderer from "./components/Modals/ModalRenderer";

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

const AppRoutes: FC<{ isLoggedIn: boolean }> = ({ isLoggedIn }) => {
  const browserRouter = useMemo(
    () =>
      createBrowserRouter(
        isLoggedIn ? wrapWithModal(protectedRoutes) : publicRoutes
      ),
    [isLoggedIn]
  );

  // Key forces React Router v7 to remount with fresh route matches when auth state flips.
  return (
    <RouterProvider
      key={isLoggedIn ? 'protected' : 'public'}
      router={browserRouter}
    />
  );
};

const App: FC = () => {
  const auth = useAuth();

  if (auth.isLoading) {
    return <Loading withOverlay={true} />;
  }

  return (
    <>
      <ReactQueryDevtools initialIsOpen={false} />
      <AppRoutes isLoggedIn={auth.isLoggedIn} />
    </>
  );
};

export default App;
