# 퍼사드 패턴

## 1. 정의

퍼사드 패턴은 **복잡한 서브시스템을 사용하기 쉽게 단순한 인터페이스로 제공하는 패턴**이다.

즉, 여러 객체를 직접 조작해야 하는 복잡한 과정을 하나의 클래스가 대신 처리해준다.

> 서브시스템에 있는 일련의 인터페이스를 통합 인터페이스로 묶어 줍니다. 또한 고수준 인터페이스도 정의하므로 서브 시스템을 더 편리하게 사용할 수 있습니다.

## 2. 언제 사용하는가?

여러 객체를 순서대로 호출해야 해서 클라이언트 코드가 복잡해질 때 사용한다.

예를 들어 홈시어터를 켜려면 원래는 조명, 스크린, 프로젝터, 앰프, DVD 플레이어 등을 각각 직접 조작해야 한다.

```java
lights.dim(10);
screen.down();
projector.on();
amp.on();
dvd.on();
dvd.play("인셉션");
```

이런 복잡한 과정을 `HomeTheaterFacade`가 감싸면 클라이언트는 간단하게 사용할 수 있다.

```java
homeTheater.watchMovie("인셉션");
```

## 3. 구조

| 역할 | 설명 | 예시 |
|---|---|---|
| Facade | 복잡한 기능을 단순한 메서드로 제공하는 클래스 | HomeTheaterFacade |
| Subsystem | 실제 기능을 수행하는 여러 클래스 | Amplifier, DvdPlayer, Projector |
| Client | Facade를 사용하는 쪽 | HomeTheaterTestDrive |

## 4. 예제 흐름

```java
HomeTheaterFacade homeTheater = new HomeTheaterFacade(
    amp,
    dvd,
    projector,
    lights,
    screen,
    popper
);

homeTheater.watchMovie("인셉션");
```

클라이언트는 `watchMovie()`만 호출하지만,  
내부에서는 여러 객체의 메서드가 순서대로 실행된다.

```java
public class HomeTheaterFacade {

    private Amplifier amp;
    private DvdPlayer dvd;
    private Projector projector;
    private TheaterLights lights;
    private Screen screen;
    private PopcornPopper popper;

    public HomeTheaterFacade(
            Amplifier amp,
            DvdPlayer dvd,
            Projector projector,
            TheaterLights lights,
            Screen screen,
            PopcornPopper popper
    ) {
        this.amp = amp;
        this.dvd = dvd;
        this.projector = projector;
        this.lights = lights;
        this.screen = screen;
        this.popper = popper;
    }

    public void watchMovie(String movie) {
        popper.on();
        popper.pop();

        lights.dim(10);
        screen.down();

        projector.on();
        projector.wideScreenMode();

        amp.on();
        amp.setDvd(dvd);
        amp.setVolume(5);

        dvd.on();
        dvd.play(movie);
    }
}
```


## 5. 장점

- 클라이언트 코드가 단순해진다.
- 복잡한 객체 사용 순서를 Facade 안에 숨길 수 있다.
- 클라이언트와 서브시스템 사이의 결합도를 낮출 수 있다.

## 6. 단점

- Facade 클래스가 많은 책임을 가지면 비대해질 수 있다.
- 모든 기능을 Facade에만 의존하면 서브시스템의 세부 기능을 활용하기 어려울 수 있다.


## 7. 핵심 정리

퍼사드 패턴은 **복잡한 여러 객체의 사용 과정을 하나의 단순한 인터페이스로 제공하는 패턴**이다.

한마디로 정리하면 다음과 같다.

> 복잡한 사용 과정을 감싸서 쉽게 사용할 수 있게 만드는 패턴