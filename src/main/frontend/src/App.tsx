import React, { useState } from "react";
import "./App.css";
import ChatButton from "./chat/ChatButton";
import { Route, Routes } from "react-router-dom";
import Main from "./Main";

const App: React.FC = () => {
  const [currentChat, setCurrentChat] = useState({ roomId: 0, nickname: "" });

  const handleChatButtonClick = (roomId: any, nickname: any) => {
    setCurrentChat({ roomId, nickname });
  };

  return (
    <Routes>
      <Route path="/" element={<Main />} />
      <Route
        path="/chat"
        element={<ChatButton chatRoomId={0} nickname={currentChat.nickname} />}
      />
    </Routes>
  );
};

export default App;
