import React, { useState } from "react";
import "./App.css";
import ChatButton from "./chat/ChatButton";
import { Route, Routes } from "react-router-dom";
import Main from "./Main";
import { CookiesProvider } from "react-cookie";
import ChatTest from "./chat/ChatTest";

const App: React.FC = () => {
  const [currentChat, setCurrentChat] = useState({ roomId: "0", nickname: "" });

  return (
    <CookiesProvider>
      <Routes>
        <Route path="*" element={<Main />} />
        <Route path="/items/:itemId/chat" element={<ChatButton />} />
        <Route
          path="/test"
          element={
            <ChatTest chatRoomId={"1"} nickname={currentChat.nickname} />
          }
        />
      </Routes>
    </CookiesProvider>
  );
};

export default App;
