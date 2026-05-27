# 컴포지트 패턴

## 1. 정의

컴포지트 패턴은 **객체들을 트리 구조로 구성해서, 개별 객체와 객체 그룹을 같은 방식으로 다룰 수 있게 해주는 패턴**이다.

즉, 하나의 객체와 여러 객체가 모인 그룹을 클라이언트가 구분하지 않고 사용할 수 있게 한다.

> 객체를 트리구조로 구성해서 부분-전체 계층구조를 구현합니다. 컴포지트 패턴을 사용하면 클라이언트에서 개별 객체와 복합 객체를 똑같은 방법으로 다룰 수 있습니다.



## 2. 언제 사용하는가?

객체가 계층 구조를 가질 때 사용한다.

예를 들어 메뉴판을 생각해보면 다음과 같은 구조가 가능하다.

```text
전체 메뉴
├── 팬케이크 하우스 메뉴
│   ├── 팬케이크 세트
│   └── 블루베리 팬케이크
├── 객체마을 식당 메뉴
│   ├── BLT
│   └── 디저트 메뉴
│       └── 애플 파이
└── 카페 메뉴
    └── 베지 버거
```

여기서 `메뉴`는 다른 메뉴나 메뉴 항목을 포함할 수 있고,  
`메뉴 항목`은 더 이상 자식을 가질 수 없는 개별 객체이다.

이때 클라이언트는 `Menu`인지 `MenuItem`인지 구분하지 않고 같은 방식으로 다루고 싶다.

## 3. 구조

| 역할 | 설명 | 예시 |
|---|---|---|
| Component | Leaf와 Composite의 공통 부모 | MenuComponent |
| Leaf | 자식을 가질 수 없는 개별 객체 | MenuItem |
| Composite | 자식을 가질 수 있는 복합 객체 | Menu |
| Client | Component를 사용하는 객체 | Waitress |


## 4. 예제 흐름

`MenuComponent`는 `Menu`와 `MenuItem`의 공통 부모이다.

```java
public abstract class MenuComponent {

    public void add(MenuComponent menuComponent) {
        throw new UnsupportedOperationException();
    }

    public void remove(MenuComponent menuComponent) {
        throw new UnsupportedOperationException();
    }

    public MenuComponent getChild(int i) {
        throw new UnsupportedOperationException();
    }

    public String getName() {
        throw new UnsupportedOperationException();
    }

    public void print() {
        throw new UnsupportedOperationException();
    }
}
```

`MenuItem`은 실제 메뉴 항목 하나를 의미한다.  
자식을 가질 수 없으므로 Leaf 역할이다.

```java
public class MenuItem extends MenuComponent {

    private String name;
    private String description;
    private boolean vegetarian;
    private double price;

    public MenuItem(String name, String description, boolean vegetarian, double price) {
        this.name = name;
        this.description = description;
        this.vegetarian = vegetarian;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void print() {
        System.out.println(getName() + ", " + price);
    }
}
```

`Menu`는 다른 `MenuComponent`들을 가질 수 있다.  
즉, `MenuItem`도 넣을 수 있고 다른 `Menu`도 넣을 수 있다.

```java
import java.util.ArrayList;
import java.util.List;

public class Menu extends MenuComponent {

    private List<MenuComponent> menuComponents = new ArrayList<>();
    private String name;

    public Menu(String name) {
        this.name = name;
    }

    @Override
    public void add(MenuComponent menuComponent) {
        menuComponents.add(menuComponent);
    }

    @Override
    public void remove(MenuComponent menuComponent) {
        menuComponents.remove(menuComponent);
    }

    @Override
    public MenuComponent getChild(int i) {
        return menuComponents.get(i);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void print() {
        System.out.println();
        System.out.println(getName());
        System.out.println("----------------");

        for (MenuComponent menuComponent : menuComponents) {
            menuComponent.print();
        }
    }
}
```

클라이언트는 `Menu`인지 `MenuItem`인지 구분하지 않는다.  
그냥 `MenuComponent`로 다룬다.

```java
public class Waitress {

    private MenuComponent allMenus;

    public Waitress(MenuComponent allMenus) {
        this.allMenus = allMenus;
    }

    public void printMenu() {
        allMenus.print();
    }
}
```

사용 예시는 다음과 같다.

```java
MenuComponent allMenus = new Menu("전체 메뉴");

MenuComponent pancakeMenu = new Menu("팬케이크 메뉴");
MenuComponent dinerMenu = new Menu("식당 메뉴");
MenuComponent dessertMenu = new Menu("디저트 메뉴");

allMenus.add(pancakeMenu);
allMenus.add(dinerMenu);

pancakeMenu.add(new MenuItem("팬케이크 세트", "팬케이크와 달걀", true, 2.99));

dinerMenu.add(new MenuItem("BLT", "베이컨, 양상추, 토마토", false, 2.99));
dinerMenu.add(dessertMenu);

dessertMenu.add(new MenuItem("애플 파이", "바닐라 아이스크림이 올라간 파이", true, 1.59));

Waitress waitress = new Waitress(allMenus);
waitress.printMenu();
```

## 5. 장점

- 개별 객체와 복합 객체를 같은 방식으로 다룰 수 있다.
- 트리 구조를 표현하기 좋다.
- 클라이언트 코드가 단순해진다.
- 새로운 `Menu`나 `MenuItem`을 추가해도 클라이언트 코드를 크게 바꾸지 않아도 된다.

## 6. 단점

- 모든 객체를 공통 타입으로 다루기 때문에, 객체별로 허용되지 않는 기능이 생길 수 있다.
- 예를 들어 `MenuItem`은 자식을 가질 수 없는데도 `add()` 메서드를 상속받는다.
- 구조가 너무 일반화되면 타입 안정성이 약해질 수 있다.

## 7. 반복자 패턴과의 관계

헤드퍼스트 예제에서는 반복자 패턴 다음에 컴포지트 패턴이 나온다.

반복자 패턴은 컬렉션 내부 구조를 숨기고 순회 방법을 통일한다.

컴포지트 패턴은 여기서 더 나아가,  
메뉴와 메뉴 항목을 트리 구조로 묶고 같은 타입으로 다룰 수 있게 한다.

```text
반복자 패턴 = 내부 저장 방식은 숨기고 순회 방법을 통일한다
컴포지트 패턴 = 개별 객체와 객체 그룹을 같은 방식으로 다룬다
```


## 8. 핵심 정리

컴포지트 패턴은 **개별 객체와 객체 그룹을 같은 타입으로 다룰 수 있게 해주는 패턴**이다.

한마디로 정리하면 다음과 같다.

> 부분과 전체를 같은 방식으로 다루는 패턴