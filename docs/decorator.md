# 데코레이터 패턴

## 1. 데코레이터 패턴의 정의

데코레이터 패턴은 객체를 감싸는 방식으로 기능을 동적으로 추가하는 구조 패턴이다.
> 객체에 추가 요소를 동적으로 더할 수 있다. 데코레이터를 사용하면 서브 클래스를 만들 떄보다 훨씬 유연하게 기능을 확장할 수 있다.

- 기존 객체의 코드를 직접 수정하지 않고, 같은 상위 타입을 가진 데코레이터 객체로 감싸서 새로운 책임이나 기능을 추가한다. 
- 즉, 상속으로 기능을 확장하는 것이 아니라 객체 조합을 통해 기능을 확장하는 방식이다.

예를 들어 기본 음료 객체가 있을 때, 모카, 휘핑, 샷 추가와 같은 옵션을 각각 데코레이터로 만들어 기존 음료 객체를 감싸면 기능과 가격을 유연하게 추가할 수 있다.

## 2. 어떤 상황일 때 사용하는가

데코레이터 패턴은 기존 객체에 기능을 추가해야 하지만, 기존 클래스를 직접 수정하고 싶지 않을 때 사용한다.

특히 기능 조합이 많아져서 상속만으로 처리하기 어려운 경우에 적합하다.

예를 들어 커피 주문 시스템에서 다음과 같은 조합이 있다고 가정한다.

```text
다크로스트
다크로스트 + 모카
다크로스트 + 휘핑
다크로스트 + 모카 + 휘핑
다크로스트 + 모카 + 모카 + 휘핑
```

이 조합을 모두 상속으로 만들면 클래스 수가 계속 증가한다.
- DarkRoast 
- DarkRoastWithMocha 
- DarkRoastWithWhip 
- DarkRoastWithMochaAndWhip 
- DarkRoastWithDoubleMochaAndWhip

이런 문제를 데코레이터 패턴은 객체를 감싸는 방식으로 해결한다.
```java
Beverage beverage = new DarkRoast();
beverage = new Mocha(beverage);
beverage = new Whip(beverage);
```

이처럼 실행 중에 필요한 기능만 조립해서 사용할 수 있다.

데코레이터 패턴은 다음과 같은 상황에서 사용하기 좋다.

* 기존 클래스를 수정하지 않고 기능을 추가하고 싶을 때
* 기능 조합이 많아 상속으로 처리하면 클래스가 너무 많아질 때
* 실행 중에 기능을 동적으로 추가하거나 제거하고 싶을 때
* 부가 기능을 객체 앞뒤에 덧붙이고 싶을 때
* 객체의 핵심 기능과 부가 기능을 분리하고 싶을 때

## 3. 어떤 패턴으로 적용되는가
데코레이터 패턴은 기본 객체와 데코레이터가 같은 상위 타입을 공유하는 구조로 적용된다.
구조는 다음과 같다.
```text
Component
├── ConcreteComponent
└── Decorator
    └── ConcreteDecorator
```
각 역할은 다음과 같다.

- Component 
  - 공통 상위 타입이다. 
  - 기본 객체와 데코레이터가 모두 이 타입을 따른다. 
예시로는 Beverage, 자바 I/O에서는 InputStream이 여기에 해당한다.
- ConcreteComponent 
  - 실제 핵심 기능을 가진 원본 객체이다. 
  - 예시로는 DarkRoast, Espresso, 자바 I/O에서는 FileInputStream이 여기에 해당한다. 
- Decorator 
  - 데코레이터들의 공통 부모 역할을 한다. 
  - 내부에 자신이 감싸는 Component 객체를 가지고 있다.
```text
public abstract class CondimentDecorator extends Beverage {
    Beverage beverage;
}
```

- ConcreteDecorator 
  - 실제로 기능을 추가하는 데코레이터이다. 
  - 예시로는 Mocha, Whip, 자바 I/O에서는 BufferedInputStream, ZipInputStream이 여기에 해당한다. 
  - Mocha는 내부에 감싸고 있는 음료 객체를 가지고 있다가, 기존 설명과 가격에 모카 설명과 가격을 추가한다.
```text
public double cost() {
  return beverage.cost() + 0.20;
}
```
즉, beverage.cost()를 통해 자신이 감싸고 있는 객체의 비용을 먼저 구한 뒤, 자신의 추가 비용을 더한다.

