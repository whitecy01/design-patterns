public abstract class Duck {
    private FlyBehavior flyBehavior;

    public Duck(FlyBehavior flyBehavior) {
        this.flyBehavior = flyBehavior;
    }

    public void performFly() {
        flyBehavior.fly();
    }

    public void setFlyBehavior(FlyBehavior flyBehavior) {
        this.flyBehavior = flyBehavior;
    }

    public abstract void display();
}

//public class Duck {
//
//    private String type;
//
//    public Duck(String type){
//        this.type = type;
//    }
//
//    public void fly(){
//        if (type.equals("mallard")) {
//            System.out.println("날개로 납니다");
//        } else if (type.equals("rubber")) {
//            System.out.println("못 납니다");
//        } else if (type.equals("rocket")) {
//            System.out.println("로켓으로 납니다");
//        }
//    }
//}