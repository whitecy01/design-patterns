# 어댑터 패턴

## 1. 정의

어댑터 패턴은 **기존 클래스의 인터페이스를 클라이언트가 원하는 인터페이스로 변환해주는 패턴**이다. 즉, 서로 맞지 않는 인터페이스를 가진 객체들이 함께 동작할 수 있도록 중간에서 연결해준다.
> 특정 클래스 인터페이스를 클라이언트에서 요구하느 다른 인터페이스로 변환합니다. 인터페이스가 호환되지 않아 같이 쓸 수 없었던 클래스를 사용할 수 있게 도와줍니다.

## 2. 언제 사용하는가?

기존에 만들어진 클래스를 사용하고 싶은데, 클라이언트가 기대하는 인터페이스와 맞지 않을 때 사용한다.

예를 들어 클라이언트는 `Duck` 인터페이스만 사용할 수 있는데, 실제로 가지고 있는 객체는 `Turkey`라면 바로 사용할 수 없다.

이때 `TurkeyAdapter`를 만들어 `Turkey`를 `Duck`처럼 사용할 수 있게 한다.


## 3. 구조

| 역할 | 설명 | 예시 |
|---|---|---|
| Target | 클라이언트가 기대하는 인터페이스 | Duck |
| Adapter | Target 인터페이스에 맞춰 변환하는 클래스 | TurkeyAdapter |
| Adaptee | 기존에 존재하던 클래스 또는 인터페이스 | Turkey |
| Client | Target 인터페이스를 사용하는 쪽 | DuckTestDrive |


## 4. 예제 흐름

```java
Duck duck = new TurkeyAdapter(turkey);
duck.quack();
duck.fly();
```

클라이언트는 `Duck`을 사용하는 것처럼 보이지만,  
실제로 내부에서는 `Turkey` 객체가 동작한다.

```java
public class TurkeyAdapter implements Duck {

    private Turkey turkey;

    public TurkeyAdapter(Turkey turkey) {
        this.turkey = turkey;
    }

    @Override
    public void quack() {
        turkey.gobble();
    }

    @Override
    public void fly() {
        turkey.fly();
    }
}
```

## 5. 장점
- 기존 코드를 수정하지 않고 재사용할 수 있다.
- 인터페이스가 맞지 않는 클래스들을 연결할 수 있다.
- 클라이언트는 실제 객체가 무엇인지 몰라도 Target 인터페이스만 사용하면 된다.


## 6. 단점
- 어댑터 클래스가 추가되므로 구조가 조금 복잡해질 수 있다.
- 변환해야 할 메서드가 많으면 어댑터 코드가 길어질 수 있다.

## 7. 핵심 정리

어댑터 패턴은 **기존 객체를 클라이언트가 원하는 형태로 감싸서 사용할 수 있게 만드는 패턴**이다.

한마디로 정리하면 다음과 같다.

> 인터페이스가 맞지 않는 객체를 중간에서 변환해주는 패턴