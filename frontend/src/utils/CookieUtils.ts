
export function setCookie(name: string, value: string, days = 1) {
  const expires = new Date(Date.now() + days * 86400000).toUTCString();
  document.cookie = `${name}=${encodeURIComponent(value)}; expires=${expires}; path=/`;
}

export function getCookie(name: string): string | null {
  const cookie = document.cookie
    .split('; ')
    .find((row) => row.startsWith(name + '='));
  if (!cookie) return null;
  const idx = cookie.indexOf('=');
  if (idx === -1) return null;
  const value = cookie.slice(idx + 1);
  return decodeURIComponent(value);
}

export function deleteCookie(name: string) {
  document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/`;
}
