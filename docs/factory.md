# 팩토리 메소드 패턴

## 1. 정의
팩토리 메소드 패턴은 **객체 생성을 서브클래스에게 맡기는 생성 패턴**이다.
> 객체를 생성할 때 필요한 인터페이스를 만듭니다. 어떤 클래스의 인스턴스를 만들지는 서브클래스에서 결정합니다. 팩토리 메서드 패턴을 사용하면 클래스 인스턴스 만든느 일을 서브클래스에게 맡기게 됩니다.

상위 클래스에서는 객체를 사용하는 전체 흐름을 정의하고,  
실제로 어떤 구상 객체를 생성할지는 하위 클래스에서 결정한다.

즉, 상위 클래스는 구체적인 클래스에 직접 의존하지 않고,  
추상 타입에 의존하면서 객체 생성 책임을 하위 클래스에게 위임한다.

```java
public abstract class PizzaStore {

    public Pizza orderPizza(String type) {
        Pizza pizza = createPizza(type);

        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();

        return pizza;
    }

    protected abstract Pizza createPizza(String type);
}
```
코드에서 orderPizza()는 피자를 주문하는 전체 흐름을 정의한다. 하지만 실제로 어떤 피자를 만들지는 createPizza()에게 맡긴다.

createPizza()는 추상 메소드이므로, 실제 피자 생성은 하위 클래스인 NYPizzaStore, ChicagoPizzaStore에서 결정된다.


## 2. 어떤 상황에서 사용하는가?

팩토리 메소드 패턴은 다음과 같은 상황에서 사용한다.

### 2.1 객체 생성 코드가 상위 클래스에 직접 들어가면 변경에 취약할 때
예를 들어 PizzaStore 안에서 직접 피자 객체를 생성한다고 해보자.
```java
if (type.equals("cheese")) {
    pizza = new NYStyleCheesePizza();
} else if (type.equals("clam")) {
    pizza = new NYStyleClamPizza();
}
```
이렇게 하면 PizzaStore가 NYStyleCheesePizza, NYStyleClamPizza 같은 구상 클래스에 직접 의존하게 된다.

이 상태에서 새로운 피자가 추가되거나 지역별 피자 스타일이 달라지면, PizzaStore 코드를 계속 수정해야 한다.

즉, 상위 클래스가 구상 클래스 변화에 영향을 받게 된다.

### 2.2 객체 생성 방식은 달라지지만, 사용하는 흐름은 동일할 때
피자 가게 예제에서 모든 피자는 다음 흐름으로 주문된다.
- 피자 생성 → 준비 → 굽기 → 자르기 → 포장

하지만 생성되는 피자의 종류는 다르다.
- NYPizzaStore       → NYStyleCheesePizza 생성 
- ChicagoPizzaStore  → ChicagoStyleCheesePizza 생성

즉, 전체 처리 흐름은 같지만 생성해야 하는 객체가 상황에 따라 달라질 때 팩토리 메소드 패턴을 사용할 수 있다.

### 2.3 상위 클래스가 구상 클래스에 직접 의존하지 않게 만들고 싶을 때
팩토리 메소드 패턴을 사용하면 상위 클래스는 구상 클래스가 아니라 추상 타입에 의존한다.
```java
Pizza pizza = createPizza(type);
```
PizzaStore는 NYStyleCheesePizza나 ChicagoStyleCheesePizza를 직접 알 필요가 없다. 그저 Pizza 타입의 객체가 반환된다는 사실만 알면 된다.

이렇게 하면 객체 생성 코드가 하위 클래스로 이동하고, 상위 클래스는 전체 흐름에만 집중할 수 있다.

## 3. 현재 코드의 핵심 포인트
### 3.1 PizzaStore는 전체 주문 흐름을 정의한다
```java
public Pizza orderPizza(String type) {
    Pizza pizza = createPizza(type);
    
        System.out.println("--- Making a " + pizza.getName() + " ---");
    
        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();
    
        return pizza;
}
```
PizzaStore는 피자를 주문하는 전체 흐름을 가지고 있다.
```java
createPizza()
prepare()
bake()
cut()
box()
```
여기서 중요한 점은 PizzaStore가 직접 new를 사용해서 구상 피자를 만들지 않는다는 것이다. PizzaStore는 단지 createPizza()를 호출할 뿐이다.

