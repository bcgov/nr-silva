/// <reference types="vitest" />
import { defineConfig } from 'vitest/config';

export default defineConfig({
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: './src/setupTests.ts',
    css: true,
    reporters: ['verbose'],
    coverage: {
      provider: 'v8',
      reporter: ['text', 'json', 'html', 'lcov'],
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
