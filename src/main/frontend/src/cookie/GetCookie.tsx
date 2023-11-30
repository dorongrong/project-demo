import { Cookies } from "react-cookie";
import { jwtDecode } from "jwt-decode";

const cookies = new Cookies();

export const getCookieInfo = () => {
  const decodeCookie = jwtDecode(cookies.get("Authorization"));
  const userId = decodeCookie.sub;
  return userId;
};
