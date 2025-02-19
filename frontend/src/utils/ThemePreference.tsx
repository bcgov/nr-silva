import React, {
  createContext, ReactNode, useContext, useEffect, useMemo, useState
} from 'react';
import { GlobalTheme } from '@carbon/react';

type ThemeType = 'white' | 'g10' | 'g90' | 'g100';

interface ThemeContextData {
  theme: ThemeType;
  setTheme: React.Dispatch<React.SetStateAction<ThemeType>>;
}

const ThemePreferenceContext = createContext<ThemeContextData | undefined>(undefined);

/**
 * Create useThemePreference hook.
 *
 * @returns {ThemeContextData} theme context
 */
function useThemePreference() {
  const context = useContext(ThemePreferenceContext);
  if (!context) {
    throw new Error("useThemePreference must be used within a ThemePreferenceProvider");
  }
  return context;
}

interface ThemePreferenceProps {
  readonly children?: ReactNode;
}

/**
 * Defines the Theme Preference Context and Provider.
 *
 * @param {ReactNode} children with nodes to be rendered
 * @returns {JSX.Element} The Context Provider
 */
function ThemePreference({ children }: ThemePreferenceProps) {
  const [theme, setTheme] = useState<ThemeType>(() => {
    const storedTheme = localStorage.getItem('theme') as ThemeType;
    if (storedTheme && ['white', 'g10', 'g90', 'g100'].includes(storedTheme)) {
      return storedTheme;
    }
    return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'g100' : 'g10';
  });

  const value = useMemo(() => ({
    theme,
    setTheme
  }), [theme]);

  useEffect(() => {
    document.documentElement.setAttribute('data-carbon-theme', theme);
    document.documentElement.setAttribute('data-bs-theme', theme === 'g100' ? 'dark' : 'light');
    localStorage.setItem('theme', theme);
  }, [theme]);

  useEffect(() => {
    const handleColorSchemeChange = (event: MediaQueryListEvent) => {
      const newTheme: ThemeType = event.matches ? 'g100' : 'g10';
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

export { ThemePreference, useThemePreference, type ThemeType };
