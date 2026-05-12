public class RubberDuck extends Duck {
    public RubberDuck() {
        super(new FlyNoWay());
    }

    @Override
    public void display() {
        System.out.println("저는 고무오리입니다");
    }
}