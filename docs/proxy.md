# 프록시 패턴

## 1. 정의

프록시 패턴은 **실제 객체 대신 대리 객체를 두고, 실제 객체에 대한 접근을 제어하는 패턴**이다. 즉, 클라이언트가 실제 객체를 직접 사용하는 것이 아니라 프록시 객체를 통해 사용한다.

> 특정 객촐의 접근을 제어하는 대리인(특정 객체를 대변하는 객체)을 제공합니다.


## 2. 언제 사용하는가?

실제 객체에 바로 접근하지 않고, 중간에서 접근을 제어해야 할 때 사용한다. 예를 들어 다음과 같은 상황에서 사용할 수 있다.

```text
1. 실제 객체가 원격 서버에 있는 경우
2. 실제 객체를 생성하는 비용이 큰 경우
3. 접근 권한을 검사해야 하는 경우
4. 실제 객체 사용 전후에 부가 작업이 필요한 경우
```

헤드퍼스트 예제에서는 **껌볼 머신을 원격에서 모니터링하는 상황**으로 설명한다. 클라이언트는 실제 껌볼 머신이 어디에 있는지 몰라도 된다.

```java
monitor.report();
```

내부에서는 프록시가 원격 껌볼 머신에 접근한다.

## 3. 구조

| 역할 | 설명 | 예시 |
|---|---|---|
| Subject | RealSubject와 Proxy가 공통으로 따르는 인터페이스 | GumballMachineRemote |
| RealSubject | 실제 작업을 수행하는 객체 | GumballMachine |
| Proxy | RealSubject에 대한 접근을 대신 처리하는 객체 | RMI Stub |
| Client | Proxy를 통해 객체를 사용하는 쪽 | GumballMonitor |

## 4. 예제 상황

껌볼 회사는 여러 지점에 있는 껌볼 머신의 상태를 원격으로 확인하고 싶다. 확인하고 싶은 정보는 다음과 같다.

```text
1. 껌볼 머신 위치
2. 남은 껌볼 개수
3. 현재 상태
```

하지만 클라이언트가 원격 서버의 `GumballMachine` 객체에 직접 접근할 수는 없다. 그래서 Java RMI가 만들어주는 프록시를 통해 접근한다.

```text
GumballMonitor
    ↓
GumballMachineRemote
    ↓
RMI Stub / Proxy
    ↓
Remote GumballMachine
```
## 5. 서버와 클라이언트 구조

프록시 패턴의 원격 프록시 예제는 서버와 클라이언트로 나누어 생각해야 한다.

```text
server
- 실제 GumballMachine 객체를 생성한다.
- RMI Registry에 객체를 등록한다.

client
- RMI Registry에서 원격 객체를 찾아온다.
- GumballMonitor가 프록시를 통해 원격 객체를 호출한다.
```

폴더 구조는 다음과 같이 나눌 수 있다.

```text
proxy/
├── common/
│   ├── GumballMachineRemote.java
│   └── State.java
│
├── server/
│   ├── GumballMachine.java
│   ├── NoQuarterState.java
│   ├── HasQuarterState.java
│   ├── SoldState.java
│   ├── SoldOutState.java
│   └── GumballMachineServer.java
│
└── client/
    ├── GumballMonitor.java
    └── GumballMonitorClient.java
```

## 6. 예제 흐름

`GumballMachineRemote`는 서버와 클라이언트가 공통으로 사용하는 원격 인터페이스이다.

```java
public interface GumballMachineRemote extends Remote {

    int getCount() throws RemoteException;

    String getLocation() throws RemoteException;

    State getState() throws RemoteException;
}
```

`State`는 클라이언트에게 전달될 수 있으므로 `Serializable`을 구현한다.

```java
public interface State extends Serializable {

    void insertQuarter();

    void ejectQuarter();

    void turnCrank();

    void dispense();
}
```

`GumballMachine`은 실제 객체이다.

```java
public class GumballMachine extends UnicastRemoteObject implements GumballMachineRemote {

    private State state;
    private int count;
    private String location;

    public GumballMachine(String location, int count) throws RemoteException {
        this.location = location;
        this.count = count;
    }

    @Override
    public int getCount() throws RemoteException {
        return count;
    }

    @Override
    public String getLocation() throws RemoteException {
        return location;
    }

    @Override
    public State getState() throws RemoteException {
        return state;
    }
}
```

