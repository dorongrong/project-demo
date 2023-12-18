import React, { useState, useEffect, useRef } from "react";
import SockJS from "sockjs-client";
import webstomp from "webstomp-client";
import { getCookieInfo } from "../cookie/GetCookie";
import { Message, ChatState } from "./model/message";
import { useParams } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import { Cookies } from "react-cookie";

const ChatButton: React.FC = () => {
  const [responseMessage, setResponseMessage] = useState("");
  const { itemId } = useParams<{ itemId: string }>();
  const userId = getCookieInfo();
  const cookies = new Cookies();

  const [messageContent, setMessageContent] = useState<string>("");
  const [chats, setChats] = useState<JSX.Element[]>([]);

  const roomId = itemId;
  const stomp = useRef<any>();
  const sockJS = new SockJS("http://localhost:1234/stomp/chat");

  const [chatRoomData, setChatRoomData] = useState<
    | {
        itemId: string;
        senderId: string;
      }
    | undefined
  >(() => {
    if (itemId && userId) {
      return {
        itemId: itemId,
        senderId: userId,
      };
    }
    return undefined;
  });

  const socketConnect = () => {
    console.log("socketConnect 진입?");

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
        // 송신자는 본인의 큐만 subscribe해야함 그렇기에 본인의 큐의 이름을 명확하게 구분해야함
        stomp.current.subscribe(
          `/exchange/chat.exchange/${roomId}.${userId}`,
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
          `/pub/chat.enter.${roomId}.${userId}`,
          JSON.stringify({
            message: userId + "님이 입장하셨습니다.",
            chatRoomId: roomId,
            sendUserId: userId,
          })
        );
      },
      onError,
      "/"
    );
  };

  //변수 할당후 fetch api 실행
  useEffect(() => {
    const fetchData = async () => {
      if (
        chatRoomData !== undefined &&
        chatRoomData.itemId !== undefined &&
        chatRoomData.senderId !== undefined
      ) {
        try {
          const response = await fetch("http://localhost:1234/api/chat", {
            method: "POST",
            credentials: "include", // 쿠키를 전송해야 하는 경우
            headers: {
              Authorization: jwtDecode(cookies.get("Authorization")),
              "Content-Type": "application/json",
            },
            body: JSON.stringify(chatRoomData),
          });

          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }

          const data = await response.json();
          // 서버 응답을 처리 또는 다른 작업 수행
        } catch (error) {
          console.error("Error:", error);
        }
      }
    };

    // fetchData 함수는 chatRoomData가 변경될 때마다 실행됨
    fetchData();
  }, [chatRoomData]);

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
      `/pub/chat.message.${roomId}.${userId}`,
      JSON.stringify({
        chatRoomId: roomId,
        sendUserId: userId,
        message: message,
      })
    );
  };

  return (
    <div>
      <h1>CHAT ROOM</h1>
      <h2>Room No. {roomId}</h2>
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
