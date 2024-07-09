import {
  BrowserRouter, Routes, Route
} from 'react-router-dom';
import { Amplify } from 'aws-amplify';
import amplifyconfig from './amplifyconfiguration';

import './custom.scss';

import Landing from "./screens/Landing";
import Help from "./screens/Help";
import Reports from './screens/Reports';
import SideLayout from './layouts/SideLayout';
import Dashboard from './screens/Dashboard';
import PostLoginRoute from './routes/PostLoginRoute';
import ProtectedRoute from './routes/ProtectedRoute';
import Opening from './screens/Opening';
import DashboardRedirect from './screens/DashboardRedirect';

Amplify.configure(amplifyconfig);

const App: React.FC = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Landing/>} />
        <Route path="/dashboard" element={
          <PostLoginRoute signed={true}>
              <DashboardRedirect />
          </PostLoginRoute>
        } />
        <Route path="/opening" element={
          <ProtectedRoute signed={true}>
            <SideLayout pageContent={<Opening/>} />
          </ProtectedRoute>
        } />
        <Route path="/reports" element={
          <ProtectedRoute signed={true}>
            <SideLayout pageContent={<Reports/>} />
          </ProtectedRoute>
        } />
        <Route path="/help" element={<SideLayout pageContent={<Help/>} />} />
      </Routes>
    </BrowserRouter>
  );
};

export default App;