### 3.2 createPizza()가 팩토리 메소드이다
```java
protected abstract Pizza createPizza(String type);
```
createPizza()는 객체 생성을 담당하는 메소드이다. 하지만 PizzaStore에서는 이 메소드를 직접 구현하지 않는다. 대신 추상 메소드로 선언해서 하위 클래스가 구현하도록 만든다.

> 이 메소드가 바로 팩토리 메소드이다.

### 3.3 NYPizzaStore는 뉴욕 스타일 피자를 생성한다
```java
public class NYPizzaStore extends PizzaStore {

    @Override
    protected Pizza createPizza(String type) {
        if (type.equals("cheese")) {
            return new NYStyleCheesePizza();
        } else if (type.equals("clam")) {
            return new NYStyleClamPizza();
        }

        return null;
    }
}
```
NYPizzaStore는 createPizza()를 구현하면서 뉴욕 스타일 피자를 생성한다
```text
cheese → NYStyleCheesePizza
clam   → NYStyleClamPizza
```
즉, 뉴욕 스타일 피자 생성 책임은 NYPizzaStore가 가진다.

### 3.4 PizzaStore는 구상 피자 클래스에 직접 의존하지 않는다
팩토리 메소드 패턴을 적용하기 전에는 PizzaStore가 직접 구상 피자를 생성할 수 있다.
```java
new NYStyleCheesePizza();
new ChicagoStyleCheesePizza();
```
하지만 현재 구조에서는 이런 코드가 PizzaStore 안에 없다. PizzaStore는 오직 추상 타입인 Pizza에만 의존한다.
```java
Pizza pizza = createPizza(type);
```
이 구조 덕분에 PizzaStore는 구체적인 피자 클래스가 어떻게 만들어지는지 몰라도 된다. 객체 생성 책임은 하위 클래스가 담당하고, 상위 클래스는 주문 흐름만 담당한다.

## 4. 이 패턴을 통해 얻는 장점
### 4.1 구상 클래스와의 결합도를 낮출 수 있다
상위 클래스인 PizzaStore가 NYStyleCheesePizza, ChicagoStyleCheesePizza 같은 구상 클래스에 직접 의존하지 않는다. 따라서 새로운 지역 피자 가게를 추가하더라도 기존 PizzaStore의 주문 흐름 코드는 수정할 필요가 없다.

### 4.2 객체 생성 책임을 분리할 수 있다
PizzaStore는 주문 흐름을 담당한다.
```text
prepare → bake → cut → box
```
NYPizzaStore, ChicagoPizzaStore는 객체 생성을 담당한다.
- 어떤 피자를 만들 것인가?

이렇게 역할이 분리되기 때문에 코드의 책임이 더 명확해진다.

### 4.3 확장에 유리하다
새로운 지역 피자 가게를 추가하고 싶다면 PizzaStore를 상속받는 새로운 클래스를 만들면 된다.
```java
public class CaliforniaPizzaStore extends PizzaStore {

    @Override
    protected Pizza createPizza(String type) {
        if (type.equals("cheese")) {
            return new CaliforniaStyleCheesePizza();
        }

        return null;
    }
}
```
기존 PizzaStore의 orderPizza() 흐름은 그대로 재사용할 수 있다.

## 5. 주의할 점
팩토리 메소드 패턴을 사용하면 클래스 수가 늘어날 수 있다. 예를 들어 지역별 피자 가게가 많아지고, 피자 종류도 많아지면 다음과 같은 클래스들이 계속 추가된다.
- NYPizzaStore 
- ChicagoPizzaStore 
- CaliforniaPizzaStore 
- NYStyleCheesePizza 
- ChicagoStyleCheesePizza 
- CaliforniaStyleCheesePizza

따라서 객체 생성 방식이 단순하고 변화 가능성이 적은 경우에는 오히려 구조가 복잡해질 수 있다.

## 6. 정리
팩토리 메소드 패턴은 객체 생성을 상위 클래스에서 직접 하지 않고, 하위 클래스에게 맡기는 패턴이다.

