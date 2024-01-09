export type ChatRoom = {
  sendUserLoginId: string;
  itemId: string;
  buyerId: string;
  sendUserId: string;
  message?: Message;
};

export type Message = {
  message: string;
  //보낸사람 Long Id
  sendUserId: string;
  //보낸사람 login Id
  sendUserLoginId: string;
  chatRoomId: string;
  readCount: number;
  regDate?: string;
};

export type UserState = {
  userId: number;
  userState: string;
};

export type Item = {
  itemName: string;
  description: string;
  price: number;
  bargain: boolean;
};
