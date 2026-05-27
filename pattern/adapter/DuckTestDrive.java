public class DuckTestDrive {

    public static void main(String[] args) {
        Duck duck = new MallardDuck();

        Turkey turkey = new WildTurkey();

        Duck turkeyAdapter = new TurkeyAdapter(turkey);

        System.out.println("칠면조:");
        turkey.gobble();
        turkey.fly();

        System.out.println();

        System.out.println("오리:");
        testDuck(duck);

        System.out.println();

        System.out.println("칠면조 어댑터:");
        testDuck(turkeyAdapter);
    }

    static void testDuck(Duck duck) {
        duck.quack();
        duck.fly();
    }
}