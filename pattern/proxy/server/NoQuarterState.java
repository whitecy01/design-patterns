package server;

import common.State;

public class NoQuarterState implements State {

    //State는 직렬화되어 전달될 수 있지만, GumballMachine 전체를 같이 직렬화할 필요는 없기 때문에 transient를 붙인다.
    private transient GumballMachine gumballMachine;

    public NoQuarterState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("동전을 넣었습니다.");
        gumballMachine.setState(gumballMachine.getHasQuarterState());
    }

    @Override
    public void ejectQuarter() {
        System.out.println("동전을 넣지 않았습니다.");
    }

    @Override
    public void turnCrank() {
        System.out.println("동전을 넣어야 합니다.");
    }

    @Override
    public void dispense() {
        System.out.println("동전을 넣어야 껌볼이 나옵니다.");
    }

    @Override
    public String toString() {
        return "동전 없음 상태";
    }
}