import { ThemeType } from "./ThemePreference";

export const toggleTheme = (theme: string, setTheme: React.Dispatch<React.SetStateAction<ThemeType>>)=>{
  if (theme === 'g10') {
    setTheme('g100');
    localStorage.setItem('mode', 'dark');
  }
  if (theme === 'g100') {
    setTheme('g10');
    localStorage.setItem('mode', 'light');
  }
}
