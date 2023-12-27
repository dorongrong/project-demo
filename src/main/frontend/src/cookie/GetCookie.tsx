import { Cookies } from "react-cookie";
import { jwtDecode } from "jwt-decode";

const cookies = new Cookies();

export const getCookieInfo = (): string => {
  //토큰에서 유저 정보 추출
  const decodeCookie = jwtDecode(cookies.get("Authorization")).sub;
  const userId: string = "토큰이 존재하지않습니다.";
  if (decodeCookie === undefined) {
    // decodeCookie가 undefined일 때 실행할 코드
    // 나중에 로그인페이지로 안내
    console.log("decodeCookie is undefined");
    return userId;
  } else {
    // decodeCookie가 정의되어 있을 때 실행할 코드
    const userId: string = String(decodeCookie);
    return userId;
  }
};

export const getCookieChatInfo = (): string => {
  //토큰에서 유저 정보 추출
  const decodeCookie = jwtDecode(cookies.get("Authorization")) as {
    [key: string]: any;
  } | null;
  const roomsId: string = "토큰이 존재하지않습니다.";
  if (decodeCookie === null) {
    // decodeCookie가 undefined일 때 실행할 코드
    // 나중에 로그인페이지로 안내
    console.log("decodeCookie is undefined");
    return roomsId;
  } else {
    // decodeCookie가 정의되어 있을 때 실행할 코드
    const roomsId: string = String(decodeCookie.itemsId);
    return roomsId;
  }
};

export const getCookieUserId = (): string => {
  //토큰에서 유저 정보 추출
  const decodeCookie = jwtDecode(cookies.get("Authorization")) as {
    [key: string]: any;
  } | null;
  const roomsId: string = "토큰이 존재하지않습니다.";
  if (decodeCookie === null) {
    // decodeCookie가 undefined일 때 실행할 코드
    // 나중에 로그인페이지로 안내
    console.log("decodeCookie is undefined");
    return roomsId;
  } else {
    // decodeCookie가 정의되어 있을 때 실행할 코드s
    const userId: string = String(decodeCookie.userId);
    return userId;
  }
};
