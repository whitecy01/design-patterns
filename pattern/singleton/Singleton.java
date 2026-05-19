
public class Singleton {
    private static Singleton uniqueInstance;

    private Singleton() {
        System.out.println("Singleton 객체 생성");
    }

    public static Singleton getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Singleton();
        }

        return uniqueInstance;
    }
}