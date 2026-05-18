package abstract_factory.store;

import abstract_factory.ingredient.ChicagoPizzaIngredientFactory;
import abstract_factory.ingredient.PizzaIngredientFactory;
import abstract_factory.pizza.CheesePizza;
import abstract_factory.pizza.ClamPizza;
import abstract_factory.pizza.Pizza;

public class ChicagoPizzaStore extends PizzaStore {

    @Override
    protected Pizza createPizza(String type) {
        Pizza pizza = null;

        PizzaIngredientFactory ingredientFactory = new ChicagoPizzaIngredientFactory();

        if (type.equals("cheese")) {
            pizza = new CheesePizza(ingredientFactory);
            pizza.setName("Chicago Style Cheese Pizza");
        } else if (type.equals("clam")) {
            pizza = new ClamPizza(ingredientFactory);
            pizza.setName("Chicago Style Clam Pizza");
        }

        return pizza;
    }
}