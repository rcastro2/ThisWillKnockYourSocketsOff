package com.example;
import io.socket.client.IO;
import io.socket.client.Socket;
import java.net.URISyntaxException;

import java.util.Scanner;
import javax.swing.*;

public class App {
    public static Socket socket;
    public static String id = "";
    public static String color = "";
    public static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        System.out.print("Enter name: ");
        App.id = scanner.nextLine();

        System.out.print("Enter color: ");
        App.color = scanner.nextLine();
        String serverUrl = "http://127.0.0.1:5000?id=" + App.id + "&color='" + App.color + "'";
        try {
            socket = IO.socket(serverUrl);
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        JFrame f = new JFrame();
        Window c = new Window(800,600);
        f.add(c);
        f.setSize(Game.width, Game.height);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //App.socket.close();
    }
}

