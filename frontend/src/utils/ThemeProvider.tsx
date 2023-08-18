import { createContext, useContext, useState, useEffect, ReactNode } from 'react';

// Define the theme context data
interface ThemeContextData {
  theme: string,
  setTheme: (theme: string) => void
}

// Create the theme context and set initial theme to "light"
const ThemeContext = createContext<ThemeContextData>({
  theme: "dark",
  setTheme: () => {}
});

// Define a custom hook for using the theme context
export const useTheme = () => useContext(ThemeContext);

interface ThemeProviderProps {
    children:ReactNode
}

// Create the ThemeProvider component
const ThemeProvider: React.FC<ThemeProviderProps> = ({ children }) => {
  const [theme, setTheme] = useState("dark");

  useEffect(() => {
    const savedTheme = localStorage.getItem("theme");
    if (savedTheme) {
      setTheme(savedTheme);
    }
  }, []);

  useEffect(() => {
    document.documentElement.setAttribute("data-theme", theme);
    localStorage.setItem("theme", theme);
  }, [theme]);

  return (
    <ThemeContext.Provider value={{ theme, setTheme }}>
      {children}
    </ThemeContext.Provider>
  );
};

export default ThemeProvider;