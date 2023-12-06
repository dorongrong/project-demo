export type Message = {
  message: string;
  sendUserId: string;
  chatRoomId: string;
  chatState: ChatState;
  key?: string;
};

export enum ChatState {
  CHAT_UNREAD = "CHAT_UNREAD",
  CHAT_READ = "CHAT_READ",
}
