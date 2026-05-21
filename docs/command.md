# 커맨드 패턴

## 1. 정의
- 커맨드 패턴은 요청을 객체로 캡슐화하는 패턴이다.

> 이 패턴을 사용하면 요청 내역을 객체로 캡슐화해서 객체를 서로 다른 요청 내역에 따라 매개변수화할 수 있습니다. 이러면 요청을 큐에 저장하거나 로그로 기록하거나 작업 취소 기능을 사용할 수 있습니다.

즉, 어떤 기능을 바로 실행하지 않고, 그 기능을 하나의 명령 객체로 만들어서 젖아하거나 전달하거나 실행할 수 있게 만든다.
- 요청 내역을 객체로 캡슐화해서 객체를 서로 다른 요청, 큐, 로그 요청 등으로 매개변수화할 수 있다. 또한 실행 취소 기능도 지원 가능

## 2. 어떤 상황일 때 쓰는가
커맨드 패턴은 요청하는 객체와 실제로 일하는 객체를 분리하고 싶을 때 사용한다.

예를 들어 다음과 같은 리모컨이 있다고 하자. 리모컨이 직접 다음과 같아 일고 있으면 문제가 생긴다.
```java
light.on();
garageDoor.up();
tv.on();
```
이렇게 되면 리모커은 Light, GarageDoor, TV 같은 구체적인 객체들을 전부 알아야한다. 그러면 새로운 기능이 추가될 때마다 리모컨 코드가 계속 바뀐다.

새로운 기기 추가 -> 리모컨 코드 수정 -> 기능 많아질수록 리모컨이 복잡해짐

커팬드 패턴은 이 문제를 해결한다 리모컨은 구체적인 기기를 모르고 오직 Command만 알면된다. 

```java
slot.execute();
```
즉 리모컨은 다음과 같이 생각한다.
- 나는 누가 불을 켜는지 모른다.
- 나는 누가 차고 문을 여는지도 모른다.
- 그냥 execute()만 호출한다.

## 3. 커맨드 패턴 구조

| 역할 | 설명 | 예제 코드 |
|---|---|---|
| Command | 모든 명령 객체가 따라야 하는 인터페이스이다. 실행 메서드인 `execute()`를 정의한다. | `Command` |
| ConcreteCommand | 실제 명령 객체이다. Receiver를 가지고 있다가 `execute()`가 호출되면 Receiver의 메서드를 실행한다. | `LightOnCommand`, `LightOffCommand`, `GarageDoorOpenCommand` |
| Receiver | 실제 일을 수행하는 객체이다. 불을 켜거나 끄고, 차고 문을 여는 실제 기능을 가지고 있다. | `Light`, `GarageDoor` |
| Invoker | 명령 객체를 저장하고 있다가 필요할 때 실행하는 객체이다. Receiver를 직접 알지 않고 `Command`의 `execute()`만 호출한다. | `SimpleRemoteControl` |
| Client | Receiver와 Command 객체를 생성하고, Invoker에 어떤 Command를 사용할지 연결해주는 객체이다. | `RemoteControlTest` |


## 4. 코드 구조로 이해해보기

### 4.1 Command 인터페이스

```java
public interface Command {
    public void execute();
}
```
모든 명령 객체는 execute()메서드를 가져야한다.
즉, 리모컨은 구체적인 명령이 무엇인지 몰라도 된다.
```java
LightOnCommand인지
GarageDoorOpenCommand인지
TVOnCommand인지
```

알 필요 없이 그냥 다음과 같이 호출 
```java
command.execute();
```

### 4.2 Receiver
- 실제로 일을 하는 객체이다
```java
public class Light {
    public void on() {
        System.out.println("Light is on");
    }
    
    public void off() {
        System.out.println("Light is off");
    }
}
```
여기서 Light가 Receiver다. 왜냐하면 실제 기능을 가지고 있다.
- Light -> 불 켜기, 끄기

### 4.3 ConcreteCommand
- Receiver를 가지고 있다가 execute()가 호출되면 Receiver의 메서드를 실행한다.
```java
public class LightOnCommand implements Command {
    Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        light.on();
    }
}
```
이 코드의 의미는 다음과 같다.
- LightOnCommand는 Light 객체를 가지고 있다. 
- execute()가 호출되면 light.on()을 실행한다.

즉, 명령 객체가 실제 일을 직접 하는 것은 아니다. 실제 일은 Light가 한다. LightOnCommand는 그 일을 호출하는 명령 객체다
- LightOnCommand.execute() -> light.on()

## 4.4 Invoker
- Invoker는 명령을 가지고 있다가 실행하는 객체다.
```java
public class SimpleRemoteControl {
    Command slot;

    public void setCommand(Command command) {
        slot = command;
    }

    public void buttonWasPressed() {
        slot.execute();
    }
}
```
여기서 SimpleRemoteControl이 Invoker이다. 리모컨은 Light를 모른다.

