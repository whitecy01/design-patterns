package abstract_factory.pizza;

import abstract_factory.ingredient.PizzaIngredientFactory;

public class ClamPizza extends Pizza {

    private
    PizzaIngredientFactory ingredientFactory;

    public ClamPizza(PizzaIngredientFactory ingredientFactory) {
        this.ingredientFactory = ingredientFactory;
    }

    @Override
    public void prepare() {
        System.out.println("Preparing " + name);

        dough = ingredientFactory.createDough();
        sauce = ingredientFactory.createSauce();
        clam = ingredientFactory.createClam();

        printIngredients();
    }
}