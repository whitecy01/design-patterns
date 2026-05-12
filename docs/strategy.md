# Strategy Pattern

## 1. 문제 상황

오리 프로그램을 만든다고 생각해보자. 처음에는 모든 오리가 공통으로 가지는 기능만 있었다.
```java
public class Duck {

    public void quack() {
        System.out.println("꽥꽥");
    }

    public void swim() {
        System.out.println("수영합니다");
    }
    public void display() {
        System.out.println("오리입니다");
    }

}
```
그런데 요구사항이 추가되었다. 오리마다 나는 방식이 달라야 한다.

예를 들면 다음과 같다.
* 청둥오리는 날개로 난다.
* 고무오리는 날 수 없다.
* 장난감 오리는 로켓으로 날 수도 있다.

처음에는 단순하게 Duck 클래스 안에서 오리 타입에 따라 분기하면 될 것 같다고 생각한다.
```java
public class Duck {

    private String type;

    public Duck(String type) {
        this.type = type;
    }

    public void fly() {
        if (type.equals("mallard")) {
            System.out.println("날개로 납니다");
        } else if (type.equals("rubber")) {
            System.out.println("못 납니다");
        } else if (type.equals("rocket")) {
            System.out.println("로켓으로 납니다");
        }
    }
}
```
하지만 이 방식은 오리 종류가 늘어나거나, 나는 방식이 바뀔 수록 문제가 생긴다.

## 2. 기존 코드의 문제점
위 코드의 가장 큰 문제는 변하는 행동이 Duck 클래스 내부에 고정되어 있다는 점이다. 현재 Duck 클래스는 오리 자체를 표현하는 역할뿐만 아니라, 오리마다 어떻게 나는지도 직접 판단하고 있다.
```java
public void fly() {
    if (type.equals("mallard")) {
    System.out.println("날개로 납니다");
    } else if (type.equals("rubber")) {
    System.out.println("못 납니다");
    } else if (type.equals("rocket")) {
    System.out.println("로켓으로 납니다");
    }
}
```
이렇게 되면 다음과 같은 문제가 생긴다.

### 2.1 새로운 나는 방식이 추가될 때마다 기존 코드를 수정해야함.
예를 들어 글라이더처럼 나는 오리가 추가되면 fly() 메서드에 새로운 조건문을 추가해야 한다.
```java
else if (type.equals("glider")) {
    System.out.println("글라이더처럼 납니다");
}
```
즉, **새로운 기능을 추가할 때마다 기존 Duck 클래스를 계속 수정해야 한다.** 이는 변경에 취약한 구조다.

### 2.2 조건문이 계속 늘어난다.
오리 종류가 몇 개 없을 때는 괜찮아 보일 수 있다.

하지만 오리 종류와 나는 방식이 많아지면 if-else가 계속 늘어난다.
```java
if (type.equals("mallard")) {
        ...
        } else if (type.equals("rubber")) {
        ...
        } else if (type.equals("rocket")) {
        ...
        } else if (type.equals("glider")) {
        ...
        } else if (type.equals("drone")) {
        ...
        }
}
```
이렇게 되면 코드를 읽기도 어렵고, 수정하기도 어려워짐.

### 2.3 Duck 클래스가 너무 많은 책임을 가진다

Duck은 원래 오리를 표현하는 클래스다. 그런데 위 코드에서는 Duck이 다음 책임까지 가지고 있다.
* 오리 타입을 판단한다.
* 타입에 따라 나는 방식을 결정한다.
* 실제 나는 행동을 수행한다.

즉, Duck 클래스가 너무 많은 일을 하고 있다. 객체지향적으로 보면, 변하는 행동은 별도로 분리하는 것이 좋다.

## 3. 해결 방향
문제를 다시 보면 핵심은 이것이다.

> 오리 자체는 유지되지만, 나는 방식은 계속 바뀔 수 있다.

즉, 변하는 부분은 Duck이 아니라 나는 행동이다. 따라서 해결 방향은 다음과 같다.

> 변하는 행동인 fly()를 Duck 클래스 안에 직접 넣지 말고, 별도의 객체로 분리하자.

다시 말해, 나는 방식을 각각의 클래스로 분리한다.

* 날개로 나는 행동 
* 못 나는 행동 
* 로켓으로 나는 행동
그리고 Duck은 구체적인 나는 방식을 직접 알지 않고, 공통 인터페이스에만 의존하게 만든다.