이 패턴의 핵심은 다음과 같다.
- 상위 클래스는 객체를 사용하는 흐름을 정의한다. 
- 하위 클래스는 어떤 객체를 생성할지 결정한다. 
- 상위 클래스는 구상 클래스가 아니라 추상 타입에 의존한다.

피자 예제에서는 다음과 같이 볼 수 있다.

PizzaStore
- orderPizza()로 주문 흐름을 정의한다.
- createPizza()는 추상 메소드로 남겨둔다.

NYPizzaStore
- createPizza()를 구현해서 뉴욕 스타일 피자를 생성한다.

ChicagoPizzaStore
- createPizza()를 구현해서 시카고 스타일 피자를 생성한다.

Pizza
- 모든 피자의 공통 추상 타입이다.

NYStyleCheesePizza, ChicagoStyleCheesePizza
- 실제로 생성되는 구상 피자 클래스이다.

결국 팩토리 메소드 패턴은 다음 디자인 원칙과 연결된다.

> 추상화된 것에 의존하게 만들고, 구상 클래스에 의존하지 않게 만든다.

즉, PizzaStore는 구체적인 피자 클래스에 직접 의존하지 않고,  Pizza라는 추상 타입에 의존한다. 구체적인 피자 생성은 NYPizzaStore, ChicagoPizzaStore 같은 하위 클래스가 담당한다.


# 추상 팩토리 패턴
## 1. 정의
추상 팩토리 패턴은 **관련 있는 객체들의 묶음, 즉 제품군을 생성하는 인터페이스를 제공하는 생성 패턴**이다.
- 구상 클래스에 의존하지 않고도 서로 연관된 객체들의 집합을 생성할 수 있게 해준다. 
- 클라이언트는 실제로 어떤 구상 제품이 만들어지는지 알 필요 없이, 추상 팩토리를 통해 필요한 객체들을 얻는다.

> 구상 클래스에 의존하지 않고도 서로 연관되거나 의존적인 객체로 이루어진 제품군을 생산하는 인터페이스를 제공한다. 구상 클래스는 서브 클래스에서 만듭니다.

팩토리 메소드 패턴이 **하나의 객체 생성을 서브클래스에게 맡기는 패턴**이라면,

추상 팩토리 패턴은 **서로 관련 있는 여러 객체를 하나의 공장에서 함께 생성하는 패턴**이다.

피자 예제에서는 피자를 만들 때 필요한 재료들이 있다.

- 도우
- 소스
- 치즈
- 조개
- 
그런데 지역마다 사용하는 재료가 다르다.

```text
뉴욕 스타일 재료
- ThinCrustDough
- MarinaraSauce
- ReggianoCheese
- FreshClams
시카고 스타일 재료
- ThickCrustDough
- PlumTomatoSauce
- MozzarellaCheese
- FrozenClams
```
이때 피자 클래스가 직접 재료를 생성하면 구상 재료 클래스에 강하게 의존하게 된다.
```java
dough = new ThinCrustDough();
sauce = new MarinaraSauce();
cheese = new ReggianoCheese();
```

추상 팩토리 패턴을 사용하면 피자는 직접 재료를 만들지 않고, 재료 공장에게 필요한 재료를 요청한다.
```java
dough = ingredientFactory.createDough();
sauce = ingredientFactory.createSauce();
cheese = ingredientFactory.createCheese();
```

여기서 PizzaIngredientFactory가 추상 팩토리 역할을 한다.
```java
public interface PizzaIngredientFactory {
    Dough createDough();
    Sauce createSauce();
    Cheese createCheese();
    Clams createClam();
}
```
이 인터페이스는 피자에 필요한 재료들을 생성하는 메소드들을 제공한다. 하지만 실제로 어떤 재료를 만들지는 NYPizzaIngredientFactory, ChicagoPizzaIngredientFactory 같은 구상 팩토리에서 결정한다.

## 2. 어떤 상황에서 사용하는가?
추상 팩토리 패턴은 다음과 같은 상황에서 사용한다.

