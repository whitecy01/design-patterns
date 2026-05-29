public class SoldState implements State {

    private GumballMachine gumballMachine;

    public SoldState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("잠깐만 기다려주세요. 이미 껌볼이 나가고 있습니다.");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("이미 손잡이를 돌렸습니다. 동전을 반환할 수 없습니다.");
    }

    @Override
    public void turnCrank() {
        System.out.println("손잡이는 한 번만 돌려주세요.");
    }

    @Override
    public void dispense() {
        gumballMachine.releaseBall();

        if (gumballMachine.getCount() > 0) {
            gumballMachine.setState(gumballMachine.getNoQuarterState());
        } else {
            System.out.println("더 이상 껌볼이 없습니다.");
            gumballMachine.setState(gumballMachine.getSoldOutState());
        }
    }
}