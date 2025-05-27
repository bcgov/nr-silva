/// <reference types="vitest/config" />
import { defineConfig } from 'vite'
import { fileURLToPath, URL } from 'node:url';
import react from '@vitejs/plugin-react';
import istanbul from 'vite-plugin-istanbul';

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  const define = {
    global: {}
  }
  return {
    define,
    plugins: [
      {
        name: 'build-html',
        apply: 'build',
        transformIndexHtml: (html) => {
          return {
            html,
            tags: [
              {
                tag: 'script',
                attrs: {
                  src: '/env.js'
                },
                injectTo: 'head'
              }
            ]
          }
        }
      },
      react()
    ],
    build: {
      chunkSizeWarningLimit: 1024,
      outDir: 'build'
    },
    server: {
      port: 3000,
      hmr: {
        overlay: false
      }
    },
    preview: {
      port: 3000
    },
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      }
    },
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
        ]
      },
      server: {
        deps: {
          inline: ['uuid']
        }
      }
    }
  };
});
