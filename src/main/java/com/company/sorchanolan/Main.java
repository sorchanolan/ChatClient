package com.company.sorchanolan;

import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Main {
  private static Map<Integer, Integer> roomRefJoinId = new HashMap<>();

  public static void main(String[] args) throws Exception {
    Socket clientSocket = new Socket("localhost", 6789);

    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    System.out.println("Client name:");
    String clientName = input.readLine();
    Client client = new Client(clientName);

    while (true) {
      System.out.println("Message type:");
      String inputString = input.readLine();
      System.out.println(inputString);
      MessageType messageType = getMessageType(inputString);

      System.out.println("Input:");
      inputString = input.readLine();

      if (inputString.equals("die")) {
        break;
      }

      String output = getMessage(inputString, messageType, client);
      System.out.println(output);
      outToServer.writeBytes(output + "\n");
      System.out.println("Sent to server");

      String response = inFromServer.readLine();
      System.out.println("FROM SERVER:\n" + response);

      try {
        JSONObject json = new JSONObject(response);
        int roomRef = json.getInt("ROOM_REF");
        int joinId = json.getInt("JOIN_ID");
        roomRefJoinId.put(roomRef, joinId);
        System.out.println("Room Ref: " + roomRef + ", Join ID: " + joinId);
      } catch (Exception e) {
        System.out.println("Could not convert to JSON: " + e.getMessage());
      }
    }

    inFromServer.close();
    outToServer.close();
    input.close();
    clientSocket.close();
  }

  private static MessageType getMessageType(String input) {
    switch (input) {
      case ("join") :
        return MessageType.JOIN;
      case ("leave") :
        return MessageType.LEAVE;
      case ("chat") :
        return MessageType.CHAT;
      case ("disconnect") :
        return MessageType.DISCONNECT;
      default:
        return MessageType.ERROR;
    }
  }

  private static String getMessage(String input, MessageType messageType, Client client) {
    switch (messageType) {
      case JOIN:
        return client.joinChatroomMessage(input).toString();
      case LEAVE: {
        int roomRef = Integer.parseInt(input);
        int joinId = roomRefJoinId.get(roomRef);
        return client.leaveChatroomMessage(roomRef, joinId).toString();
      }
      case CHAT: {
        int roomRef = Integer.parseInt(input);
        int joinId = roomRefJoinId.get(roomRef);
        return client.chatMessage(roomRef, joinId).toString();
      }
      case DISCONNECT:
        return client.disconnectMessage().toString();
      default: return "ERROR incorrect message";
    }
  }
}
