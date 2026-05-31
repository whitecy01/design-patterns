package client;
import common.GumballMachineRemote;

import java.rmi.RemoteException;

public class GumballMonitor {

    private GumballMachineRemote machine;

    public GumballMonitor(GumballMachineRemote machine) {
        this.machine = machine;
    }

    public void report() {
        try {
            System.out.println("껌볼 머신 위치: " + machine.getLocation());
            System.out.println("현재 재고: " + machine.getCount() + "개");
            System.out.println("현재 상태: " + machine.getState());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}