import React, { useState } from "react";
import "./App.css";
import ChatButton from "./chat/ChatButton";
import { Route, Routes } from "react-router-dom";
import Main from "./Main";
import { CookiesProvider } from "react-cookie";
import ChatTest from "./chat/ChatTest";
import Test from "./chat/Test";

const App: React.FC = () => {
  const [currentChat, setCurrentChat] = useState({ roomId: "0", nickname: "" });

  return (
    <CookiesProvider>
      <Routes>
        <Route path="*" element={<Main />} />
        <Route path="/items/:itemId/chat/:buyerId" element={<ChatButton />} />
        <Route path="/test" element={<Test />} />
      </Routes>
    </CookiesProvider>
  );
};

export default App;
