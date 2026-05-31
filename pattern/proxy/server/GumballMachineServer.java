package server;

import common.GumballMachineRemote;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class GumballMachineServer {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);

            GumballMachineRemote machine =
                    new GumballMachine("서울 강남점", 5);

            Naming.rebind("rmi://localhost/GumballMachine", machine);

            System.out.println("껌볼 머신 서버가 실행되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}