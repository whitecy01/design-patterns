# 상태 패턴

## 1. 정의

상태 패턴은 **객체의 내부 상태가 바뀜에 따라 같은 행동 요청도 다르게 처리되도록 만드는 패턴**이다.  즉, 객체가 현재 어떤 상태인지에 따라 실행되는 행동이 달라진다.

> 이 패턴을 사용하면 객체의 내부 상태가 바뀜에 따라서 객체의 행동을 바꿀 수 있습니다. 미처 객체의 클래스가 바뀌는 것과 같은 결과를 얻을 수 있습니다.

## 2. 언제 사용하는가?

객체가 여러 상태를 가지고 있고, 상태에 따라 같은 메서드의 동작이 달라질 때 사용한다. 예를 들어 껌볼 머신에는 다음과 같은 상태가 있다.

```text
1. 동전 없음 상태
2. 동전 있음 상태
3. 판매 상태
4. 매진 상태
```

같은 `insertQuarter()` 메서드를 호출해도 현재 상태에 따라 결과가 달라진다.

```text
동전 없음 상태 → 동전을 받는다
동전 있음 상태 → 이미 동전이 있다고 말한다
매진 상태 → 동전을 넣을 수 없다고 말한다
```

이런 조건문을 `GumballMachine` 안에 모두 작성하면 코드가 복잡해진다. 상태 패턴은 상태별 행동을 각각의 클래스로 분리한다.


## 3. 구조

| 역할 | 설명 | 예시 |
|---|---|---|
| State | 상태들이 공통으로 구현해야 하는 인터페이스 | State |
| ConcreteState | 각 상태별 행동을 구현하는 클래스 | NoQuarterState, HasQuarterState, SoldState, SoldOutState |
| Context | 현재 상태를 가지고 있는 객체 | GumballMachine |
| Client | Context를 사용하는 객체 | GumballMachineTestDrive |

## 4. 예제 흐름

`State` 인터페이스는 껌볼 머신에서 가능한 행동을 정의한다.

```java
public interface State {
    void insertQuarter();
    void ejectQuarter();
    void turnCrank();
    void dispense();
}
```

`NoQuarterState`는 동전이 없는 상태를 의미한다.

```java
public class NoQuarterState implements State {

    private GumballMachine gumballMachine;

    public NoQuarterState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("동전을 넣었습니다.");
        gumballMachine.setState(gumballMachine.getHasQuarterState());
    }

    @Override
    public void ejectQuarter() {
        System.out.println("동전을 넣지 않았습니다.");
    }

    @Override
    public void turnCrank() {
        System.out.println("동전을 넣어야 합니다.");
    }

    @Override
    public void dispense() {
        System.out.println("동전을 넣어야 껌볼이 나옵니다.");
    }
}
```

동전을 넣으면 상태가 `NoQuarterState`에서 `HasQuarterState`로 바뀐다.

```java
gumballMachine.setState(gumballMachine.getHasQuarterState());
```

`HasQuarterState`는 동전이 들어있는 상태를 의미한다.

```java
public class HasQuarterState implements State {

    private GumballMachine gumballMachine;

    public HasQuarterState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("동전은 한 개만 넣어주세요.");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("동전이 반환됩니다.");
        gumballMachine.setState(gumballMachine.getNoQuarterState());
    }

    @Override
    public void turnCrank() {
        System.out.println("손잡이를 돌렸습니다.");
        gumballMachine.setState(gumballMachine.getSoldState());
    }

    @Override
    public void dispense() {
        System.out.println("아직 껌볼을 내보낼 수 없습니다.");
    }
}
```

손잡이를 돌리면 상태가 `SoldState`로 바뀐다.

```java
gumballMachine.setState(gumballMachine.getSoldState());
```

`SoldState`는 껌볼을 판매하는 상태를 의미한다.

```java
public class SoldState implements State {

    private GumballMachine gumballMachine;

    public SoldState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("잠깐만 기다려주세요. 이미 껌볼이 나가고 있습니다.");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("이미 손잡이를 돌렸습니다. 동전을 반환할 수 없습니다.");
    }

    @Override
    public void turnCrank() {
        System.out.println("손잡이는 한 번만 돌려주세요.");
    }

    @Override
    public void dispense() {
        gumballMachine.releaseBall();

        if (gumballMachine.getCount() > 0) {
            gumballMachine.setState(gumballMachine.getNoQuarterState());
        } else {
            System.out.println("더 이상 껌볼이 없습니다.");
            gumballMachine.setState(gumballMachine.getSoldOutState());
        }
    }
}
```

껌볼을 내보낸 뒤 남은 개수에 따라 상태가 달라진다.

