import React, { useState, useEffect, useRef } from "react";
import SockJS from "sockjs-client";
import webstomp from "webstomp-client";
import { ChatRoom, Item, Message, UserState } from "./model/chatRoomData";
import {
  getCookieInfo,
  getCookieChatInfo,
  getCookieUserId,
} from "../cookie/GetCookie";
import { useParams } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import { Cookies } from "react-cookie";
import "./css/ChatButton.css";
import SendIcon from "@mui/icons-material/Send";
import { AppBar, Avatar, Button, Input } from "@mui/material";
import { formatDate, formatDateArray } from "./FormDate";

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
  const userStateRef = useRef<string>();

  const [item, setItem] = useState<Item>();
  // 채팅 자동으로 위로
  const chatContainerRef = useRef<HTMLDivElement | null>(null);

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
              console.log("확인용", userState);

              const newChat = (
                <>
                  {payload.sendUserId === sendUserId ? (
                    <div className="flex items-end space-x-2 ml-auto">
                      {userStateRef.current === "ONLINE" && (
                        <p className="text-xs text-gray-500">읽음</p>
                      )}
                      <div className="p-2 rounded-lg bg-blue-500 text-white">
                        <p className="text-sm">{payload.message}</p>
                        <p className="text-xs text-white mt-1">
                          {formatDateArray(payload.regDate)}
                        </p>
                      </div>
                      <Avatar className="w-6 h-6"></Avatar>
                    </div>
                  ) : (
                    <div className="flex items-end space-x-2">
                      <Avatar className="w-6 h-6"></Avatar>
                      <div className="p-2 rounded-lg bg-gray-800 text-white">
                        <p className="text-sm">{payload.message}</p>
                        <p className="text-xs text-white mt-1">
                          {formatDateArray(payload.regDate)}
                        </p>
                      </div>
                    </div>
                  )}
                  {/* <div className="nickname">{payload.sendUserId}</div>
                  <div className="message">{payload.message}</div> */}
                </>
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
        setItem(resUserState.Item);
        console.log("fetch success");
        console.log(resUserState);
        console.log("채팅 나와라");
        console.log(resUserState.ChatRoom.chats);

        if (resUserState.ChatRoom.chats) {
          const fetchChat = resUserState.ChatRoom.chats.map(
            //이거 사용자 이름 나오게 제대로 해야함
            (chat: Message, index: number) => (
              <>
                {String(chat.sendUserId) === sendUserId ? (
                  //채팅이 본인이 보낸것일때
                  <div className="flex items-end space-x-2 ml-auto" key={index}>
                    {chat.readCount === 2 && (
                      <p className="text-xs text-gray-500">읽음</p>
                    )}
                    <div className="p-2 rounded-lg bg-blue-500 text-white">
                      <p className="text-sm">{chat.message}</p>
                      <p className="text-xs text-white mt-1">
                        {formatDate(chat.regDate)}
                      </p>
                    </div>
                    <Avatar className="w-6 h-6"></Avatar>
                  </div>
                ) : (
                  <div className="flex items-end space-x-2" key={index}>
                    <Avatar className="w-6 h-6"></Avatar>
                    <div className="p-2 rounded-lg bg-gray-800 text-white">
                      <p className="text-sm">{chat.message}</p>
                      <p className="text-xs text-gray-500 mt-1">
                        {formatDate(chat.regDate)}
                      </p>
                    </div>
                  </div>
                )}
              </>
            )
            // (chat: Message, index: number) => (
            //   <div key={index}>
            //       {String(chat.sendUserId) === sendUserId
            //         ? //채팅이 본인이 보낸것일때
            //           sendUserLoginId
            //         : resUserState.ChatRoom.displayName}
            //     <div className="message">{chat.message}</div>
            //   </div>
            // )
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
    userStateRef.current = userState?.userState;
    // userState가 바뀌면 Online일시 Chat의 모든 메시지에 읽음 추가
    if (userStateRef.current === "ONLINE") {
      //fetch api
      const fetchChatData = async () => {
        console.log("fetch 확인이용");
        try {
          const response = await fetch("http://localhost:1234/api/chatFetch", {
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
          const resUserState = await response.json();

          if (resUserState.chats) {
            const fetchChat = resUserState.chats.map(
              //이거 사용자 이름 나오게 제대로 해야함
              (chat: Message, index: number) => (
                <>
                  {String(chat.sendUserId) === sendUserId ? (
                    //채팅이 본인이 보낸것일때
                    <div
                      className="flex items-end space-x-2 ml-auto"
                      key={index}
                    >
                      {chat.readCount === 2 && (
                        <p className="text-xs text-gray-500">읽음</p>
                      )}
                      <div className="p-2 rounded-lg bg-blue-500 text-white">
                        <p className="text-sm">{chat.message}</p>
                        <p className="text-xs text-white mt-1">
                          {formatDate(chat.regDate)}
                        </p>
                      </div>
                      <Avatar className="w-6 h-6"></Avatar>
                    </div>
                  ) : (
                    <div className="flex items-end space-x-2" key={index}>
                      <Avatar className="w-6 h-6"></Avatar>
                      <div className="p-2 rounded-lg bg-gray-800 text-white">
                        <p className="text-sm">{chat.message}</p>
                        <p className="text-xs text-gray-500 mt-1">
                          {formatDate(chat.regDate)}
                        </p>
                      </div>
                    </div>
                  )}
                </>
              )
            );
            setChats([]);
            setFetchChats(fetchChat);
          }

          // 서버 응답을 컴포넌트에 넣어서 (저장된) 채팅을 보여줄꺼임
        } catch (error) {
          console.error("Error:", error);
        }
      };
      fetchChatData();
    }
  }, [userState]);

  // 메시지 send 버튼
  const handleSendMessage = (e: React.FormEvent) => {
    e.preventDefault();

    if (chatContainerRef.current) {
      chatContainerRef.current.scrollTop =
        chatContainerRef.current.scrollHeight;
    }

    const message = messageContent;
    setMessageContent("");

    console.log("상대 유저의 상태", userState?.userState);
    console.log("내 userId", sendUserId);
    console.log("내 loginId", sendUserLoginId);

    if (message.trim() !== "") {
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

  //스크롤 이벤트
  useEffect(() => {
    // 채팅이 업데이트될 때마다 스크롤을 아래로 이동

    if (chatContainerRef.current) {
      chatContainerRef.current.scrollTop =
        chatContainerRef.current.scrollHeight;
    }
  }, [chats]);

  useEffect(() => {}, []);

  return (
    <main className="flex flex-col h-screen">
      <div className="flex flex-col h-full bg-[#343a40]">
        <AppBar position="fixed">
          <div className="p-4 bg-gray-200 dark:bg-gray-800">
            <h2 className="text-lg font-bold text-gray-700 dark:text-gray-200">
              {item?.itemName}
            </h2>
            <p className="text-sm text-gray-600 dark:text-gray-400 mt-2">
              {item?.price} 원
            </p>
          </div>
        </AppBar>
        <div
          className="flex flex-col flex-1 p-4 space-y-4 overflow-y-auto mt-24 mb-16"
          ref={chatContainerRef}
        >
          {/* 채팅 시작 */}
          {fetchChats}
          {chats}
        </div>
      </div>
      <div className="border-t p-4 border-gray-600 fixed bottom-0 left-0 w-full bg-[#343a40]">
        <form className="flex space-x-3" onSubmit={handleSendMessage}>
          <Input
            type="text"
            className="flex-1"
            id="message"
            // inputProps={{ style: { color: "white" } }}
            value={messageContent}
            onChange={(e) => setMessageContent(e.target.value)}
            placeholder="메시지를 입력하세요..."
            sx={{
              color: "white", // 글자색을 흰색으로 지정
              "&::before": {
                borderBottomColor: "primary", // 밑줄 색상을 흰색으로 지정
              },
              "&::after": {
                borderBottomColor: "white", // 밑줄 색상을 흰색으로 지정
              },
            }}
          />
          <Button type="submit" className="btn-send" endIcon={<SendIcon />}>
            전송
          </Button>
        </form>
      </div>
    </main>
  );
};

export default ChatButton;
