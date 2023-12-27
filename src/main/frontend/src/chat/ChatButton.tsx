import React, { useState, useEffect, useRef } from "react";
import SockJS from "sockjs-client";
import webstomp from "webstomp-client";
import { ChatRoom, Message, UserState } from "./model/chatRoomData";
import {
  getCookieInfo,
  getCookieChatInfo,
  getCookieUserId,
} from "../cookie/GetCookie";
import { useParams } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import { Cookies } from "react-cookie";

const ChatButton: React.FC = () => {
  const [responseMessage, setResponseMessage] = useState("");
  const { itemId } = useParams<{ itemId: string }>();
  const { buyerId } = useParams<{ buyerId: string }>();
  // 방문자의 loginid
  const [userLoginId, setUserLoginId] = useState<String>(getCookieInfo());
  // 방문자의 id
  const [userId, setUserId] = useState<String>(getCookieUserId());
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

  const [userState, setUserState] = useState<UserState>();

  const refState = useRef<UserState>();

  const socketConnect = () => {
    const sockJS = new SockJS("http://localhost:1234/stomp/chat");
    console.log("socketConnect 진입?");

    stomp.current = webstomp.over(sockJS);

    const onError = (e: any) => {
      console.log("STOMP ERROR", e);
      console.log("다시 연결");

      stomp.current.disconnect(function () {
        console.log("Disconnected successfully.");
        setTimeout(function () {
          socketConnect();
        }, 3000);
      });

      // sockJS.onclose = function () {
      //   setTimeout(function () {
      //     socketConnect();
      //   }, 5000);
      // };
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
        // 송신자는 본인의 큐만 subscribe해야함 그렇기에 본인의 큐의 이름을 명확하게 구분해야함
        stomp.current.subscribe(
          `/exchange/chat.exchange/${roomId}.${buyerId}`,
          (content: any) => {
            const payload = JSON.parse(content.body);

            console.log(payload);

            //데이터를 보낸자가 본인이 아닐시를 판단해 온라인 유무 판단
            if (
              payload.userId !== userId &&
              payload.chatUserState === "ONLINE"
            ) {
              console.log("뭐야야야양");
              const userState: UserState = {
                userId: payload.userId,
                userState: "ONLINE",
              };
              setUserState(userState);
              console.log("업데이트된 사용자 정보", userState);
            }
            if (
              payload.userId !== userId &&
              payload.chatUserState === "OFFLINE"
            ) {
              console.log("뭐야야야양");
              const userState: UserState = {
                userId: payload.userId,
                userState: "OFFLINE",
              };
              setUserState(userState);
              console.log("업데이트된 사용자 정보", userState);
            } else {
              const newChat = (
                <div>
                  <div className="nickname">{payload.sendUserId}</div>
                  <div className="message">{payload.message}</div>
                </div>
              );

              setChats((prevChats) => [...prevChats, newChat]);
            }

            //메시지 읽음 확인요청
            // messageRead(content.body);
          },
          { "auto-delete": "true", durable: "false", exclusive: "false" }
        );
        //사용자 상태 업로드

        //폐기
        stomp.current.send(
          `/pub/chat.enter.${roomId}.${buyerId}`,
          JSON.stringify({
            message: userLoginId + "님이 입장하셨습니다.",
            chatRoomId: roomId,
            sendUserId: userLoginId,
            userId: userId,
          })
        );
        //추후 변경
      },
      onError,
      "/"
    );

    // return sockJS;
  };

  useEffect(() => {
    if (itemId && userLoginId && buyerId && userId) {
      const chatRoom: ChatRoom = {
        senderLoginId: userLoginId,
        itemId: itemId,
        buyerId: buyerId,
        userId: userId,
      };
      setChatRoomData(chatRoom);
    }
  }, [userLoginId, itemId, buyerId, userId]);

  //변수 할당후 fetch api 실행
  useEffect(() => {
    //chatRoom 데이터 할당
    console.log("?????????????");
    console.log(
      chatRoomData?.itemId,
      chatRoomData?.senderLoginId,
      chatRoomData?.buyerId,
      chatRoomData?.userId
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
        console.log("fetch api response data ", data);
        // 서버 응답을 컴포넌트에 넣어서 (저장된) 채팅을 보여줄꺼임
      } catch (error) {
        console.error("Error:", error);
      }
    };
    //해당 변수 할당이 끝났을시 실행
    if (
      chatRoomData?.itemId &&
      chatRoomData?.senderLoginId &&
      chatRoomData?.buyerId &&
      chatRoomData?.userId
    ) {
      fetchData();
    }

    // fetchData 함수는 chatRoomData가 변경될 때마다 실행됨
  }, [chatRoomData]);

  useEffect(() => {
    const fetchUnmountData = async () => {
      try {
        const response = await fetch("http://localhost:1234/api/chatunmount", {
          method: "POST",
          credentials: "include", // 쿠키를 전송해야 하는 경우
          headers: {
            Authorization: jwtDecode(cookies.get("Authorization")),
            "Content-Type": "application/json",
          },
          body: JSON.stringify(chatRoomData),
          // body: JSON.stringify(chatRoomData),
        });

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        setUserState(await response.json());

        console.log("Server response:", userState);
        // 서버 응답을 컴포넌트에 넣어서 (저장된) 채팅을 보여줄꺼임
      } catch (error) {
        console.error("Error:", error);
      }
    };

    const handleBeforeUnload = () => {
      stomp.current.send(
        `/pub/chat.exit.${roomId}.${buyerId}`,
        JSON.stringify({
          message: userLoginId + "님이 입장하셨습니다.",
          chatRoomId: roomId,
          sendUserId: userLoginId,
          userId: userId,
        })
      );
      fetchUnmountData();
    };

    socketConnect();

    window.addEventListener("beforeunload", handleBeforeUnload);
    // cleanup function
    return () => {
      window.removeEventListener("beforeunload", handleBeforeUnload);
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
        sendUserId: userLoginId,
        message: message,
        userId: userId,
      })
    );
  };

  return (
    <div>
      <h1>CHAT ROOM</h1>
      <h2>Room No. {roomId}</h2>
      <h2>Nickname {userLoginId}</h2>

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
