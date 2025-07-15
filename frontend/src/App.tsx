import { Loading } from "@carbon/react";
import { createBrowserRouter, type RouteObject, RouterProvider } from "react-router-dom";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import { useAuth } from "@/contexts/AuthProvider";
import "./styles/theme.scss";
import "./styles/default-components.scss";
import { protectedRoutes, publicRoutes } from "./routes";
import { ClientSelectionRoute } from "./routes/config";

const App: React.FC = () => {
  const auth = useAuth();

  if (auth.isLoading) {
    return <Loading withOverlay={true} />;
  }

  const selecteRouter = (): RouteObject[] => {
    if (auth.user?.associatedClients && auth.user?.associatedClients.length > 1 && !auth.selectedClient) {
      return [ClientSelectionRoute];
    }
    if (auth.isLoggedIn) {
      return protectedRoutes;
    }
    return publicRoutes;
  }

  const browserRouter = createBrowserRouter(selecteRouter());

  return (
    <>
      <ReactQueryDevtools initialIsOpen={false} />
      <RouterProvider router={browserRouter} />
    </>
  );
};

export default App;
