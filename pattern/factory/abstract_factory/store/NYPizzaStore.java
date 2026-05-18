package abstract_factory.store;

import abstract_factory.ingredient.NYPizzaIngredientFactory;
import abstract_factory.ingredient.PizzaIngredientFactory;
import abstract_factory.pizza.CheesePizza;
import abstract_factory.pizza.ClamPizza;
import abstract_factory.pizza.Pizza;

public class NYPizzaStore extends PizzaStore {

    @Override
    protected Pizza createPizza(String type) {
        Pizza pizza = null;

        PizzaIngredientFactory ingredientFactory = new NYPizzaIngredientFactory();

        if (type.equals("cheese")) {
            pizza = new CheesePizza(ingredientFactory);
            pizza.setName("New York Style Cheese Pizza");
        } else if (type.equals("clam")) {
            pizza = new ClamPizza(ingredientFactory);
            pizza.setName("New York Style Clam Pizza");
        }

        return pizza;
    }
}