package abstract_factory.pizza;

import abstract_factory.ingredient.Cheese;
import abstract_factory.ingredient.Clams;
import abstract_factory.ingredient.Dough;
import abstract_factory.ingredient.Sauce;

public abstract class Pizza {
    protected String name;

    protected Dough dough;
    protected Sauce sauce;
    protected Cheese cheese;
    protected Clams clam;

    public abstract void prepare();

    public void bake() {
        System.out.println("Bake for 25 minutes at 350");
    }

    public void cut() {
        System.out.println("Cutting the pizza into diagonal slices");
    }

    public void box() {
        System.out.println("Place pizza in official PizzaStore box");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected void printIngredients() {
        if (dough != null) {
            System.out.println("Dough: " + dough.getName());
        }
        if (sauce != null) {
            System.out.println("Sauce: " + sauce.getName());
        }
        if (cheese != null) {
            System.out.println("Cheese: " + cheese.getName());
        }
        if (clam != null) {
            System.out.println("Clam: " + clam.getName());
        }
    }
}