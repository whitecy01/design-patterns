package abstract_factory.ingredient;

//추상 팩토리
public interface PizzaIngredientFactory {
    Dough createDough();
    Sauce createSauce();
    Cheese createCheese();
    Clams createClam();
}