package factory_method;

public class ChicagoStyleClamPizza extends Pizza{
    public ChicagoStyleClamPizza() {
        name = "Chicago Style Clam Pizza";
        dough = "Extra Thick Crust Dough";
        sauce = "Plum Tomato Sauce";
    }

    @Override
    public void cut() {
        System.out.println("Cutting the pizza into square slices");
    }
}