서버는 실제 껌볼 머신 객체를 만들고 RMI Registry에 등록한다.

```java
public class GumballMachineServer {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);

            GumballMachineRemote machine =
                    new GumballMachine("서울 강남점", 5);

            Naming.rebind("rmi://localhost/GumballMachine", machine);

            System.out.println("껌볼 머신 서버가 실행되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

클라이언트는 RMI Registry에서 원격 객체를 찾아온다.

```java
public class GumballMonitorClient {

    public static void main(String[] args) {
        try {
            GumballMachineRemote machine =
                    (GumballMachineRemote) Naming.lookup("rmi://localhost/GumballMachine");

            GumballMonitor monitor = new GumballMonitor(machine);
            monitor.report();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

`GumballMonitor`는 실제 객체가 로컬에 있는지 원격에 있는지 신경 쓰지 않는다.

```java
public class GumballMonitor {

    private GumballMachineRemote machine;

    public GumballMonitor(GumballMachineRemote machine) {
        this.machine = machine;
    }

    public void report() {
        try {
            System.out.println("껌볼 머신 위치: " + machine.getLocation());
            System.out.println("현재 재고: " + machine.getCount() + "개");
            System.out.println("현재 상태: " + machine.getState());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
```

클라이언트는 단순히 메서드를 호출한다.

```java
machine.getLocation();
machine.getCount();
machine.getState();
```

하지만 실제로는 RMI 프록시가 네트워크 통신을 대신 처리한다.


## 7. 프록시 패턴의 종류

| 종류 | 설명 | 예시 |
|---|---|---|
| 원격 프록시 | 원격 객체에 대한 접근을 대신 처리 | RMI, 원격 서버 객체 |
| 가상 프록시 | 비용이 큰 객체 생성을 필요한 순간까지 미룸 | 이미지 로딩, JPA Lazy Loading |
| 보호 프록시 | 접근 권한을 검사 | 권한별 메서드 제한 |
| 동적 프록시 | 런타임에 프록시 객체 생성 | Java Dynamic Proxy, Spring AOP |

## 8. JPA와 프록시

JPA의 지연 로딩은 프록시 패턴의 대표적인 예시이다.

```java
Team team = member.getTeam();
```

이때 `team`은 실제 `Team` 객체가 아니라 JPA가 만든 프록시 객체일 수 있다.

```java
team.getName();
```

실제 데이터가 필요한 순간에 DB 조회가 발생한다.

이것은 목적상 **가상 프록시**에 가깝다.

```text
가상 프록시 = 실제 객체 생성이나 조회를 필요한 순간까지 미룬다
```

Hibernate 같은 JPA 구현체는 이를 동적 프록시나 바이트코드 기반 프록시 방식으로 구현한다.

```text
가상 프록시 = 목적
동적 프록시 = 구현 방법
```

## 9. 장점

- 실제 객체에 대한 접근을 제어할 수 있다.
- 클라이언트는 실제 객체가 어디에 있는지 몰라도 사용할 수 있다.
- 원격 호출, 지연 로딩, 권한 검사 같은 기능을 자연스럽게 추가할 수 있다.
- 실제 객체와 클라이언트 사이의 결합을 줄일 수 있다.


## 10. 단점

- 프록시 클래스가 추가되어 구조가 복잡해질 수 있다.
- 원격 프록시의 경우 네트워크 예외 처리가 필요하다.
- 실제 객체를 직접 호출하는 것보다 성능 비용이 생길 수 있다.
- 프록시가 내부에서 어떤 작업을 하는지 모르면 동작을 이해하기 어려울 수 있다.


## 11. 핵심 정리

프록시 패턴은 **실제 객체 대신 대리 객체를 두고, 실제 객체에 대한 접근을 제어하는 패턴**이다.

헤드퍼스트의 껌볼 머신 예제는 **원격 프록시** 예제이다.

```text
클라이언트는 로컬 객체처럼 호출하지만,
실제 호출은 RMI 프록시가 네트워크 너머의 GumballMachine에 전달한다.
```

한마디로 정리하면 다음과 같다.

> 실제 객체에 바로 접근하지 않고, 대리 객체를 통해 접근을 제어하는 패턴