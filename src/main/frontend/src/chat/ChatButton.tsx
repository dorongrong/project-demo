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
import "./css/ChatButton.css";
import { Avatar, Button, Input } from "@mui/material";

const ChatButton: React.FC = () => {
  const [responseMessage, setResponseMessage] = useState("");
  const { itemId = "0" } = useParams<{ itemId: string }>();
  const { buyerId } = useParams<{ buyerId: string }>();
  // 방문자의 loginid
  const [sendUserLoginId, setSendUserLoginId] = useState<string>(
    getCookieInfo()
  );
  // 방문자의 id
  const [sendUserId, setSendUserId] = useState<string>(getCookieUserId());
  // const userId = getCookieInfo();
  // 사용자의 itemId -> 추후 fetch api 실행 유무에 사용
  const [myRoomsId, setMyRoomsId] = useState<string>(getCookieChatInfo());
  // const myRoomsId = getCookieChatInfo();
  const roomId = itemId;
  const cookies = new Cookies();
  const sendCookie = jwtDecode(cookies.get("Authorization"));

  const [messageContent, setMessageContent] = useState<string>("");
  const [chats, setChats] = useState<JSX.Element[]>([]);
  const [fetchChats, setFetchChats] = useState<JSX.Element[]>([]);

  const stomp = useRef<any>();

  const [chatRoomData, setChatRoomData] = useState<ChatRoom | null>(null);
  //페이지 언마운트시 보낼 값 보존
  const chatRoomDataRef = useRef<ChatRoom | null>(null);

  const [userState, setUserState] = useState<UserState>();

  const socketConnect = async () => {
    const sockJS = await new SockJS("http://localhost:1234/stomp/chat");
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

            if (payload.sendUserId !== sendUserId) {
              if (payload.chatUserState === "ONLINE") {
                const userState = {
                  userId: payload.sendUserId,
                  userState: "ONLINE",
                };
                setUserState(userState);
                console.log("업데이트된 사용자 정보", userState);
              } else if (payload.chatUserState === "OFFLINE") {
                const userState = {
                  userId: payload.sendUserId,
                  userState: "OFFLINE",
                };
                setUserState(userState);
                console.log("업데이트된 사용자 정보", userState);
              }
              // setChats 실행 조건 추가
            }
            if (payload.chatUserState === "CHAT") {
              const newChat = (
                <div>
                  <div className="nickname">{payload.sendUserLoginId}</div>
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
            chatRoomId: roomId,
            sendUserLoginId: sendUserLoginId,
            sendUserId: sendUserId,
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
    if (itemId && sendUserLoginId && buyerId && sendUserId) {
      const chatRoom: ChatRoom = {
        sendUserLoginId: sendUserLoginId,
        itemId: itemId,
        buyerId: buyerId,
        sendUserId: sendUserId,
      };
      setChatRoomData(chatRoom);
    }
  }, [sendUserLoginId, itemId, buyerId, sendUserId]);

  //변수 할당후 fetch api 실행
  useEffect(() => {
    //chatRoom 데이터 할당

    chatRoomDataRef.current = chatRoomData;

    //fetch api
    const fetchData = async () => {
      try {
        console.log("가긴하냐?");
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
        // response가 서버로부터 온 답변이 맞아? 다시 확인해
        const resUserState = await response.json();
        // const resUserState: UserState = await response.json();

        setFetchChats(resUserState.ChatRoom.chats);
        setUserState(resUserState.userStateDto);
        console.log("fetch success");
        console.log(resUserState);
        console.log("채팅 나와라");
        console.log(resUserState.ChatRoom.chats);

        if (resUserState.ChatRoom.chats) {
          const fetchChat = resUserState.ChatRoom.chats.map(
            //이거 사용자 이름 나오게 제대로 해야함
            (chat: Message, index: number) => (
              <div key={index}>
                <div className="nickname">
                  {String(chat.sendUserId) === sendUserId
                    ? //채팅이 본인이 보낸것일때
                      sendUserLoginId
                    : resUserState.ChatRoom.displayName}
                </div>
                <div className="message">{chat.message}</div>
              </div>
            )
          );

          setFetchChats(fetchChat);
        }

        // console.log(resUserState);
        // console.log(userState);

        // 서버 응답을 컴포넌트에 넣어서 (저장된) 채팅을 보여줄꺼임
      } catch (error) {
        console.error("Error:", error);
      }
    };
    //해당 변수 할당이 끝났을시 실행
    if (
      chatRoomData?.itemId &&
      chatRoomData?.sendUserLoginId &&
      chatRoomData?.buyerId &&
      chatRoomData?.sendUserId
    ) {
      fetchData();
    }

    // fetchData 함수는 chatRoomData가 변경될 때마다 실행됨
  }, [chatRoomData]);

  useEffect(() => {
    const fetchUnmountData = async () => {
      try {
        const response = await fetch("http://localhost:1234/api/unmount", {
          method: "POST",
          credentials: "include", // 쿠키를 전송해야 하는 경우
          headers: {
            Authorization: jwtDecode(cookies.get("Authorization")),
            "Content-Type": "application/json",
          },
          body: JSON.stringify(chatRoomDataRef.current),
          //보통 페이지 언마운트시 모든 데이터 통신이 종료된다. 그걸 막아주기 위한 코드
          keepalive: true,
          // body: JSON.stringify(chatRoomData),
        });

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        // 서버 응답을 컴포넌트에 넣어서 (저장된) 채팅을 보여줄꺼임
      } catch (error) {
        console.error("Error:", error);
      }
    };

    const handleBeforeUnload = () => {
      fetchUnmountData();

      stomp.current.send(
        `/pub/chat.exit.${roomId}.${buyerId}`,
        JSON.stringify({
          chatRoomId: roomId,
          sendUserLoginId: sendUserLoginId,
          sendUserId: sendUserId,
        })
      );
      stomp.current.disconnect();
    };

    socketConnect();

    window.addEventListener("beforeunload", () => {
      handleBeforeUnload();
    });

    // cleanup function
    return () => {};
  }, []);
  // , [chatRoomId, nickname, chats]

  useEffect(() => {
    // count 상태가 업데이트된 후에 실행되는 로직
    console.log("count가 변경되었습니다:", userState);
  }, [userState]);

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

    console.log("상대 유저의 상태", userState?.userState);
    console.log("내 userId", sendUserId);
    console.log("내 loginId", sendUserLoginId);

    if (message !== "") {
      const messageData: Message = {
        message: message,
        sendUserLoginId: sendUserLoginId,
        chatRoomId: roomId,
        sendUserId: sendUserId,
        readCount:
          userState?.userState === "ONLINE"
            ? 2
            : userState?.userState === "OFFLINE"
            ? 1
            : 1,
      };

      console.log(userState?.userState);

      stomp.current.send(
        `/pub/chat.message.${roomId}.${buyerId}`,
        JSON.stringify(messageData)
        // JSON.stringify({
        //   chatRoomId: roomId,
        //   sendUserId: userLoginId,
        //   message: message,
        //   userId: userId,
        // })
      );
    }
  };

  return (
    <main>
      <div className="flex flex-col h-full bg-[#343a40]">
        <div className="p-4 bg-gray-200 dark:bg-gray-800">
          <h2 className="text-lg font-bold text-gray-700 dark:text-gray-200">
            상품 정보
          </h2>
          <p className="text-sm text-gray-600 dark:text-gray-400 mt-2">
            이곳에 상품에 대한 간단한 정보가 표시됩니다.
          </p>
        </div>
        <div className="flex flex-col flex-1 overflow-y-auto p-4 space-y-4">
          <div className="flex items-end space-x-2">
            <Avatar className="w-6 h-6">리로롱</Avatar>
            <div className="p-2 rounded-lg bg-gray-200 dark:bg-gray-800">
              <p className="text-sm">안녕하세요!</p>
              <p className="text-xs text-gray-500 mt-1">12:00 PM</p>
            </div>
          </div>
          <div className="flex items-end space-x-2 ml-auto">
            <p className="text-xs text-gray-500">읽음</p>
            <div className="p-2 rounded-lg bg-blue-500 text-white">
              <p className="text-sm">안녕하세요, 어떻게 도와드릴까요?</p>
              <p className="text-xs text-white mt-1">12:01 PM</p>
            </div>
            <Avatar className="w-6 h-6">도로롱</Avatar>
          </div>
          <div className="flex items-end space-x-2">
            <Avatar className="w-6 h-6">도로롱</Avatar>
            <div className="p-2 rounded-lg bg-gray-200 dark:bg-gray-800">
              <p className="text-sm">제품에 대해 더 알고 싶습니다.</p>
              <p className="text-xs text-gray-500 mt-1">12:02 PM</p>
            </div>
          </div>
          <div className="flex items-end space-x-2 ml-auto">
            <p className="text-xs text-gray-500">읽지 않음</p>
            <div className="p-2 rounded-lg bg-blue-500 text-white">
              <p className="text-sm">
                물론이죠, 어떤 제품에 대해 궁금하신가요?
              </p>
              <p className="text-xs text-white mt-1">12:03 PM</p>
            </div>
            <Avatar className="w-6 h-6">도로롱</Avatar>
          </div>
        </div>
        <div className="border-t p-4 border-gray-600">
          <form className="flex space-x-3">
            <Input className="flex-1" placeholder="메시지를 입력하세요..." />
            <Button type="submit">전송</Button>
          </form>
        </div>
      </div>
      {/* <div className="chat-box">
          <h2 style={{}}>Room No. {roomId}</h2>
          <h2>Nickname {sendUserLoginId}</h2>
          <form onSubmit={handleSendMessage}>
            <input
              type="text"
              id="message"
              value={messageContent}
              onChange={(e) => setMessageContent(e.target.value)}
            />
            <input type="submit" value="전송" className="btn-send" />
          </form>
          <div className="chats">{fetchChats}</div>
          <div className="chats">{chats}</div>
        </div> */}
    </main>
  );
};

export default ChatButton;
