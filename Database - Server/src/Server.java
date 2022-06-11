/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author abena
 */
public class Server {
    ServerSocket serverSocket;
    Socket clientSocket;
    DataOutputStream out;
    DataInputStream in;
    String method;

    public void run() {
        System.out.println("Initiating Server...");

        int portNumber = 8081;
        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Creating a server socket that listens on port " + portNumber);
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket.getInetAddress().getHostName());
                out = new DataOutputStream(clientSocket.getOutputStream());
                in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                out.writeUTF("categories");
                out.writeUTF(Database.getAllCategories());
                while (true) {
                    method = in.readUTF();
                    String msg;
                    switch (method) {
                        case "searchByName":
                            if(!clientSocket.isClosed()) {
                                msg = Database.searchCompaniesByName(in.readUTF());
                                out.writeUTF(msg);
                            }
                            break;
                        case "searchByCategory":
                            if(!clientSocket.isClosed()) {
                                msg = Database.getCompaniesByCategory(in.readUTF());
                                out.writeUTF(msg);
                            }
                            break;
                    }
                    if (method.equals("exit")) {
                        System.out.println("Closing connection with client");
                        break;
                    }
                }
                in.close();
                out.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Exception on port " + portNumber + ".");
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}

