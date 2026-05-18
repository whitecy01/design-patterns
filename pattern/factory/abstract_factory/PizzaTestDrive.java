package abstract_factory;

import abstract_factory.pizza.Pizza;
import abstract_factory.store.ChicagoPizzaStore;
import abstract_factory.store.NYPizzaStore;
import abstract_factory.store.PizzaStore;

public class PizzaTestDrive {

    public static void main(String[] args) {
        PizzaStore nyStore = new NYPizzaStore();
        PizzaStore chicagoStore = new ChicagoPizzaStore();

        Pizza pizza = nyStore.orderPizza("cheese");
        System.out.println("Ethan ordered a " + pizza.getName());
        System.out.println();

        pizza = chicagoStore.orderPizza("cheese");
        System.out.println("Joel ordered a " + pizza.getName());
        System.out.println();

        pizza = nyStore.orderPizza("clam");
        System.out.println("Ethan ordered a " + pizza.getName());
        System.out.println();

        pizza = chicagoStore.orderPizza("clam");
        System.out.println("Joel ordered a " + pizza.getName());
    }
}