//registration number: 1803123
package Helpers;
import Helpers.Enum.Status;

import java.io.Serializable;

public abstract class Order extends Thread implements Serializable {
    protected Status status = Status.WAITING_AREA;
    protected int waiting_time = 1000;

    public Order(String name) {
        super(name);
    }

    @Override
    public void run() {
        try {
            this.status = Status.BREWING_AREA;
            Thread.sleep(this.waiting_time);
            this.status = Status.TRAY_AREA;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Status getStatus() {
        return status;
    }
}
