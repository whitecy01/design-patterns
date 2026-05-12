public class Main {
    public static void main(String[] args) {
        Duck mallard = new MallardDuck();
        mallard.display();
        mallard.performFly();

        Duck rubberDuck = new RubberDuck();
        rubberDuck.display();
        rubberDuck.performFly();

        rubberDuck.setFlyBehavior(new FlyRocketPowered());
        rubberDuck.performFly();
    }
}