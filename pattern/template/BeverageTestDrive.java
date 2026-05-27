public class BeverageTestDrive {

    public static void main(String[] args) {
        Tea tea = new Tea();
        Coffee coffee = new Coffee();

        System.out.println("차 준비 중...");
        tea.prepareRecipe();

        System.out.println();

        System.out.println("커피 준비 중...");
        coffee.prepareRecipe();
    }
}