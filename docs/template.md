# 템플릿 메소드 패턴

## 1. 정의

템플릿 메소드 패턴은 **알고리즘의 전체 구조는 부모 클래스에서 정의하고, 일부 단계는 자식 클래스에서 구현하도록 하는 패턴**이다.

즉, 공통된 처리 순서는 부모가 정하고, 달라지는 부분만 자식 클래스가 바꾼다.

> 알고리즘의 골격을 정의합니다 템플릿 멤소드를 사용하면 알고리즘의 일부 단계를 서브 클래스에서 구현할수 있으며, 알고리즘의 구조는 그대로 유지하면서 특정 단계를 서브클래스에서 재정의할 수도 있습니다.

## 2. 언제 사용하는가?

여러 클래스가 거의 같은 순서로 동작하지만,  
중간중간 세부 구현만 다를 때 사용한다.

예를 들어 차와 커피는 만드는 순서가 비슷하다.

```text
1. 물을 끓인다
2. 우려낸다
3. 컵에 따른다
4. 첨가물을 넣는다
```

하지만 `우려내는 방법`과 `첨가물`은 다르다.

이럴 때 공통 흐름은 부모 클래스에 두고, 다른 부분만 자식 클래스에서 구현한다.


## 3. 구조

| 역할 | 설명 | 예시 |
|---|---|---|
| AbstractClass | 알고리즘의 전체 흐름을 정의하는 부모 클래스 | CaffeineBeverage |
| Template Method | 알고리즘 순서를 정의한 메서드 | prepareRecipe() |
| Primitive Operation | 자식 클래스가 구현해야 하는 단계 | brew(), addCondiments() |
| ConcreteClass | 세부 단계를 구현하는 자식 클래스 | Tea, Coffee |


## 4. 예제 흐름

```java
public abstract class CaffeineBeverage {

    final void prepareRecipe() {
        boilWater();
        brew();
        pourInCup();
        addCondiments();
    }

    abstract void brew();

    abstract void addCondiments();

    void boilWater() {
        System.out.println("물을 끓입니다");
    }

    void pourInCup() {
        System.out.println("컵에 따릅니다");
    }
}
```

`prepareRecipe()`가 템플릿 메소드이다.

전체 순서는 부모 클래스가 정한다.

```java
final void prepareRecipe() {
    boilWater();
    brew();
    pourInCup();
    addCondiments();
}
```

`brew()`와 `addCondiments()`는 음료마다 다르기 때문에 자식 클래스에서 구현한다.

```java
public class Tea extends CaffeineBeverage {

    @Override
    void brew() {
        System.out.println("찻잎을 우려냅니다");
    }

    @Override
    void addCondiments() {
        System.out.println("레몬을 추가합니다");
    }
}
```

```java
public class Coffee extends CaffeineBeverage {

    @Override
    void brew() {
        System.out.println("커피를 우려냅니다");
    }

    @Override
    void addCondiments() {
        System.out.println("설탕과 우유를 추가합니다");
    }
}
```

## 5. 후크

후크는 **부모 클래스에서 기본 구현을 제공하는 메서드**이다.

자식 클래스는 필요하면 후크를 오버라이드해서 알고리즘의 흐름에 끼어들 수 있고,  
필요하지 않으면 그대로 사용할 수 있다.

즉, 추상 메서드처럼 반드시 구현해야 하는 것은 아니고,  
선택적으로 재정의할 수 있는 메서드이다.

```java
public abstract class CaffeineBeverageWithHook {

    final void prepareRecipe() {
        boilWater();
        brew();
        pourInCup();

        if (customerWantsCondiments()) {
            addCondiments();
        }
    }

    abstract void brew();

    abstract void addCondiments();

    void boilWater() {
        System.out.println("물을 끓입니다");
    }

    void pourInCup() {
        System.out.println("컵에 따릅니다");
    }

    // 후크 메서드
    boolean customerWantsCondiments() {
        return true;
    }
}
```

위 코드에서 `customerWantsCondiments()`가 후크이다.

기본값은 `true`이므로 첨가물을 넣는다.

하지만 자식 클래스에서 이 메서드를 오버라이드하면 첨가물을 넣을지 말지 결정할 수 있다.

```java
public class CoffeeWithHook extends CaffeineBeverageWithHook {

    @Override
    void brew() {
        System.out.println("커피를 우려냅니다");
    }

    @Override
    void addCondiments() {
        System.out.println("설탕과 우유를 추가합니다");
    }

    @Override
    boolean customerWantsCondiments() {
        return false;
    }
}
```

이 경우 `customerWantsCondiments()`가 `false`를 반환하므로  
`addCondiments()`는 실행되지 않는다.

### 후크 정리

| 구분 | 설명 |
|---|---|
| 추상 메서드 | 자식 클래스가 반드시 구현해야 한다 |
| 후크 메서드 | 부모 클래스가 기본 구현을 제공하고, 자식 클래스가 필요할 때만 재정의한다 |

후크를 사용하면 알고리즘의 전체 구조는 유지하면서,  
특정 단계의 실행 여부나 동작을 자식 클래스가 선택적으로 바꿀 수 있다.

## 6. 장점

- 중복 코드를 줄일 수 있다.
- 알고리즘의 전체 순서를 한 곳에서 관리할 수 있다.
- 자식 클래스는 달라지는 부분만 구현하면 된다.
- 공통 흐름이 쉽게 바뀌지 않도록 `final`로 보호할 수 있다.


## 7. 단점

- 상속을 사용하기 때문에 부모 클래스와 자식 클래스의 결합이 생긴다.
- 자식 클래스가 많아지면 구조가 복잡해질 수 있다.
- 부모 클래스의 흐름을 이해해야 자식 클래스를 제대로 구현할 수 있다.

## 8. 핵심 정리

템플릿 메소드 패턴은 **공통된 알고리즘 순서를 부모 클래스에 두고, 달라지는 세부 단계만 자식 클래스에서 구현하는 패턴**이다.

한마디로 정리하면 다음과 같다.

> 전체 흐름은 고정하고, 세부 구현만 자식 클래스가 바꾸는 패턴