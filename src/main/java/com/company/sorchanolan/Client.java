package com.company.sorchanolan;

import org.json.JSONObject;

public class Client {
  private String clientName;

  public Client(String name) {
    this.clientName = name;
  }

  public JSONObject joinChatroomMessage(String chatroomName) {
    return new JSONObject()
        .put("JOIN_CHATROOM", chatroomName)
        .put("CLIENT_IP", 0)
        .put("PORT", 0)
        .put("CLIENT_NAME", clientName);
  }

  public JSONObject leaveChatroomMessage(int roomRef, int joinId) {
    return new JSONObject()
        .put("LEAVE_CHATROOM", roomRef)
        .put("JOIN_ID", joinId)
        .put("CLIENT_NAME", clientName);
  }

  public JSONObject chatMessage(int roomRef, int joinId) {
    return new JSONObject()
        .put("CHAT", roomRef)
        .put("JOIN_ID", joinId)
        .put("CLIENT_NAME", clientName)
        .put("MESSAGE", getRandomWord(20));
  }

  public JSONObject disconnectMessage() {
    return new JSONObject()
        .put("DISCONNECT", 0)
        .put("PORT", 0)
        .put("CLIENT_NAME", clientName);
  }

  private String getRandomWord(int length) {
    String r = "";
    for(int i = 0; i < length; i++) {
      r += (char)(Math.random() * 26 + 97);
    }
    return r;
  }
}