오직 Command만 한다. 그리고 버튼이 눌리면 다음을 실행한다.
```java
slot.execute();
```

### 4.5 Client
- Client는 객체들을 생성하고 서로 연결해주는 역할이다.
```java
public class RemoteControlTest {
public static void main(String[] args) {
SimpleRemoteControl remote = new SimpleRemoteControl();

        Light light = new Light();
        GarageDoor garageDoor = new GarageDoor();

        LightOnCommand lightOn = new LightOnCommand(light);
        GarageDoorOpenCommand garageOpen = new GarageDoorOpenCommand(garageDoor);

        remote.setCommand(lightOn);
        remote.buttonWasPressed();

        remote.setCommand(garageOpen);
        remote.buttonWasPressed();
    }
}
```

여기서 RemoteControlTest는 다음 일을 한다.
1. Receiver 생성 - Light
2. Command 생성 - LightOnCommand
3. Invoker에 Command 등록 - remote.setCommand(lightOn)
4. Invoker 실행 - remote.buttonWasPressed()

## 5. 실행 흐름
### 5.1 불켜기 명령
```java
remote.setCommand(lightOn);
remote.buttonWasPressed();
```

실행 흐름은 다음과 같다.
1. remote.buttonWasPressed()
2. slot.execute()
3. LightOnCommand.execute()
4. light.on()

역할로 보면 
- Invoker → Command → Receiver
- 구체적으로 SimpleRemoteControl → LightOnCommand → Light

## 6. 커맨드 패턴의 핵심
- 커맨드 패턴의 핵심은 다음과 같다.

> 요청하는 객체와 실제 일을 하는 객체를 분리한다.

- 리모컨이 직접 불을 켜는 방식은 다음과 같다.

```java
light.on();
```

이 경우 리모컨은 Light를 직접 알아야한다. 하지만 커맨드 패턴에서는 다음과 같이 한다.

```java
command.execute();
```

리모컨은 Light를 모른다. 대신 LightOnCommand가 Light를 알고 있다.
- SimpleRemoteControl은 Light를 모른다. 
- SimpleRemoteControl은 Command만 안다.

이렇게 하면 리모컨은 더 유연해진다.

## 7. 장점
### 7.1 요청하는 객체와 실행하는 객체를 분리할 수 있다.
- 리모컨은 실제 기기를 몰라도 된다. 
  - 리모컨 → Command만 알면 됨 
  - Command → 실제 Receiver를 알고 있음 
  - Receiver → 실제 동작 수행

그래서 리모컨 코드가 단순해진다.

### 7.2 새로운 명령을 쉽게 추가할 수 있다
- 예를 들어 TV를 켜는 기능을 추가하고 싶다면 리모컨 코드를 바꾸지 않아도 된다.

```java
public class TVOnCommand implements Command {
    TV tv;

    public TVOnCommand(TV tv) {
        this.tv = tv;
    }

    public void execute() {
        tv.on();
    }
}
```
새로운 Command 클래스만 추가하면 된다.
- 기존 SimpleRemoteControl 수정 X 
- 새로운 Command 클래스 추가 O

이것은 OCP와 관련있다 -> 확장에는 열려있고 변경에는 닫혀있다

### 7.3 요청을 큐나 로그로 저장할 수 있다.
- 명령이 객체이기 때문에 저장할 수 있다.
```java
List<Command> commands = new ArrayList<>();
```

그러면 나중에 순서대로 실행할 수도 있다. 이런식으로 큐, 예약실행, 로그 기록, 매크로 명령 등에 활용할 수 있다.


## 8. 단점
### 8.1 클래스가 많아진다.
기능 하나마다 Command 클래스가 생길 수 있다.
- LightOnCommand 
- LightOffCommand 
- GarageDoorOpenCommand 
- GarageDoorCloseCommand 
- TVOnCommand 
- TVOffCommand

기능이 많아질수록 클래스 수도 많아진다

### 8.2 구조가 처음에는 복잡해보인다.

## 9. 언제쓰면 좋은가
커맨드 패턴은 다음과 같은 상황에 좋다.
1. 요청하는 객체와 실행하는 객체를 분리하고 싶을 때
2. 버튼, 메뉴, 단축키처럼 요청을 나중에 연결하고 싶을 때
3. 실행 취소 기능이 필요할 때
4. 요청을 큐에 저장하거나 로그로 남기고 싶을 때
5. 여러 명령을 묶어서 한 번에 실행하고 싶을 때

예를 들면 
1. 리모컨 버튼 
2. GUI 버튼 
3. 메뉴 클릭 
4. 단축키 
5. 작업 큐 
6. 예약 작업 
7. 트랜잭션 로그 
8. 실행 취소 기능

이런 곳에서 사용할 수 있다.

## 10. 한 줄 요약
- 커맨드 패턴은 요청을 객체로 만들어서, 요청하는 객체와 실제 실행하는 객체를 분리하는 패턴이다.

