export type ChatRoom = {
  senderId: String;
  itemId: String;
  buyerId: String;
  message?: Message;
};

export type Message = {
  message: string;
  sendUserId: string;
  chatRoomId: string;
  chatCount: number;
  key?: string;
};
