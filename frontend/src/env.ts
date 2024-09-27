declare global {
  // eslint-disable-next-line no-unused-vars
  interface Window {
    config: object
  }
}

// eslint-disable-next-line import/prefer-default-export
export const env: Record<string, string> = { ...import.meta.env, ...window.config }
