//registration number: 1803123
import Helpers.Client;

import java.util.*;

public class Customer {
    public static void main(String[] args) {
        System.out.println("Welcome to the VirtualCafé!" +
                "\nPlease enter your name: ");
        try {
            Scanner in = new Scanner(System.in);
            String customerName = in.nextLine();
            System.out.println("Welcome " + customerName + "!");
            try  {
                Client client = new Client(customerName);
                String instructions = """

                        Consider the available commands:
                        To order:
                        - order [amount] [tea(s)/coffee(s)] (and) [amount] [tea(s)/coffee(s)]
                        To check the status of your orders:
                        - order status
                        To pick up the order and exit the VirtualCafé:
                        - exit""";
                System.out.println(instructions);

                while (true) {
                    System.out.println("Your command: ");
                    String choice = in.nextLine();
                    List<String> myList = new ArrayList<>(Arrays.asList(choice.split(" ")));
                    if(myList.get(0).equalsIgnoreCase("order")) {
                        boolean validOrder = false;
                        for(int i = 0; i < myList.size(); i++) {
                            if(myList.get(i).equalsIgnoreCase("tea") || myList.get(i).equalsIgnoreCase("teas")) {
                                client.orderTea(Integer.parseInt(myList.get(i - 1)));
                                System.out.println("Ordered: " + Integer.parseInt(myList.get(i - 1)) + " Tea[s]");
                                validOrder = true;
                            }
                            if(myList.get(i).equalsIgnoreCase("coffee") || myList.get(i).equalsIgnoreCase("coffees")) {
                                client.orderCoffee(Integer.parseInt(myList.get(i - 1)));
                                System.out.println("Ordered: " + Integer.parseInt(myList.get(i - 1)) + " Coffee[s]");
                                validOrder = true;
                            }
                            if(myList.get(i).equalsIgnoreCase("status")) {
                                client.updateOrder();
                                System.out.println(client.printOrdersStatus());
                                validOrder = true;
                            }
                        }
                        if(!validOrder) {
                            System.out.println("Order misspelled; please retry or type 'help' for instructions.");
                        }
                    } else if(myList.get(0).equalsIgnoreCase("exit")) {
                        client.updateOrder();
                        System.out.println(client.completeOrdersChecker());
                        in.close();
                        System.out.println("Have a nice day!");
                        System.exit(0);
                    } else if(myList.get(0).equalsIgnoreCase("help")) {
                        System.out.println(instructions);
                    } else {
                        System.out.println("Unknown command; please retry.");
                    }
                }
            } catch (Exception e) { e.printStackTrace();}

        } catch (Exception e) {e.printStackTrace();}
    }
}
