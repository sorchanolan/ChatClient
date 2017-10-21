package com.company.sorchanolan;

import java.io.*;
import java.net.*;

public class Main {
  private static final String CLIENT_NAME = "Client 1";

  public static void main(String[] args) throws Exception {
    Client client = new Client(CLIENT_NAME);
    Socket clientSocket = new Socket("localhost", 6789);

    System.out.println("Chatroom Name:");
    BufferedReader chatroomNameInput = new BufferedReader(new InputStreamReader(System.in));
    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    String chatroomName = chatroomNameInput.readLine();
    System.out.println(chatroomName);

    String output = client.joinChatroomMessage(chatroomName).toString();
    System.out.println(output);
    outToServer.writeBytes(output + "\n");
    System.out.println("Sent to server");

    String response = inFromServer.readLine();

    System.out.println("FROM SERVER:\n" + response);

    inFromServer.close();
    outToServer.close();
    chatroomNameInput.close();
    clientSocket.close();
  }
}
