import React from "react";

interface ChatDataProps {
  // 프롭스의 타입을 정의합니다.
  message: string;
}

const GetChatData: React.FC<ChatDataProps> = ({ message }) => {
  return (
    <div>
      <h2>Functional Component</h2>
      <p>{message}</p>
    </div>
  );
};

export default GetChatData;
