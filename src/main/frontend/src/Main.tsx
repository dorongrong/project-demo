import React, { useEffect, useRef, useState } from "react";
import ChatButton from "./chat/ChatButton";
import { Link, Route, Routes } from "react-router-dom";

const Main: React.FC = () => {
  const [currentChat, setCurrentChat] = useState({ roomId: "0", nickname: "" });

  const [userState, setUserState] = useState();

  const handleChatButtonClick = (roomId: any, nickname: any) => {
    setCurrentChat({ roomId, nickname });
  };

  console.log(currentChat);

  useEffect(() => {
    // count 상태가 업데이트된 후에 실행되는 로직
    console.log("count가 변경되었습니다:", userState);
  }, [userState]);

  return (
    <div>
      <Link to="/chat">이성훈 채팅</Link>
      <Link to="/test">전부 볼수있는 채팅</Link>
      <Routes>
        <Route path="*" element={<div>Main Default Content</div>} />
      </Routes>
    </div>
  );
};

export default Main;
