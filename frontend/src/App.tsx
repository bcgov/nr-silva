import { Loading } from "@carbon/react";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import { useAuth } from "@/contexts/AuthProvider";
import "./styles/theme.scss";
import "./styles/default-components.scss";
import { protectedRoutes, publicRoutes } from "./routes";

const App: React.FC = () => {
  const auth = useAuth();

  if (auth.isLoading) {
    return <Loading withOverlay={true} />;
  }

  const browserRouter = createBrowserRouter(
    auth.isLoggedIn ? protectedRoutes : publicRoutes
  );

  return (
    <>
      <ReactQueryDevtools initialIsOpen={false} />
      <RouterProvider router={browserRouter} />
    </>
  );
};

export default App;