## 4. 나는 행동을 인터페이스로 분리하기
먼저 나는 행동을 표현하는 인터페이스를 만든다.
```java
public interface FlyBehavior {
    void fly();
}
```
이 인터페이스는 “나는 행동”이라는 역할만 정의한다. 구체적으로 어떻게 나는지는 이 인터페이스를 구현하는 클래스들이 결정한다.

## 5. 나는 방식별 구현 클래스 만들기
이제 나는 방식들을 각각 별도의 클래스로 만든다.

### 5.1 날개로 나는 행동
```java
public class FlyWithWings implements FlyBehavior {

    @Override
    public void fly() {
        System.out.println("날개로 납니다");
    }
}
```

### 5.2 날 수 없는 행동
```java
public class FlyNoWay implements FlyBehavior {

    @Override
    public void fly() {
        System.out.println("못 납니다");
    }
}
```

### 5.3 로켓으로 나는 행동
```java
public class FlyRocketPowered implements FlyBehavior {

    @Override
    public void fly() {
        System.out.println("로켓으로 납니다");
    }
}
```
여기서 각각의 클래스는 FlyBehavior를 구현하고 있다.

즉, 모두 같은 방식으로 사용할 수 있다.

```java
FlyBehavior behavior = new FlyWithWings();
behavior.fly();

behavior = new FlyNoWay();
behavior.fly();

behavior = new FlyRocketPowered();
behavior.fly();
```
중요한 점은 사용하는 쪽에서 구체 클래스가 아니라 FlyBehavior라는 상위 타입으로 바라본다는 것이다.

## 6. Duck은 나는 행동을 직접 구현하지 않는다.
이제 Duck 클래스는 나는 방식을 직접 구현하지 않는다. 대신 FlyBehavior 타입의 필드를 가진다.
```java
public abstract class Duck {

    private FlyBehavior flyBehavior;

    public Duck(FlyBehavior flyBehavior) {
        this.flyBehavior = flyBehavior;
    }

    public void performFly() {
        flyBehavior.fly();
    }

    public void setFlyBehavior(FlyBehavior flyBehavior) {
        this.flyBehavior = flyBehavior;
    }

    public abstract void display();
}
```
여기서 중요한 코드는 다음과 같다.
```java
private FlyBehavior flyBehavior;
```
Duck은 FlyWithWings, FlyNoWay, FlyRocketPowered를 직접 의존하지 않는다. 대신 FlyBehavior라는 상위 타입에 의존한다. 즉, Duck은 이렇게 생각한다.

> 나는 어떻게 나는지는 몰라도 된다. 
> 나는 FlyBehavior에게 fly()를 요청하면 된다.

실제 나는 방식은 Duck이 아니라, 주입된 FlyBehavior 구현체가 결정한다.


## 7. 구체적인 오리 만들기
이제 각각의 오리는 자신에게 맞는 행동을 주입받으면 된다.

### 7.1 청둥 오리
```java
public class MallardDuck extends Duck {

    public MallardDuck() {
        super(new FlyWithWings());
    }

    @Override
    public void display() {
        System.out.println("저는 청둥오리입니다");
    }
}
```
청둥오리는 날개로 날기 때문에 FlyWithWings를 사용한다.

### 7.2. 고무오리
```java
public class RubberDuck extends Duck {

    public RubberDuck() {
        super(new FlyNoWay());
    }

    @Override
    public void display() {
        System.out.println("저는 고무오리입니다");
    }
}
```
고무오리는 날 수 없기 때문에 FlyNoWay를 사용한다.

## 8. 실행 코드
```java
public class Main {

    public static void main(String[] args) {
        Duck mallard = new MallardDuck();
        mallard.display();
        mallard.performFly();

        Duck rubberDuck = new RubberDuck();
        rubberDuck.display();
        rubberDuck.performFly();

        rubberDuck.setFlyBehavior(new FlyRocketPowered());
        rubberDuck.performFly();
    }
}
```
실행 결과는 다음과 같다.
```text
저는 청둥오리입니다 
날개로 납니다 
저는 고무오리입니다 
못 납니다 
로켓으로 납니다
```
여기서 중요한 부분은 다음 코드다.
```java
rubberDuck.setFlyBehavior(new FlyRocketPowered());
```
처음에 고무오리는 FlyNoWay 전략을 사용해서 날 수 없었다. 하지만 실행 중에 FlyRocketPowered로 나는 방식을 교체했다. 그래서 이후에는 로켓으로 날 수 있게 된다.


