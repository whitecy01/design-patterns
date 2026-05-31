package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

// 프록시가 대신 접근할 대상의 공통 인터페이스
// 원격에서 호출할 수 있어야 하니까 Remote를 상속하고, 메서드마다 RemoteException을 던진다.
public interface GumballMachineRemote extends Remote {

    int getCount() throws RemoteException;

    String getLocation() throws RemoteException;

    State getState() throws RemoteException;

}
