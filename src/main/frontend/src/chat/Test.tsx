import React from "react";

export default function Component() {
  return (
    <div
      className="flex flex-col h-full"
      style={{ backgroundColor: "#343a40" }}
    >
      <div className="flex flex-col flex-1 overflow-y-auto p-4 space-y-4">
        <div className="flex items-end space-x-2">
          <div className="w-6 h-6">
            <img alt="Sender" src="/placeholder.svg?height=24&width=24" />
            <span>SE</span>
          </div>
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
          <div className="w-6 h-6">
            <img alt="Receiver" src="/placeholder.svg?height=24&width=24" />
            <span>RE</span>
          </div>
        </div>
        <div className="flex items-end space-x-2">
          <div className="w-6 h-6">
            <img alt="Sender" src="/placeholder.svg?height=24&width=24" />
            <span>SE</span>
          </div>
          <div className="p-2 rounded-lg bg-gray-200 dark:bg-gray-800">
            <p className="text-sm">제품에 대해 더 알고 싶습니다.</p>
            <p className="text-xs text-gray-500 mt-1">12:02 PM</p>
          </div>
        </div>
        <div className="flex items-end space-x-2 ml-auto">
          <p className="text-xs text-gray-500">읽지 않음</p>
          <div className="p-2 rounded-lg bg-blue-500 text-white">
            <p className="text-sm">물론이죠, 어떤 제품에 대해 궁금하신가요?</p>
            <p className="text-xs text-white mt-1">12:03 PM</p>
          </div>
          <div className="w-6 h-6">
            <img alt="Receiver" src="/placeholder.svg?height=24&width=24" />
            <span>RE</span>
          </div>
        </div>
      </div>
      <div className="border-t p-4 border-gray-600">
        <form className="flex space-x-3">
          <input className="flex-1" placeholder="메시지를 입력하세요..." />
          <button type="submit">전송</button>
        </form>
      </div>
    </div>
  );
}
