/// <reference types="vitest" />
import { defineConfig } from 'vitest/config';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: './src/setupTests.ts',
    css: false,
    coverage: {
      provider: 'v8',
      reporter: ['lcov', 'cobertura', 'html'],
      include: ['src/**/*'],
      exclude: [
        'src/amplifyconfiguration.ts',
        'src/module.d.ts',
        'src/react-app-env.d.ts',
        'src/reportWebVitals.ts'
      ],
    },
    server: {
      deps: {
        inline: [/^(?!.*vitest).*$/]
      }
    },
  },
});