```java
if (gumballMachine.getCount() > 0) {
    gumballMachine.setState(gumballMachine.getNoQuarterState());
} else {
    gumballMachine.setState(gumballMachine.getSoldOutState());
}
```

`SoldOutState`는 매진 상태를 의미한다.

```java
public class SoldOutState implements State {

    private GumballMachine gumballMachine;

    public SoldOutState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("껌볼이 매진되었습니다. 동전을 넣을 수 없습니다.");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("동전을 넣지 않았습니다.");
    }

    @Override
    public void turnCrank() {
        System.out.println("껌볼이 매진되었습니다.");
    }

    @Override
    public void dispense() {
        System.out.println("껌볼이 나갈 수 없습니다.");
    }
}
```

`GumballMachine`은 현재 상태를 가지고 있고, 실제 행동은 현재 상태 객체에게 위임한다.

```java
public class GumballMachine {

    private State soldOutState;
    private State noQuarterState;
    private State hasQuarterState;
    private State soldState;

    private State state;
    private int count = 0;

    public GumballMachine(int numberGumballs) {
        soldOutState = new SoldOutState(this);
        noQuarterState = new NoQuarterState(this);
        hasQuarterState = new HasQuarterState(this);
        soldState = new SoldState(this);

        this.count = numberGumballs;

        if (numberGumballs > 0) {
            state = noQuarterState;
        } else {
            state = soldOutState;
        }
    }

    public void insertQuarter() {
        state.insertQuarter();
    }

    public void ejectQuarter() {
        state.ejectQuarter();
    }

    public void turnCrank() {
        state.turnCrank();
        state.dispense();
    }

    void setState(State state) {
        this.state = state;
    }

    void releaseBall() {
        System.out.println("껌볼이 나옵니다.");

        if (count > 0) {
            count--;
        }
    }

    public int getCount() {
        return count;
    }

    public State getSoldOutState() {
        return soldOutState;
    }

    public State getNoQuarterState() {
        return noQuarterState;
    }

    public State getHasQuarterState() {
        return hasQuarterState;
    }

    public State getSoldState() {
        return soldState;
    }
}
```

여기서 핵심은 `GumballMachine`이 직접 조건문으로 상태를 판단하지 않는다는 점이다.

```java
public void insertQuarter() {
    state.insertQuarter();
}
```

현재 상태가 `NoQuarterState`면 `NoQuarterState.insertQuarter()`가 실행되고,  
현재 상태가 `SoldOutState`면 `SoldOutState.insertQuarter()`가 실행된다.

## 5. 상태 전환 흐름

```text
동전 없음 상태
    ↓ 동전 넣기
동전 있음 상태
    ↓ 손잡이 돌리기
판매 상태
    ↓ 껌볼 배출
동전 없음 상태 또는 매진 상태
```

상태 객체가 직접 다음 상태로 전환한다.

```java
gumballMachine.setState(gumballMachine.getHasQuarterState());
```

---

## 6. 장점

- 상태별 행동을 각각의 클래스로 분리할 수 있다.
- 복잡한 조건문을 줄일 수 있다.
- 새로운 상태를 추가하기 쉬워진다.
- 상태 전환 로직을 명확하게 표현할 수 있다.


## 7. 단점

- 상태마다 클래스를 만들어야 하므로 클래스 수가 늘어난다.
- 상태 전환 흐름을 전체적으로 파악하려면 여러 클래스를 확인해야 한다.
- 단순한 상태 처리에는 오히려 구조가 복잡해질 수 있다.


## 8. 전략 패턴과의 차이

상태 패턴은 전략 패턴과 구조가 비슷하다. 둘 다 객체에게 행동을 위임한다.

하지만 목적이 다르다.

| 구분 | 전략 패턴 | 상태 패턴 |
|---|---|---|
| 목적 | 행동 알고리즘을 바꿔 끼우기 | 상태에 따라 행동을 바꾸기 |
| 변경 이유 | 외부에서 전략을 선택함 | 객체 내부 상태가 변함 |
| 예시 | 오리의 나는 방식 | 껌볼 머신의 현재 상태 |
| 핵심 | 방법이 바뀐다 | 상황이 바뀐다 |

전략 패턴은 “어떤 방법으로 할 것인가”에 초점이 있고, 상태 패턴은 “현재 상태가 무엇인가”에 초점이 있다.

## 9. 핵심 정리

상태 패턴은 **객체의 현재 상태에 따라 같은 요청을 다르게 처리하도록 만드는 패턴**이다. 한마디로 정리하면 다음과 같다.

> 상태별 행동을 클래스로 분리하는 패턴