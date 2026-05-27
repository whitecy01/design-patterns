public class Tea extends CaffeineBeverage {

    @Override
    void brew() {
        System.out.println("찻잎을 우려냅니다");
    }

    @Override
    void addCondiments() {
        System.out.println("레몬을 추가합니다");
    }
}