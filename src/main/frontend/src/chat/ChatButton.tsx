import { Stomp } from "@stomp/stompjs";
import React, { useState, useEffect, useRef } from "react";
import SockJS from "sockjs-client";

const ChatButton: React.FC<{ chatRoomId: number; nickname: string }> = ({
  chatRoomId,
  nickname,
}) => {
  const [messageContent, setMessageContent] = useState<string>("");
  const [chats, setChats] = useState<JSX.Element[]>([]);

  const sockJS = useRef(new SockJS("/stomp/chat"));
  const stomp = useRef(Stomp.over(sockJS.current));

  useEffect(() => {
    const onError = (e: any) => {
      console.log("STOMP ERROR", e);
    };

    const onDebug = (m: any) => {
      console.log("STOMP DEBUG", m);
    };

    stomp.current.debug = onDebug;

    stomp.current.connect(
      "guest",
      "guest",
      (frame: any) => {
        console.log("STOMP Connected");

        stomp.current.subscribe(
          `/exchange/chat.exchange/room.${chatRoomId}`,
          (content: any) => {
            const payload = JSON.parse(content.body);

            let className = payload.nickname === nickname ? "mine" : "yours";

            const newChat = (
              <div key={chats.length} className={className}>
                <div className="nickname">{payload.nickname}</div>
                <div className="message">{payload.message}</div>
              </div>
            );

            setChats((prevChats) => [...prevChats, newChat]);
          },
          { "auto-delete": "true", durable: "false", exclusive: "false" }
        );

        stomp.current.send(
          `/pub/chat.enter.${chatRoomId}`,
          {},
          JSON.stringify({
            memberId: 1,
            nickname: nickname,
          })
        );
      },
      onError,
      "/"
    );

    // cleanup function
    return () => {
      stomp.current.disconnect(); // disconnect when component unmounts
    };
  }, [chatRoomId, nickname, chats]);

  const handleSendMessage = (e: React.FormEvent) => {
    e.preventDefault();

    const newChat = (
      <div key={chats.length} className="mine">
        <div className="nickname">{nickname}</div>
        <div className="message">{messageContent}</div>
      </div>
    );

    setChats((prevChats) => [...prevChats, newChat]);

    const message = messageContent;
    setMessageContent("");

    stomp.current.send(
      `/pub/chat.message.${chatRoomId}`,
      {},
      JSON.stringify({
        message: message,
        memberId: 1,
        nickname: nickname,
      })
    );
  };

  return (
    <div>
      <h1>CHAT ROOM</h1>
      <h2>{`Room No. ${chatRoomId}`}</h2>
      <h2>{`Nickname = ${nickname}`}</h2>

      <form onSubmit={handleSendMessage}>
        <input
          type="text"
          id="message"
          value={messageContent}
          onChange={(e) => setMessageContent(e.target.value)}
        />
        <input type="submit" value="전송" className="btn-send" />
      </form>

      <div className="chats">{chats}</div>
    </div>
  );
};

export default ChatButton;
