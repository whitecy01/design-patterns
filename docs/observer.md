# Observer Pattern

## 1. Observer Pattern이란?

**Observer Pattern**
> 한 객체의 상태가 바뀌었을 때, 그 객체에 의존하는 다른 객체들에게 자동으로 알림이 가고 내용이 갱신되도록 만드는 디자인 패턴이다. 즉, 객체 사이에 **일대다 의존성**을 정의하는 패턴이다.

> 한 객체의 상태가 바뀌면, 그 객체를 구독하고 있는 여러 객체에게 자동으로 알림이 전달된다.

여기서 상태가 바뀌는 객체를 **Subject**라고 하고, 그 상태 변화를 전달받는 객체를 **Observer**라고 한다.


## 2. 문제 상황
기상 스테이션 프로그램을 만든다고 생각해보자. `WeatherData`는 온도, 습도, 기압 정보를 가지고 있다.
그리고 이 데이터를 화면에 보여주는 `CurrentConditionsDisplay`가 있다. 문제는 `WeatherData`의 값이 바뀔 때마다, 이 값을 사용하는 화면들도 자동으로 갱신되어야 한다는 점이다.

예를 들어 현재 상태 화면뿐만 아니라, 통계 화면, 예보 화면이 추가될 수도 있다. 이때 `WeatherData`가 각 화면 객체를 직접 알고 하나씩 호출하면 문제가 생긴다. 새로운 화면이 추가될 때마다 `WeatherData` 코드를 수정해야 하고, `WeatherData`가 화면 클래스들에 강하게 의존하게 된다.


## 3. 해결 방향

옵저버 패턴은 이 문제를 해결하기 위해 **상태가 바뀌는 객체와 그 상태를 사용하는 객체를 분리**한다.

상태가 바뀌는 객체를 `Subject`라고 하고, 상태 변화를 전달받는 객체를 `Observer`라고 한다.

이번 예제에서는 다음과 같이 역할을 나누었다.

| 역할 | 클래스/인터페이스 | 설명 |
|---|---|---|
| Subject | `Subject` | Observer를 등록, 제거, 알림하는 규칙을 정의한다 |
| ConcreteSubject | `WeatherData` | 실제 날씨 데이터를 가지고 있고, 값이 바뀌면 Observer들에게 알린다 |
| Observer | `Observer` | Subject로부터 변경 알림을 받는 규칙을 정의한다 |
| ConcreteObserver | `CurrentConditionsDisplay` | 날씨 데이터가 바뀌면 현재 상태를 화면에 출력한다 |
| Display 역할 | `DisplayElement` | 화면에 표시하는 기능을 정의한다 |

즉, `WeatherData`는 구체적인 화면 클래스를 직접 알지 않는다. 대신 `Observer` 인터페이스를 구현한 객체들을 목록으로 관리한다.

## 4. Push 방식과 Pull 방식
옵저버 패턴에서 Subject가 Observer에게 데이터를 전달하는 방식은 크게 두 가지가 있다.
- Push 방식
- Pull 방식

### Push 방식
Push 방식은 Subject가 Observer에게 변경된 데이터를 직접 전달하는 방식이다.  현재 예제는 Push 방식이다. WeatherData는 Observer들에게 온도, 습도, 기압 값을 직접 전달한다.

장점은 Observer가 따로 데이터를 조회하지 않아도 된다는 점이다. 하지만 단점도 있다.
- Observer가 필요하지 않은 데이터까지 받을 수 있다. 
- 예를 들어 CurrentConditionsDisplay는 온도와 습도만 사용한다.  하지만 update 메서드에서는 기압까지 함께 전달받는다. 즉, Subject가 데이터를 밀어 넣는 구조이기 때문에 Observer 입장에서는 불필요한 데이터도 받을 수 있다.

### Pull 방식
Pull 방식은 Subject가 Observer에게 “상태가 바뀌었다”는 사실만 알려주고, Observer가 필요한 데이터를 직접 가져가는 방식이다.
- WeatherData → Observer에게 변경 사실만 알림 
- Observer → WeatherData에서 필요한 데이터만 조회

이 방식에서는 Observer가 자신에게 필요한 값만 가져올 수 있다. 예를 들어 현재 상태 화면은 온도와 습도만 가져오고,  예보 화면은 기압만 가져올 수 있다.

- 장점은 Observer마다 필요한 데이터만 선택해서 사용할 수 있다는 점이다.
- 단점은 Observer가 Subject를 알고 있어야 한다는 점이다. 
- 즉, Observer가 WeatherData의 getter 메서드를 호출해야 하므로 Subject에 대한 의존성이 생긴다.


## 5. 최종 정리

옵저버 패턴은 한 객체의 상태가 바뀌었을 때, 그 객체에 의존하는 다른 객체들에게 자동으로 알림이 전달되고 갱신되도록 만드는 패턴이다.

이번 예제에서는 WeatherData가 Subject이고, CurrentConditionsDisplay가 Observer다.

WeatherData는 날씨 데이터가 바뀌면 자신에게 등록된 Observer들에게 알림을 보낸다.
CurrentConditionsDisplay는 그 알림을 받아 현재 온도와 습도를 화면에 출력한다.

핵심은 WeatherData가 구체적인 화면 클래스에 직접 의존하지 않는다는 점이다.

WeatherData는 오직 Observer 인터페이스에만 의존한다.
따라서 새로운 화면이 추가되어도 WeatherData를 크게 수정하지 않고 Observer를 추가할 수 있다.

옵저버 패턴은 Subject와 Observer를 느슨하게 연결해서, 상태 변화가 여러 객체에 자동으로 전달되도록 만드는 패턴이다.

또한, 추가적으로 JDK에 있는 자바빈과 스윙 라이브러리에서도 옵저버 패턴을 쓰고 있다.