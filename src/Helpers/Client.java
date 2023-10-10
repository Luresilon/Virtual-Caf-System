//registration number: 1803123
package Helpers;

import Helpers.Enum.Status;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client implements Serializable, AutoCloseable {
    final int port = 8888;
    private final String name;
    public ArrayList<Order> orders = new ArrayList<>();
    private final transient Socket socket = new Socket("localhost", port);

    private final Scanner reader;
    private final PrintWriter writer;

    public Client(String name) throws Exception {
        this.name = name;

        new ObjectOutputStream(socket.getOutputStream()).writeObject(this);

        reader  = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);
    }
    public String completeOrdersChecker() {
        if(this.completeStatus()) {
            int coffeeTray = (int) this.orders.stream().filter(o -> o instanceof Coffee && o.getStatus() == Status.TRAY_AREA).count();
            int teaTray = (int) this.orders.stream().filter(o -> o instanceof Tea && o.getStatus() == Status.TRAY_AREA).count();
            this.orders.clear();
            return "Order delivered to " + this.getName() + " (" + teaTray + " teas " + coffeeTray + " coffees)";
        }
        return " x ";
    }

    public void orderTea(int quantity) {
        writer.println("TEA " + quantity);
    }
    public void orderCoffee(int quantity) {
        writer.println("COFFEE " + quantity);
    }
    public boolean completeStatus() {
        ListIterator<Order> tmp;
        tmp = this.orders.listIterator();
        int count = 0;
        while(tmp.hasNext()) {
            Order it = tmp.next();
            if(it.getStatus() == Status.TRAY_AREA) {
                count++;
            }
        }
        boolean complete = count == this.orders.size();
        return complete && !orders.isEmpty();
    }

    public String printOrdersStatus() {
        int teaWaiting = (int) this.orders.stream().filter(o -> o instanceof Tea && o.getStatus() == Status.WAITING_AREA).count();
        int teaBrewing = (int) this.orders.stream().filter(o -> o instanceof Tea && o.getStatus() == Status.BREWING_AREA).count();
        int teaTray = (int) this.orders.stream().filter(o -> o instanceof Tea && o.getStatus() == Status.TRAY_AREA).count();

        int coffeeWaiting = (int) this.orders.stream().filter(o -> o instanceof Coffee && o.getStatus() == Status.WAITING_AREA).count();
        int coffeeBrewing = (int) this.orders.stream().filter(o -> o instanceof Coffee && o.getStatus() == Status.BREWING_AREA).count();
        int coffeeTray = (int) this.orders.stream().filter(o -> o instanceof Coffee && o.getStatus() == Status.TRAY_AREA).count();
        String orderStatus = "Order status for " + this.name + ":"
        + "\n- " + coffeeWaiting + " coffee and " + teaWaiting + " in waiting area"
        + "\n- " + coffeeBrewing + " coffee and " + teaBrewing + " currently being prepared"
        + "\n- " + coffeeTray + " coffee and " + teaTray + " in the tray";
        writer.println("STATUS");
        writer.println(orderStatus);
        return orderStatus;
    }

    public String getName() {return this.name;}
    public List<Order> getOrders() {return this.orders;}

    public void updateOrder() throws IOException, ClassNotFoundException, InterruptedException {
        synchronized (orders) {
            writer.println("INFO");
            Thread.sleep(10);
            this.orders = (ArrayList<Order>) new ObjectInputStream(socket.getInputStream()).readObject();
        }
    }

    @Override
    public void close() {
        reader.close();
        writer.close();
    }
}
