package client;

import common.GumballMachineRemote;

import java.rmi.Naming;

public class GumballMonitorClient {

    public static void main(String[] args) {
        try {
            GumballMachineRemote machine =
                    (GumballMachineRemote) Naming.lookup("rmi://localhost/GumballMachine");

            GumballMonitor monitor = new GumballMonitor(machine);
            monitor.report();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}