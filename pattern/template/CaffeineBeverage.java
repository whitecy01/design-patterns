public abstract class CaffeineBeverage {

    // 템플릿 메소드
    // 알고리즘의 전체 순서를 정의한다.
    final void prepareRecipe() {
        boilWater();
        brew();
        pourInCup();
        addCondiments();
    }

    // 자식 클래스마다 다르게 구현해야 하는 부분
    abstract void brew();

    abstract void addCondiments();

    // 공통으로 사용하는 부분
    void boilWater() {
        System.out.println("물을 끓입니다");
    }

    void pourInCup() {
        System.out.println("컵에 따릅니다");
    }
}