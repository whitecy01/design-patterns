# 반복자 패턴

## 1. 정의

반복자 패턴은 **컬렉션의 내부 구조를 노출하지 않고, 원소에 순차적으로 접근할 수 있게 해주는 패턴**이다.

즉, 데이터가 배열에 저장되어 있는지, `ArrayList`에 저장되어 있는지 클라이언트가 몰라도 된다.

클라이언트는 반복자만 사용해서 데이터를 하나씩 꺼내면 된다.

> 컬렉션 구현 방법을 노출하지 않으면서 집합체 안에 들어있는 모든 항목에 접근할 수 있게 해주는 방법을 제공합니다.


## 2. 언제 사용하는가?

여러 객체를 순서대로 접근해야 하는데, 각 컬렉션의 저장 방식이 다를 때 사용한다. 예를 들어 한 메뉴는 `ArrayList`로 저장하고, 다른 메뉴는 배열로 저장한다고 하자.

```java
import composite.MenuItem;

ArrayList<MenuItem> menuItems;
MenuItem[] menuItems;
```

저장 방식은 다르지만, 클라이언트는 둘 다 같은 방식으로 순회하고 싶다.

```java
import composite.MenuItem;

Iterator<MenuItem> iterator = menu.createIterator();

while(iterator.

hasNext()){
MenuItem menuItem = iterator.next();
}
```

이럴 때 반복자 패턴을 사용한다.

## 3. 구조

| 역할 | 설명 | 예시 |
|---|---|---|
| Iterator | 원소에 순차적으로 접근하는 인터페이스 | Iterator |
| ConcreteIterator | 실제 순회 방법을 구현한 클래스 | DinerMenuIterator |
| Aggregate | 반복자를 생성하는 인터페이스 | Menu |
| ConcreteAggregate | 실제 컬렉션을 가지고 있는 클래스 | PancakeHouseMenu, DinerMenu |
| Client | 반복자를 사용해서 원소에 접근하는 클래스 | Waitress |

## 4. 예제 흐름

`Menu` 인터페이스는 반복자를 생성하는 메서드를 가진다.

```java
import composite.MenuItem;
import java.util.Iterator;

public interface Menu {
    Iterator<MenuItem> createIterator();
}
```

`PancakeHouseMenu`는 내부적으로 `ArrayList`를 사용한다.

```java
import composite.MenuItem;

public class PancakeHouseMenu implements Menu {

    private List<MenuItem> menuItems;

    @Override
    public Iterator<MenuItem> createIterator() {
        return menuItems.iterator();
    }
}
```

`DinerMenu`는 내부적으로 배열을 사용한다.

```java
import composite.MenuItem;

public class DinerMenu implements Menu {

    private MenuItem[] menuItems;

    @Override
    public Iterator<MenuItem> createIterator() {
        return new DinerMenuIterator(menuItems);
    }
}
```

배열은 직접 반복자를 만들어서 순회할 수 있게 한다.

```java
import composite.MenuItem;
import java.util.Iterator;

public class DinerMenuIterator implements Iterator<MenuItem> {

    private MenuItem[] items;
    private int position = 0;

    public DinerMenuIterator(MenuItem[] items) {
        this.items = items;
    }

    @Override
    public boolean hasNext() {
        if (position >= items.length || items[position] == null) {
            return false;
        }

        return true;
    }

    @Override
    public MenuItem next() {
        MenuItem menuItem = items[position];
        position++;
        return menuItem;
    }
}
```

클라이언트는 메뉴가 배열인지 `ArrayList`인지 알 필요가 없다.

```java
import composite.MenuItem;

public class Waitress {

    private Menu pancakeHouseMenu;
    private Menu dinerMenu;

    public Waitress(Menu pancakeHouseMenu, Menu dinerMenu) {
        this.pancakeHouseMenu = pancakeHouseMenu;
        this.dinerMenu = dinerMenu;
    }

    public void printMenu() {
        Iterator<MenuItem> pancakeIterator = pancakeHouseMenu.createIterator();
        Iterator<MenuItem> dinerIterator = dinerMenu.createIterator();

        printMenu(pancakeIterator);
        printMenu(dinerIterator);
    }

    private void printMenu(Iterator<MenuItem> iterator) {
        while (iterator.hasNext()) {
            MenuItem menuItem = iterator.next();
            System.out.println(menuItem.getName());
        }
    }
}
```


## 5. 장점

- 컬렉션의 내부 구조를 숨길 수 있다.
- 배열, 리스트 등 서로 다른 저장 방식을 같은 방법으로 순회할 수 있다.
- 클라이언트 코드가 단순해진다.
- 컬렉션을 순회하는 책임을 반복자 객체에게 분리할 수 있다.


## 6. 단점

- 단순한 컬렉션에는 구조가 조금 복잡해질 수 있다.
- 반복자 클래스를 따로 만들어야 하는 경우 코드가 늘어날 수 있다.

## 7. 핵심 정리

반복자 패턴은 **컬렉션의 저장 방식은 숨기고, 원소에 접근하는 방법을 통일하는 패턴**이다.

한마디로 정리하면 다음과 같다.

> 내부 구조는 숨기고, 순회 방법만 제공하는 패턴