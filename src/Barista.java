//registration number: 1803123
import Helpers.CoffeeMachine;
import Helpers.CustomerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Barista {
    private static final int serverPort = 8888;

    public static void main(String[] args) {RunServer();}

    private static void RunServer() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(serverPort);
            CoffeeMachine coffeeMachine = new CoffeeMachine();
            coffeeMachine.start();
            System.out.println("Waiting for incoming connections...");
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new CustomerHandler(socket, coffeeMachine)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
