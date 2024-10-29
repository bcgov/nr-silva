import {
  BrowserRouter, Routes, Route
} from 'react-router-dom';
import { Amplify } from 'aws-amplify';
import amplifyconfig from './amplifyconfiguration';

import './custom.scss';

import Landing from "./screens/Landing";
import Help from "./screens/Help";
import SideLayout from './layouts/SideLayout';
import PostLoginRoute from './routes/PostLoginRoute';
import ProtectedRoute from './routes/ProtectedRoute';
import Opening from './screens/Opening';
import DashboardRedirect from './screens/DashboardRedirect';
import SilvicultureSearch from './screens/SilvicultureSearch';

Amplify.configure(amplifyconfig);

const App: React.FC = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Landing/>} />
        <Route path="/dashboard" element={
          <PostLoginRoute>
            <DashboardRedirect />
          </PostLoginRoute>
        } />
        <Route path="/opening" element={
          <ProtectedRoute>
            <SideLayout pageContent={<Opening/>} />
          </ProtectedRoute>
        } />
        <Route path="/silviculture-search" element={
          <ProtectedRoute>
            <SideLayout pageContent={<SilvicultureSearch/>} />
          </ProtectedRoute>
        } />
        <Route path="/help" element={<SideLayout pageContent={<Help/>} />} />
      </Routes>
    </BrowserRouter>
  );
};

export default App;