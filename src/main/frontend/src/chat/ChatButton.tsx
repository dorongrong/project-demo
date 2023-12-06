import React, { useState, useEffect, useRef } from "react";
import SockJS from "sockjs-client";
import webstomp from "webstomp-client";
import { getCookieInfo } from "../cookie/GetCookie";
import { Message, ChatState } from "./model/message";

const ChatButton: React.FC<{ chatRoomId: string; nickname: string }> = ({
  chatRoomId,
  nickname,
}) => {
  const [messageContent, setMessageContent] = useState<string>("");
  const [chats, setChats] = useState<JSX.Element[]>([]);

  const userId = getCookieInfo();

  const roomId = useRef<string>(chatRoomId);
  const stomp = useRef<any>();
  const sockJS = new SockJS("http://localhost:1234/stomp/chat");

  const socketConnect = () => {
    console.log("socketConnect 진입?");

    // stomp.current = webstomp.over(
    //   (() => {
    //     console.log("webstomp.over 진입");
    //     const sockJS = new SockJS("http://localhost:1234/stomp/chat");
    //     return sockJS;
    //   })()
    // );

    stomp.current = webstomp.over(sockJS);

    const onError = (e: any) => {
      console.log("STOMP ERROR", e);
    };

    const onDebug = (m: any) => {
      console.log("STOMP DEBUG", m);
    };

    stomp.current.debug = onDebug;

    console.log("시작");

    stomp.current.connect(
      "guest",
      "guest",
      (frame: any) => {
        console.log("STOMP Connected");

        stomp.current.subscribe(
          `/exchange/chat.exchange/room.` + roomId.current,
          (content: any) => {
            const payload = JSON.parse(content.body);

            // let className = payload.userId === userId ? userId : payload.userId;

            const newChat = (
              <div>
                <div className="nickname">{payload.sendUserId}</div>
                <div className="message">{payload.message}</div>
              </div>
            );

            setChats((prevChats) => [...prevChats, newChat]);

            //메시지 읽음 확인요청
            // messageRead(content.body);
          },
          { "auto-delete": "true", durable: "false", exclusive: "false" }
        );

        stomp.current.send(
          `/pub/chat.enter.` + roomId.current,
          JSON.stringify({
            message: userId + "님이 입장하셨습니다.",
            chatState: ChatState.CHAT_UNREAD,
            chatRoomId: roomId.current,
            sendUserId: userId,
          })
        );
      },
      onError,
      "/"
    );
  };

  useEffect(() => {
    socketConnect();
    // cleanup function
    return () => {
      stomp.current.disconnect(); // disconnect when component unmounts
    };
  }, []);
  // , [chatRoomId, nickname, chats]

  // 메시지 send 버튼
  const handleSendMessage = (e: React.FormEvent) => {
    e.preventDefault();

    // const newChat = (
    //   <div key={chats.length} className="mine">
    //     <div className="nickname">{userId}</div>
    //     <div className="message">{messageContent}</div>
    //   </div>
    // );

    // setChats((prevChats) => [...prevChats, newChat]);

    const message = messageContent;
    setMessageContent("");

    stomp.current.send(
      `/pub/chat.message.` + roomId.current,
      JSON.stringify({
        chatRoomId: roomId.current,
        sendUserId: userId,
        chatState: ChatState.CHAT_UNREAD,
        message: message,
      })
    );
  };

  const messageRead = (message: JSON) => {
    //메시지 확인처리를 서버에 요청
    stomp.current.send(`/pub/chat.messageRead.` + roomId.current, message);
  };

  return (
    <div>
      <h1>CHAT ROOM</h1>
      <h2>Room No. {chatRoomId}</h2>
      <h2>Nickname {userId}</h2>

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
