import React from 'react';
import './index.css';
import { ClassPrefix } from '@carbon/react';
import { Provider } from 'react-redux'
import store from './store'
import App from './App';
import { ThemePreference } from './utils/ThemePreference';
import { createRoot } from 'react-dom/client';
const container:any = document.getElementById('root');
const root = createRoot(container);

root.render(
  <React.StrictMode>
    <ClassPrefix prefix='bcgov'>
      <ThemePreference>
        <Provider store={store}>
          <App />
        </Provider>
      </ThemePreference>
    </ClassPrefix>
  </React.StrictMode>
);
