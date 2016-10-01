package com.eugeniuparvan.socketserver.example.server;

import com.eugeniuparvan.multiplayer.server.IServer;
import com.eugeniuparvan.multiplayer.server.Server;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by eugeniuparvan on 10/1/16.
 */
public class Main {

    private static IServer server;

    private static int port;

    private static ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(System.in);

        //... Read port number from the console.
        System.out.print("Enter server port number : ");
        port = input.nextInt();

        executor.submit(new ServerThread());

        System.out.println("Server has been started on port: " + port);

        System.out.println("To gracefully stop the server type: stop");
        while (true) {
            String stop = input.nextLine();
            if (stop.equals("stop")) {
                server.stop();
                executor.shutdownNow();
                System.out.println("Server has been gracefully stopped");
                return;
            }
        }
    }

    private static class ServerThread extends Thread {
        public void run() {
            try {
                server = new Server(port);
                server.start();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