## 9. 어떤 부분이 전략 패턴인가?

이제 이 구조에서 어떤 부분이 전략 패턴인지 정리해보자. 전략 패턴은 보통 다음 구조를 가진다.

```text
Context
Strategy
ConcreteStrategy
```

오리 예제에서는 다음과 같이 대응된다.

| 전략 패턴 구성 요소 | 오리 예제 |
|---|---|
| Context | Duck |
| Strategy | FlyBehavior |
| ConcreteStrategy | FlyWithWings, FlyNoWay, FlyRocketPowered |

### 9.1 Context

`Context`는 전략을 사용하는 객체다.

여기서는 `Duck`이 Context다.

```java
public abstract class Duck {

    private FlyBehavior flyBehavior;

    public Duck(FlyBehavior flyBehavior) {
        this.flyBehavior = flyBehavior;
    }

    public void performFly() {
        flyBehavior.fly();
    }

    public void setFlyBehavior(FlyBehavior flyBehavior) {
        this.flyBehavior = flyBehavior;
    }

    public abstract void display();
}
```

`Duck`은 직접 나는 방식을 구현하지 않는다. 대신 `FlyBehavior` 타입의 필드를 가지고 있다가, `performFly()`가 호출되면 실제 나는 행동을 `flyBehavior`에게 위임한다.

```java
public void performFly() {
    flyBehavior.fly();
}
```

즉, `Duck`은 이렇게 말하는 구조다.

```text
나는 어떻게 나는지는 모른다. 다만 FlyBehavior에게 fly()를 요청할 뿐이다.
```

이렇게 하면 `Duck`은 `FlyWithWings`, `FlyNoWay`, `FlyRocketPowered` 같은 구체 클래스를 직접 알 필요가 없다.


### 9.2 Strategy

`Strategy`는 변하는 행동을 정의하는 인터페이스다.

오리 예제에서는 `FlyBehavior`가 Strategy다.

```java
public interface FlyBehavior {
    void fly();
}
```

여기서 `FlyBehavior`는 “나는 행동”을 추상화한다. 즉, 구체적으로 날개로 나는지, 못 나는지, 로켓으로 나는지는 정의하지 않는다. 그저 다음 규칙만 정한다.

```text
나는 행동을 하려면 fly()를 구현해야 한다.
```

이 인터페이스 덕분에 `Duck`은 구체적인 나는 방식에 의존하지 않고, 공통 타입인 `FlyBehavior`에만 의존할 수 있다.

### 9.3 ConcreteStrategy

`ConcreteStrategy`는 `Strategy` 인터페이스를 실제로 구현한 클래스다. 오리 예제에서는 다음 클래스들이 ConcreteStrategy다.

```java
public class FlyWithWings implements FlyBehavior {

    @Override
    public void fly() {
        System.out.println("날개로 납니다");
    }
}
```

```java
public class FlyNoWay implements FlyBehavior {

    @Override
    public void fly() {
        System.out.println("못 납니다");
    }
}
```

```java
public class FlyRocketPowered implements FlyBehavior {

    @Override
    public void fly() {
        System.out.println("로켓으로 납니다");
    }
}
```

각 클래스는 같은 `FlyBehavior` 인터페이스를 구현하지만, 실제 동작은 다르다.

| 클래스 | 실제 동작 |
|---|---|
| FlyWithWings | 날개로 난다 |
| FlyNoWay | 날 수 없다 |
| FlyRocketPowered | 로켓으로 난다 |

즉, 전략 패턴에서 말하는 “전략”은 바로 이런 구현 클래스들이다.


## 12. 전략 패턴 한 문장 정리
전략 패턴은 다음과 같이 정리할 수 있다.

> 변하는 행동을 인터페이스로 분리하고, 실제 행동을 구현 클래스로 만들어 실행 시점에 교체할 수 있게 하는 패턴이다.

조금 더 쉽게 말하면 다음과 같다.

> `if-else`로 행동을 고정하지 말고, 행동 자체를 객체로 만들어 갈아끼우는 방식이다.
