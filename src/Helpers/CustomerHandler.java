//registration number: 1803123
package Helpers;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class CustomerHandler implements Runnable {
    private final Socket socket;
    private final CoffeeMachine coffeeMachine;
    private Client client;
    private final String serverMsg = "[INFO] ";

    public CustomerHandler(Socket socket, CoffeeMachine coffeeMachine) {
        this.socket = socket;
        this.coffeeMachine = coffeeMachine;
    }

    @Override
    public void run() {
        String customerName = null;
        try {
            client = (Client) new ObjectInputStream(socket.getInputStream()).readObject();
            try {
                customerName = client.getName();
                System.out.println(serverMsg + "New connection; Client name: " + client.getName());

                Scanner scanner = new Scanner(socket.getInputStream());

                while (true) {
                    Thread.sleep(10);
                    String choice = scanner.nextLine();
                    List<String> myList = new ArrayList<>(Arrays.asList(choice.split(" ")));
                    List<Order> myOrderList = new ArrayList<>();
                    int quantity;
                    switch (myList.get(0).toLowerCase(Locale.ROOT)) {
                        case "tea" -> {
                            quantity = Integer.parseInt(myList.get(1).toLowerCase(Locale.ROOT));
                            for (int i = 0; i < quantity; i++) {
                                Tea tea = new Tea(customerName);
                                client.orders.add(tea);
                                myOrderList.add(tea);
                            }
                            System.out.println(serverMsg + customerName + " has ordered " + quantity + " tea(s)...");
                        }
                        case "coffee" -> {
                            quantity = Integer.parseInt(myList.get(1).toLowerCase(Locale.ROOT));
                            for (int i = 0; i < quantity; i++) {
                                Coffee coffee = new Coffee(customerName);
                                client.orders.add(coffee);
                                myOrderList.add(coffee);
                            }
                            System.out.println(serverMsg + customerName + " has ordered " + quantity + " coffee(s)...");
                        }
                        case "info" -> new ObjectOutputStream(socket.getOutputStream()).writeObject(client.getOrders());
                    }
                    if(!myOrderList.isEmpty()) {
                        coffeeMachine.addCustomer(client);
                        coffeeMachine.addToOrder(myOrderList);
                    }
                }
            } catch (Exception e) {
                socket.close();
            }
        } catch (Exception e) {
        } finally {
            System.out.println(serverMsg + "Customer " + customerName + " has disconnected.");
        }
    }
}
