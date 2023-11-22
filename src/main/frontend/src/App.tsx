import React from "react";
import "./App.css";
import ChatButton from "./chat/ChatButton";
import { Route, Routes } from "react-router-dom";
import Main from "./Main";

const App: React.FC = () => {
  return (
    <Routes>
      <Route path="/">
        <Route index element={<Main />} />
        <Route path="/chat" element={<ChatButton />} />
      </Route>
    </Routes>
  );
};

export default App;
