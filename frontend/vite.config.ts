import { fileURLToPath, URL } from 'node:url';
import { defineConfig, UserConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  const config: UserConfig = {
    define: {},
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
      },
    },
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      }
    },
  };

  if (mode === 'development') {
    if (config.define) {
      config.define.global = {};
    }
  }

  return config;
});
