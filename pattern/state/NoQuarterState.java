public class NoQuarterState implements State {

    private GumballMachine gumballMachine;

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
}