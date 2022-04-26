package ui;

import model.*;

public class LogPrinter {

    public LogPrinter() {
    }

    public void printLog(EventLog el) {
        System.out.println("EVENT LOG");
        System.out.println("==============================");
        for (Event e: el) {
            System.out.println(e.toString());
        }
        System.out.println("==============================");
        System.out.println("END OF LOG");

    }
}
