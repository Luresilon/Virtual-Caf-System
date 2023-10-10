//registration number: 1803123
package Helpers;

import Helpers.Enum.Status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class CoffeeMachine extends Thread {
    private final List<Order> orders = new ArrayList<>();
    private final List<Order> completeOrders = new ArrayList<>();
    protected final List<Client> clients = new ArrayList<>();
    private ListIterator<Client> cc = clients.listIterator();
    long startTime;
    long endTime;

    public CoffeeMachine() throws IOException {}

    @Override
    public void run() {
        try {
            while(true) {
                Thread.sleep(1);
                if(this.orders.isEmpty()) {
                    continue;
                }
                startBrewing();
                addToCompleteOrders();
                updateCustomer();
            }
        } catch (InterruptedException | IOException e) {e.printStackTrace();}

    }

    public void addToOrder(List<Order> listOfOrder) {
        this.orders.addAll(listOfOrder);
    }

    private void startBrewing() {
        if(this.orders.stream().filter(o -> o instanceof Tea && o.getStatus() == Status.BREWING_AREA).count() < 2 && !this.orders.isEmpty()
                && this.orders.stream().anyMatch(o -> o instanceof Tea && o.getStatus() == Status.WAITING_AREA)) {
            this.orders.stream().filter(o -> o instanceof Tea && o.getStatus() == Status.WAITING_AREA).findFirst().get().start();
        }

        if(this.orders.stream().filter(o -> o instanceof Coffee && o.getStatus() == Status.BREWING_AREA).count() < 2 && !this.orders.isEmpty()
                && this.orders.stream().anyMatch(o -> o instanceof Coffee && o.getStatus() == Status.WAITING_AREA)) {
            this.orders.stream().filter(o -> o instanceof Coffee && o.getStatus() == Status.WAITING_AREA).findFirst().get().start();
        }
    }
    public void addToCompleteOrders() {
        this.completeOrders.addAll(this.orders.stream().filter(o -> o.getStatus() == Status.TRAY_AREA).collect(Collectors.toList()));
    }
    public void addCustomer(Client client) {
        this.cc = this.clients.listIterator();
        while(cc.hasNext()) {
            if(cc.next() == client) {
                return;
            }
        }
        cc.add(client);
    }


    public void updateCustomer() throws IOException {
        this.cc = this.clients.listIterator();
        while(cc.hasNext()) {
            Client it = cc.next();
            if(it.completeStatus()) {
                System.out.println("[INFO] " + it.getName() + ", your order of " + (int) it.getOrders().stream().filter(o -> o instanceof Coffee).count() + " Coffees and "+ (int) it.getOrders().stream().filter(o -> o instanceof Tea).count() + " Teas is finished!");
                this.cc.remove();
            }
        }
    }
}
