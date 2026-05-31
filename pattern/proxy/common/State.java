package common;

import java.io.Serializable;

//상태 객체가 네트워크를 통해 전달될 수 있으려면 Serializable을 구현
public interface State extends Serializable {

    void insertQuarter();

    void ejectQuarter();

    void turnCrank();

    void dispense();
}