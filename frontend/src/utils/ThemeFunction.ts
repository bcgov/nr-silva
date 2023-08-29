export const toggleTheme = (theme: any, setTheme: any) => {
  if (theme === 'g10') {
    setTheme('g100')
    localStorage.setItem('mode', 'dark')
  }
  if (theme === 'g100') {
    setTheme('g10')
    localStorage.setItem('mode', 'light')
  }
}
