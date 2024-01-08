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
  regData?: string;
};

export type UserState = {
  userId: number;
  userState: string;
};
