export type ChatRoom = {
  senderLoginId: String;
  itemId: String;
  buyerId: String;
  userId: String;
  message?: Message;
};

export type Message = {
  message: string;
  sendUserId: string;
  chatRoomId: string;
  chatCount: number;
  key?: string;
};

export type UserState = {
  userId: number;
  userState: string;
};
