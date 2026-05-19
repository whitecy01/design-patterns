# 싱글턴 패턴

## 1. 정의

싱글턴 패턴은 **특정 클래스의 인스턴스가 오직 하나만 생성되도록 보장하는 생성 패턴**이다. 즉, 프로그램 전체에서 하나의 객체만 공유해서 사용하고 싶을 때 사용한다.

> 클래스 인스턴스를 하나만 만들고, 그 인스턴스로의 전역 접근을 제공한다.


## 2. 어떤 상황에서 사용하는가?

싱글턴 패턴은 다음과 같이 **하나만 존재해야 자연스러운 객체**에 사용된다.

- 설정 정보 관리 객체
- 로그 기록 객체
- DB 커넥션 풀
- 스레드 풀
- 캐시 객체
- 애플리케이션 전체에서 공유하는 상태 관리 객체

예를 들어 로그를 기록하는 객체가 여러 개 만들어지면 로그 파일 관리가 복잡해질 수 있다. 또한 설정 정보 객체가 여러 개 만들어지면 서로 다른 설정 값을 가질 위험이 있다. 그래서 이런 경우에는 객체를 하나만 만들고 여러 곳에서 공유하도록 한다.

## 3. 기본 구조

싱글턴 패턴의 핵심은 다음 3가지이다.

1. 생성자를 `private`으로 막는다.
2. 클래스 내부에서 자기 자신의 인스턴스를 가진다.
3. 외부에서는 `getInstance()` 메서드로만 인스턴스에 접근한다.

```java
public class Singleton {
    private static Singleton uniqueInstance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Singleton();
        }

        return uniqueInstance;
    }
}
```
사용하는 쪽에서는 다음처럼 객체를 직접 생성하지 않고 가져와서 사용한다.

## 4. 왜 생성자를 private으로 만드는가?
싱글턴은 인스턴스를 하나만 만들어야 한다. 그런데 생성자가 public이면 외부에서 마음대로 객체를 만들 수 있다.
```java
Singleton s1 = new Singleton();
Singleton s2 = new Singleton();
```
이렇게 되면 객체가 여러 개 만들어질 수 있으므로 싱글턴이 깨진다.

그래서 생성자를 private으로 막아 외부에서 new를 사용할 수 없게 만든다.
```java
private Singleton() {
}
```
그 대신 객체 생성은 클래스 내부의 getInstance() 메서드가 담당한다.

## 5. 기본 싱글턴의 문제점
아래 코드는 단일 스레드 환경에서는 문제가 없다.
```java
public class Singleton {
    private static Singleton uniqueInstance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Singleton();
        }

        return uniqueInstance;
    }
}
```
하지만 멀티스레드 환경에서는 문제가 생길 수 있다.

예를 들어 두 개의 스레드가 거의 동시에 getInstance()를 호출했다고 하자.

```java
Thread A: uniqueInstance == null 확인
Thread B: uniqueInstance == null 확인

Thread A: new Singleton()
Thread B: new Singleton()
```
둘 다 null이라고 판단하면 객체가 2개 만들어질 수 있다. 즉, 싱글턴 패턴의 핵심인 인스턴스가 하나만 존재해야 한다는 조건이 깨질 수 있다.

## 6. synchronized를 사용한 싱글턴

멀티스레드 환경에서 안전하게 만들기 위해 synchronized를 사용할 수 있다.
```java
public class Singleton {
    private static Singleton uniqueInstance;

    private Singleton() {
    }

    public static synchronized Singleton getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Singleton();
        }

        return uniqueInstance;
    }
}
```
## 7. synchronized 방식의 단점

synchronized를 사용하면 멀티스레드 환경에서 안전하다. 하지만 단점도 있다. getInstance()는 객체가 처음 생성될 때만 동기화가 필요하다. 객체가 이미 만들어진 후에는 단순히 기존 객체를 반환하면 된다.

그런데 메서드 전체에 synchronized를 붙이면 객체가 이미 만들어진 이후에도 계속 동기화 비용이 발생한다. 즉, 처음 한 번만 필요한 동기화가 매번 발생한다.

그래서 성능이 중요한 상황에서는 다른 방식도 고려할 수 있다.

## 8. 즉시 생성 방식

클래스가 로딩될 때 인스턴스를 미리 만들어두는 방식이다.
```java 
public class Singleton {
    private static final Singleton uniqueInstance = new Singleton();

    private Singleton() {
    }

    public static Singleton getInstance() {
        return uniqueInstance;
    }
}
```

이 방식은 클래스가 로딩될 때 객체가 바로 생성된다. 멀티스레드 환경에서도 안전하고 코드도 단순하다. 다만, 실제로 사용하지 않더라도 객체가 미리 만들어진다는 특징이 있다.

## 9. Double-Checked Locking 방식

객체가 없을 때만 동기화하도록 최적화한 방식이다.
```java
public class Singleton {
    private volatile static Singleton uniqueInstance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (uniqueInstance == null) {
            synchronized (Singleton.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new Singleton();
                }
            }
        }

        return uniqueInstance;
    }
}
```
이 방식은 먼저 인스턴스가 있는지 확인한다.
```java
if (uniqueInstance == null)
```
객체가 없을 때만 synchronized 블록에 들어간다. 그리고 동기화 블록 안에서 다시 한 번 확인한다.

## 10. 정리

싱글턴 패턴은 객체를 하나만 생성하고 공유해야 할 때 사용하는 패턴이다. 생성자를 private으로 막고, getInstance() 메서드를 통해 하나의 인스턴스만 반환하도록 만든다. 단일 스레드 환경에서는 기본 구현으로도 충분하지만, 멀티스레드 환경에서는 여러 객체가 생성될 수 있으므로 동기화 처리가 필요하다.

가장 단순하게는 synchronized를 사용할 수 있고, 성능을 고려하면 즉시 생성 방식이나 Double-Checked Locking 방식을 사용할 수 있다.