### 2.1 관련 있는 객체들을 함께 생성해야 할 때
피자를 만들 때 필요한 재료들은 각각 따로 존재하지만, 실제로는 하나의 스타일로 묶인다. 예를 들어 뉴욕 피자를 만들 때는 뉴욕 스타일 재료들이 함께 사용되어야 한다. 예를 들어 뉴욕 피자를 만들 때는 뉴욕 스타일 재료들이 함께 사용되어야 한다.

NY 피자 재료군
- ThinCrustDough
- MarinaraSauce
- ReggianoCheese
- FreshClams

시카고 피자를 만들 때는 시카고 스타일 재료들이 함께 사용되어야 한다.
Chicago 피자 재료군
- ThickCrustDough
- PlumTomatoSauce
- MozzarellaCheese
- FrozenClams

즉, 도우, 소스, 치즈, 조개는 각각 독립된 객체이지만, 지역 스타일이라는 기준으로 서로 관련된 객체들의 묶음이 된다. 이런 경우 추상 팩토리 패턴을 사용하면 관련 객체들을 하나의 공장에서 일관되게 생성할 수 있다.

### 2.2 객체 생성 규칙을 한 곳에 모으고 싶을 때
피자 클래스 안에서 직접 재료를 생성한다고 해보자.
```java
public class CheesePizza extends Pizza {

    public void prepare() {
        dough = new ThinCrustDough();
        sauce = new MarinaraSauce();
        cheese = new ReggianoCheese();
    }
}
```
이렇게 작성하면 CheesePizza는 뉴욕 스타일 재료에 직접 의존하게 된다. 만약 시카고 스타일 치즈 피자를 만들고 싶다면 또 다른 클래스를 만들어야 할 수도 있다.
- NYStyleCheesePizza 
- ChicagoStyleCheesePizza

하지만 추상 팩토리 패턴을 사용하면 CheesePizza는 재료를 직접 만들지 않는다.
```java
public class CheesePizza extends Pizza {

    private PizzaIngredientFactory ingredientFactory;

    public CheesePizza(PizzaIngredientFactory ingredientFactory) {
        this.ingredientFactory = ingredientFactory;
    }

    @Override
    public void prepare() {
        dough = ingredientFactory.createDough();
        sauce = ingredientFactory.createSauce();
        cheese = ingredientFactory.createCheese();
    }
}
```
이제 CheesePizza는 어떤 지역의 재료인지 알 필요가 없다. 그저 전달받은 재료 공장에게 필요한 재료를 요청할 뿐이다. 객체 생성 규칙은 피자 클래스가 아니라 재료 공장으로 이동한다.

### 2.3 구상 클래스에 직접 의존하지 않고 제품군을 바꾸고 싶을 때
추상 팩토리 패턴을 사용하면 클라이언트는 구상 재료 클래스에 직접 의존하지 않는다.
```java
PizzaIngredientFactory ingredientFactory = new NYPizzaIngredientFactory();
Pizza pizza = new CheesePizza(ingredientFactory);
```
CheesePizza는 ThinCrustDough, MarinaraSauce, ReggianoCheese를 직접 알지 못한다. 대신 다음 추상 타입에만 의존한다.
- Dough 
- Sauce 
- Cheese 
- Clams 
- PizzaIngredientFactory

만약 시카고 스타일 재료를 사용하고 싶다면 재료 공장만 바꾸면 된다.
```java
PizzaIngredientFactory ingredientFactory = new ChicagoPizzaIngredientFactory();
Pizza pizza = new CheesePizza(ingredientFactory);
```

즉, 피자 클래스는 그대로 두고 제품군만 바꿀 수 있다.

## 3. 현재 코드의 핵심 포인트
### 3.1 PizzaIngredientFactory가 추상 팩토리이다
```java
public interface PizzaIngredientFactory {
    Dough createDough();
    Sauce createSauce();
    Cheese createCheese();
    Clams createClam();
}
```

PizzaIngredientFactory는 피자에 필요한 재료들을 생성하는 인터페이스이다. 이 인터페이스는 구체적으로 어떤 도우, 어떤 소스, 어떤 치즈, 어떤 조개를 만들지는 결정하지 않는다. 그저 다음과 같은 재료를 만들 수 있어야 한다는 규칙만 정의한다.
- createDough()
- createSauce()
- createCheese()
- createClam()

