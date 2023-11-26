import React, { useEffect, useRef, useState } from "react";
import ChatButton from "./chat/ChatButton";
import { Link, Route, Routes } from "react-router-dom";

const Main: React.FC = () => {
  const [currentChat, setCurrentChat] = useState({ roomId: 0, nickname: "" });

  const handleChatButtonClick = (roomId: any, nickname: any) => {
    setCurrentChat({ roomId, nickname });
  };

  const test = useRef<any>("첫번째");
  console.log(test);

  const effectTest = () => {
    test.current = "두번째";
    console.log(test);
  };

  useEffect(() => {
    effectTest();
  });

  return (
    <div>
      <Link to="/chat">이성훈 채팅</Link>
      <Link to="/chat">이지훈 채팅</Link>
      <button onClick={() => handleChatButtonClick(0, "이성훈")}>Chat 1</button>
      <button onClick={() => handleChatButtonClick(0, "이지훈")}>Chat 2</button>
      <Routes>
        <Route path="*" element={<div>Main Default Content</div>} />
        <Route
          path="/chat"
          element={<ChatButton chatRoomId={0} nickname={"이성훈"} />}
        />
      </Routes>
    </div>
  );
};

export default Main;
