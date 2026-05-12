public class MallardDuck extends Duck {
    public MallardDuck() {
        super(new FlyWithWings());
    }

    @Override
    public void display() {
        System.out.println("저는 청둥오리입니다");
    }
}