즉, PizzaIngredientFactory는 관련 있는 객체들의 제품군을 생성하는 추상 팩토리이다.

### 3.2 NYPizzaIngredientFactory는 뉴욕 스타일 재료군을 생성한다
```java
public class NYPizzaIngredientFactory implements PizzaIngredientFactory {

    @Override
    public Dough createDough() {
        return new ThinCrustDough();
    }

    @Override
    public Sauce createSauce() {
        return new MarinaraSauce();
    }

    @Override
    public Cheese createCheese() {
        return new ReggianoCheese();
    }

    @Override
    public Clams createClam() {
        return new FreshClams();
    }
}
```
NYPizzaIngredientFactory는 PizzaIngredientFactory를 구현한 구상 팩토리이다. 이 클래스는 뉴욕 스타일 피자에 들어가는 재료들을 생성한다.
- createDough()  → ThinCrustDough 
- createSauce()  → MarinaraSauce 
- createCheese() → ReggianoCheese 
- createClam()   → FreshClams

즉, 뉴욕 스타일 재료 생성 책임은 NYPizzaIngredientFactory가 가진다.

### 3.4 CheesePizza는 구상 재료 클래스를 직접 생성하지 않는다
```java
public class CheesePizza extends Pizza {

    private PizzaIngredientFactory ingredientFactory;

    public CheesePizza(PizzaIngredientFactory ingredientFactory) {
        this.ingredientFactory = ingredientFactory;
    }

    @Override
    public void prepare() {
        System.out.println("Preparing " + name);

        dough = ingredientFactory.createDough();
        sauce = ingredientFactory.createSauce();
        cheese = ingredientFactory.createCheese();

        printIngredients();
    }
}
```
CheesePizza는 치즈 피자를 만드는 클래스이다. 하지만 내부에서 직접 다음과 같이 구상 재료를 생성하지 않는다.
```java
new ThinCrustDough();
new MarinaraSauce();
new ReggianoCheese();
```

대신 재료 공장에게 필요한 재료를 요청한다.
```java
dough = ingredientFactory.createDough();
sauce = ingredientFactory.createSauce();
cheese = ingredientFactory.createCheese();
```

이 구조 덕분에 CheesePizza는 뉴욕 스타일 치즈 피자도 될 수 있고, 시카고 스타일 치즈 피자도 될 수 있다. 차이는 어떤 재료 공장을 전달받느냐에 따라 결정된다.
```java
new CheesePizza(new NYPizzaIngredientFactory());
new CheesePizza(new ChicagoPizzaIngredientFactory());
```
즉, CheesePizza는 구상 재료 클래스에 직접 의존하지 않고, PizzaIngredientFactory라는 추상 팩토리에 의존한다.

### 3.5 ClamPizza도 재료 생성을 공장에게 위임한다
```java
public class ClamPizza extends Pizza {

    private PizzaIngredientFactory ingredientFactory;

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
```

ClamPizza도 마찬가지로 조개를 직접 생성하지 않는다.
```java
clam = ingredientFactory.createClam();
```
뉴욕 재료 공장을 받으면 신선한 조개가 들어가고, 시카고 재료 공장을 받으면 냉동 조개가 들어간다.
```java
NYPizzaIngredientFactory      → FreshClams
ChicagoPizzaIngredientFactory → FrozenClams
```
즉, 피자 클래스는 어떤 조개가 들어가는지 직접 결정하지 않는다. 재료 선택은 구상 재료 공장이 담당한다.

### 3.6 PizzaStore는 지역별 재료 공장을 선택한다
```java
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
```

NYPizzaStore는 뉴욕 스타일 피자를 만들기 위해 NYPizzaIngredientFactory를 사용한다. 그리고 피자를 생성할 때 이 재료 공장을 전달한다.
```java
pizza = new CheesePizza(ingredientFactory);
```

### 3.7 추상 팩토리 예제 안에도 팩토리 메소드가 들어있다
현재 추상 팩토리 예제에서도 PizzaStore는 다음 구조를 가진다.
```java
public abstract class PizzaStore {

    public Pizza orderPizza(String type) {
        Pizza pizza = createPizza(type);

        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();

        return pizza;
    }

    protected abstract Pizza createPizza(String type);
}
```
여기서 createPizza()는 여전히 팩토리 메소드이다.

