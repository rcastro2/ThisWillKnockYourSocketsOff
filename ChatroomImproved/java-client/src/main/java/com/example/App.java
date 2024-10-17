package com.example;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.util.Scanner;

import org.json.JSONObject;
import java.net.URISyntaxException;

public class App {
    private Socket socket;
    public static String name = "";
    public static Scanner scanner;

    public App(String serverUrl) {
        try {
            socket = IO.socket(serverUrl);
            socket.on("message", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject jsonObject = (JSONObject) args[0];
                    
                    String sender = jsonObject.getString("sender");
                    String info = jsonObject.getString("info");

                    if(!sender.equals(name)){
                        System.out.println("\t\t\t\t\t\t\t" + sender + ": " + info);
                    }
                }
            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        socket.emit("message", message);
    }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        System.out.print("Enter name: ");
        name = scanner.nextLine();

        App client = new App("http://localhost:5000?name=" + name); 

        while (true) {
            String message = scanner.nextLine();
            if (message.equalsIgnoreCase("exit")) {
                System.out.println("Exiting...");
                break;
            }
            client.sendMessage(message);
        }
        client.socket.close();
    }
}

