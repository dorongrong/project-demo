import React, { useState, useEffect, useRef } from "react";
import SockJS from "sockjs-client";
import webstomp from "webstomp-client";
import { ChatRoom, Message } from "./model/chatRoomData";
import { getCookieInfo, getCookieChatInfo } from "../cookie/GetCookie";
import { useParams } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import { Cookies } from "react-cookie";

const ChatButton: React.FC = () => {
  const [responseMessage, setResponseMessage] = useState("");
  const { itemId } = useParams<{ itemId: string }>();
  const { buyerId } = useParams<{ buyerId: string }>();
  // 방문자의 id
  const [userId, setUserId] = useState<String>(getCookieInfo());
  // const userId = getCookieInfo();
  // 사용자의 itemId -> 추후 fetch api 실행 유무에 사용
  const [myRoomsId, setMyRoomsId] = useState<String>(getCookieChatInfo());
  // const myRoomsId = getCookieChatInfo();
  const roomId = itemId;
  const cookies = new Cookies();

  const [messageContent, setMessageContent] = useState<string>("");
  const [chats, setChats] = useState<JSX.Element[]>([]);

  const stomp = useRef<any>();

  const [chatRoomData, setChatRoomData] = useState<ChatRoom | null>(null);

  const socketConnect = () => {
    const sockJS = new SockJS("http://localhost:1234/stomp/chat");
    console.log("socketConnect 진입?");

    stomp.current = webstomp.over(sockJS);

    const onError = (e: any) => {
      console.log("STOMP ERROR", e);
      console.log("다시 연결");

      sockJS.onclose = function () {
        setTimeout(function () {
          socketConnect();
        }, 1000);
      };

      // setTimeout(() => {
      //   // 이미 연결이 되어 있는 경우에는 disconnect 후에 다시 connect
      //   if (stomp.current.connected) {
      //     stomp.current.disconnect();
      //   }
      //   socketConnect();
      // }, 5000);
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
          `/exchange/chat.exchange/${roomId}.${buyerId}`,
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
          `/pub/chat.enter.${roomId}.${buyerId}`,
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
    return sockJS;
  };

  useEffect(() => {
    if (itemId && userId && buyerId) {
      const chatRoom: ChatRoom = {
        senderId: userId,
        itemId: itemId,
        buyerId: buyerId,
      };
      setChatRoomData(chatRoom);
    }
  }, [userId, itemId, buyerId]);

  //변수 할당후 fetch api 실행
  useEffect(() => {
    //chatRoom 데이터 할당
    console.log("?????????????");
    console.log(
      chatRoomData?.itemId,
      chatRoomData?.senderId,
      chatRoomData?.buyerId
    );

    //fetch api
    const fetchData = async () => {
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
        // 서버 응답을 컴포넌트에 넣어서 (저장된) 채팅을 보여줄꺼임
      } catch (error) {
        console.error("Error:", error);
      }
    };
    //해당 변수 할당이 끝났을시 실행
    if (
      chatRoomData?.itemId &&
      chatRoomData?.senderId &&
      chatRoomData?.buyerId
    ) {
      fetchData();
    }

    // fetchData 함수는 chatRoomData가 변경될 때마다 실행됨
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
      `/pub/chat.message.${roomId}.${buyerId}`,
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
