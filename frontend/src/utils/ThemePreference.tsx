import React, {
  createContext, ReactNode, useContext, useEffect, useMemo, useState
} from 'react';
import { GlobalTheme } from '@carbon/react';

interface ThemeContextData {
  theme: string,
  setTheme: React.Dispatch<React.SetStateAction<string>>
}

const ThemePreferenceContext = createContext<ThemeContextData>({} as ThemeContextData);

/**
 * Create useThemePreference hook.
 *
 * @returns {ThemeContextData} theme context
 */
function useThemePreference() {
  return useContext(ThemePreferenceContext);
}

interface ThemePreferenceProps {
  readonly children?: ReactNode
}

/**
 * Defines the Theme Preference Context and Provider.
 *
 * @param {ReactNode} children with nodes to be rendered
 * @returns {JSX.Element} The Context Provider
 */
function ThemePreference({ children }: ThemePreferenceProps) {
  const [theme, setTheme] = useState(() => {
    const storedTheme = localStorage.getItem('theme');
    if (storedTheme) {
      return storedTheme;
    } else {
      const prefersDarkMode = window.matchMedia('(prefers-color-scheme: dark)').matches;
      return prefersDarkMode ? 'g100' : 'g10';
    }
  });

  const value = useMemo(() => ({
    theme,
    setTheme
  }), [theme, setTheme]);

  useEffect(() => {
    document.documentElement.setAttribute('data-carbon-theme', theme);
    document.documentElement.setAttribute('data-bs-theme', theme === 'g100' ? 'dark' : 'light');
    localStorage.setItem('theme', theme);
  }, [theme]);

  useEffect(() => {
    const handleColorSchemeChange = (event: MediaQueryListEvent) => {
      const newTheme = event.matches ? 'g100' : 'g10';
      setTheme(newTheme);
    };

    const colorSchemeMediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
    colorSchemeMediaQuery.addEventListener('change', handleColorSchemeChange);

    return () => {
      colorSchemeMediaQuery.removeEventListener('change', handleColorSchemeChange);
    };
  }, []);

  return (
    <ThemePreferenceContext.Provider value={value}>
      <GlobalTheme theme={theme}>{children}</GlobalTheme>
    </ThemePreferenceContext.Provider>
  );
}

export { ThemePreference, useThemePreference };