즉, 추상 팩토리 예제는 팩토리 메소드 패턴과 완전히 분리된 구조가 아니라, 팩토리 메소드 구조 안에서 재료 생성을 추상 팩토리로 분리한 예제라고 볼 수 있다.
팩토리 메소드
- 어떤 피자 객체를 만들지 결정한다.

추상 팩토리
- 피자에 들어갈 재료군을 생성한다.

## 4. 이 패턴을 통해 얻는 장점
### 4.1 관련 객체들을 일관성 있게 생성할 수 있다
추상 팩토리 패턴을 사용하면 서로 관련된 객체들을 하나의 공장에서 생성할 수 있다. 예를 들어 뉴욕 재료 공장은 항상 뉴욕 스타일 재료들을 생성한다.

NYPizzaIngredientFactory
- ThinCrustDough
- MarinaraSauce
- ReggianoCheese
- FreshClams

ChicagoPizzaIngredientFactory
- ThickCrustDough
- PlumTomatoSauce
- MozzarellaCheese
- FrozenClams

이렇게 하면 서로 어울리지 않는 재료가 섞이는 문제를 줄일 수 있다. 예를 들어 뉴욕 도우에 시카고 소스가 잘못 들어가는 상황을 방지할 수 있다.

### 4.2 구상 클래스와의 결합도를 낮출 수 있다
CheesePizza, ClamPizza는 구상 재료 클래스를 직접 생성하지 않는다. 즉, 다음 클래스들에 직접 의존하지 않는다.

- ThinCrustDough 
- MarinaraSauce 
- ReggianoCheese 
- FreshClams 
- ThickCrustDough 
- PlumTomatoSauce 
- MozzarellaCheese 
- FrozenClams

대신 다음 추상 타입에 의존한다.

- PizzaIngredientFactory 
- Dough 
- Sauce 
- Cheese 
- Clams

이렇게 하면 피자 클래스는 재료의 구체적인 종류를 몰라도 된다. 구체적인 재료 생성은 구상 팩토리가 담당한다.

### 4.3 제품군을 쉽게 교체할 수 있다
추상 팩토리 패턴에서는 공장을 바꾸면 제품군 전체가 바뀐다.
```java
PizzaIngredientFactory ingredientFactory = new NYPizzaIngredientFactory();
```

위 코드는 뉴욕 스타일 재료군을 사용한다.
```java
PizzaIngredientFactory ingredientFactory = new ChicagoPizzaIngredientFactory();
```

즉, 피자 클래스 자체를 수정하지 않고도 재료군을 변경할 수 있다.
- CheesePizza는 그대로 둔다. 
- 재료 공장만 바꾼다. 
- 결과적으로 피자의 스타일이 바뀐다.

### 4.4 객체 생성 책임을 분리할 수 있다
추상 팩토리 패턴을 적용하면 피자 클래스는 피자를 만드는 과정에 집중할 수 있다.
```java
public void prepare() {
    dough = ingredientFactory.createDough();
    sauce = ingredientFactory.createSauce();
    cheese = ingredientFactory.createCheese();
}
```
피자 클래스는 “치즈 피자에는 도우, 소스, 치즈가 필요하다”는 사실만 안다.

하지만 “어떤 도우를 쓸지”, “어떤 소스를 쓸지”, “어떤 치즈를 쓸지”는 재료 공장이 결정한다.


CheesePizza
- 치즈 피자를 준비하는 책임

NYPizzaIngredientFactory
- 뉴욕 스타일 재료를 생성하는 책임

ChicagoPizzaIngredientFactory
- 시카고 스타일 재료를 생성하는 책임

이렇게 역할이 분리되기 때문에 코드의 책임이 더 명확해진다.

## 5. 정리

추상 팩토리 패턴은 구상 클래스에 직접 의존하지 않고, 관련 있는 객체들의 제품군을 생성할 수 있게 해주는 패턴이다.

이 패턴의 핵심은 다음과 같다.

