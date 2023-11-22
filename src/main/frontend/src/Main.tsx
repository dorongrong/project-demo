import React from "react";
import ChatButton from "./chat/ChatButton";
import { Link } from "react-router-dom";

const Main: React.FC = () => {
  return (
    <div>
      <Link to="/chat">채팅</Link>
    </div>
  );
};

export default Main;