예를 들어 다음과 같이 감싸면:
```text
Beverage beverage = new DarkRoast();
beverage = new Mocha(beverage);
beverage = new Whip(beverage);
```
객체 구조는 다음과 같다.
```text
Whip
└── Mocha
    └── DarkRoast
```

cost() 호출 흐름은 다음과 같다.
```text
Whip.cost()
    -> Mocha.cost()
        -> DarkRoast.cost()
```

계산은 다음과 같이 이루어진다.
```text
DarkRoast 가격 + Mocha 가격 + Whip 가격
```

자바 I/O도 같은 구조를 가진다.
```java
InputStream in =
    new ZipInputStream(
        new BufferedInputStream(
            new FileInputStream("data.zip")
        )
    );
```

역할 대응은 다음과 같다.
- Beverage = InputStream 
- DarkRoast = FileInputStream 
- CondimentDecorator = FilterInputStream 
- Mocha, Whip  = BufferedInputStream, ZipInputStream

즉, FileInputStream이라는 원본 객체를 BufferedInputStream, ZipInputStream 같은 구상 데코레이터가 감싸면서 기능을 추가하는 구조이다.

## 4. 장점 및 단점
장점
1. 기존 코드를 수정하지 않고 기능을 추가할 수 있다
- 데코레이터 패턴은 기존 객체를 직접 수정하지 않고 새로운 데코레이터 클래스를 추가하여 기능을 확장한다. 
- 따라서 객체지향 원칙 중 OCP, 즉 개방-폐쇄 원칙을 잘 만족한다.
> 확장에는 열려 있고, 변경에는 닫혀 있다.

2. 상속보다 유연하다
- 상속을 사용하면 기능 조합마다 새로운 클래스를 만들어야 한다. 
- 하지만 데코레이터 패턴은 객체를 조합하는 방식이기 때문에 필요한 기능만 선택적으로 붙일 수 있다.
```java
new Whip(new Mocha(new DarkRoast()))
```
이처럼 실행 중에도 원하는 조합을 만들 수 있다.

3. 책임을 분리할 수 있다
- 기본 객체는 자신의 핵심 기능만 담당하고, 부가 기능은 데코레이터가 담당한다. 
- 예를 들어 DarkRoast는 기본 음료의 가격만 알고, Mocha는 모카 추가 비용만 알고, Whip은 휘핑 추가 비용만 알면 된다. 
- 각 클래스의 책임이 작고 명확해진다.

단점
1. 객체 수가 많아질 수 있다
- 기능을 추가할 때마다 데코레이터 객체로 감싸기 때문에 객체가 여러 겹으로 쌓인다.
```java
new Whip(new Mocha(new Mocha(new DarkRoast())))
```
구조가 깊어지면 처음 보는 사람은 흐름을 이해하기 어려울 수 있다.

2. 코드가 복잡해 보일 수 있다
단순히 객체 하나를 생성하는 것이 아니라 여러 데코레이터로 감싸야 하기 때문에 생성 코드가 복잡해질 수 있다.
```java
Beverage beverage = new Whip(new Mocha(new DarkRoast()));
```
이런 구조는 익숙하지 않으면 어떤 순서로 기능이 적용되는지 헷갈릴 수 있다.

3. 데코레이터 순서에 따라 결과가 달라질 수 있다
- 대부분의 경우 단순히 가격이나 설명을 추가하는 정도라면 순서가 크게 중요하지 않다. 
- 하지만 압축, 암호화, 버퍼링처럼 기능의 순서가 중요한 경우에는 데코레이터를 감싸는 순서를 주의해야 한다. 
- 예를 들어 파일 처리에서는 다음과 같은 순서가 의미를 가진다.
```text
파일 읽기 -> 버퍼링 -> 압축 해제
```
순서를 잘못 구성하면 의도한 대로 동작하지 않을 수 있다.

# 정리
- 데코레이터 패턴은 객체를 감싸서 기능을 동적으로 추가하는 패턴이다. 
- 기존 클래스를 수정하지 않고 기능을 확장할 수 있기 때문에 OCP를 만족하는 대표적인 패턴이다. 
- 상속으로 기능 조합을 만들면 클래스가 폭발할 수 있지만, 데코레이터 패턴은 객체 조합을 통해 필요한 기능만 유연하게 붙일 수 있다. 
- 따라서 기능 조합이 많고, 기존 객체를 수정하지 않고 확장해야 하는 상황에서 유용하게 사용할 수 있다.