* 관련 있는 객체들을 하나의 공장에서 함께 생성한다.
* 클라이언트는 구상 제품 클래스를 직접 알 필요가 없다.
* 제품군을 바꾸고 싶다면 구상 팩토리를 바꾸면 된다.
* 객체 생성 책임을 사용하는 클래스에서 분리할 수 있다.

피자 예제에서는 다음과 같이 볼 수 있다.

PizzaIngredientFactory

* 재료를 생성하는 추상 팩토리이다.
* 도우, 소스, 치즈, 조개를 생성하는 메소드를 정의한다.

NYPizzaIngredientFactory

* 뉴욕 스타일 재료군을 생성한다.
* ThinCrustDough, MarinaraSauce, ReggianoCheese, FreshClams를 만든다.

ChicagoPizzaIngredientFactory

* 시카고 스타일 재료군을 생성한다.
* ThickCrustDough, PlumTomatoSauce, MozzarellaCheese, FrozenClams를 만든다.

CheesePizza

* 치즈 피자를 만드는 클래스이다.
* 구상 재료를 직접 만들지 않고, 재료 공장에게 요청한다.

ClamPizza

* 조개 피자를 만드는 클래스이다.
* 구상 조개를 직접 만들지 않고, 재료 공장에게 요청한다.

PizzaStore

* 피자 주문 흐름을 정의한다.
* 어떤 피자를 만들지는 createPizza()에서 결정한다.

결국 추상 팩토리 패턴은 다음 디자인 원칙과 연결된다.

추상화된 것에 의존하게 만들고, 구상 클래스에 의존하지 않게 만든다.

즉, CheesePizza는 ThinCrustDough, MarinaraSauce, ReggianoCheese 같은 구상 재료 클래스에 직접 의존하지 않는다.

대신 PizzaIngredientFactory, Dough, Sauce, Cheese 같은 추상 타입에 의존한다.

구체적인 재료 생성은 NYPizzaIngredientFactory, ChicagoPizzaIngredientFactory 같은 구상 팩토리가 담당한다.

# 팩토리 메소드 패턴과 추상 팩토리 패턴 비교

## 1. 공통점

두 패턴 모두 객체 생성을 캡슐화한다. 즉, 클라이언트 코드가 구상 클래스에 직접 의존하지 않도록 만들고,
객체 생성 책임을 별도의 구조로 분리한다. 또한 두 패턴 모두 다음 디자인 원칙과 연결된다. 추상화된 것에 의존하게 만들고, 구상 클래스에 의존하지 않게 만든다.

## 2. 차이점
| 구분 | 팩토리 메소드 패턴 | 추상 팩토리 패턴 |
|---|---|---|
| 목적 | 객체 하나의 생성을 서브클래스에게 맡긴다 | 관련 있는 객체들의 제품군을 생성한다 |
| 핵심 구조 | 상속을 사용한다 | 객체 구성을 사용한다 |
| 생성 단위 | 하나의 구상 객체 | 서로 관련된 여러 객체 |
| 예제에서의 역할 | 어떤 피자 객체를 만들지 결정한다 | 어떤 재료군을 사용할지 결정한다 |
| 핵심 메소드 | `createPizza()` | `createDough()`, `createSauce()`, `createCheese()`, `createClam()` |
| 주요 클래스 | `PizzaStore`, `NYPizzaStore`, `ChicagoPizzaStore` | `PizzaIngredientFactory`, `NYPizzaIngredientFactory`, `ChicagoPizzaIngredientFactory` |
| 클라이언트가 모르는 것 | 어떤 구상 피자 클래스가 생성되는지 | 어떤 구상 재료 클래스들이 생성되는지 |
| 변경 포인트 | 새로운 피자 스타일이나 지점을 추가할 때 하위 Store를 추가한다 | 새로운 재료군을 추가할 때 구상 IngredientFactory를 추가한다 |
| 장점 | 상위 클래스의 주문 흐름을 재사용하면서 객체 생성을 하위 클래스에 맡길 수 있다 | 관련 객체들을 일관성 있는 묶음으로 생성할 수 있다 |
| 주의점 | 지역이나 종류가 많아지면 Store와 피자 클래스 수가 늘어날 수 있다 | 제품 종류가 추가되면 추상 팩토리 인터페이스와 모든 구상 팩토리를 수정해야 할 수 있다 |