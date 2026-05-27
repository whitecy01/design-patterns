import composite.MenuItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PancakeHouseMenu implements Menu {

    private List<MenuItem> menuItems;

    public PancakeHouseMenu() {
        menuItems = new ArrayList<>();

        addItem(
                "팬케이크 세트",
                "달걀과 토스트가 함께 나오는 팬케이크",
                true,
                2.99
        );

        addItem(
                "블루베리 팬케이크",
                "신선한 블루베리와 시럽이 들어간 팬케이크",
                true,
                3.49
        );

        addItem(
                "와플",
                "블루베리 또는 딸기를 얹을 수 있는 와플",
                true,
                3.59
        );
    }

    public void addItem(String name, String description, boolean vegetarian, double price) {
        MenuItem menuItem = new MenuItem(name, description, vegetarian, price);
        menuItems.add(menuItem);
    }

    @Override
    public Iterator<MenuItem> createIterator() {
        return menuItems.iterator();
    }
}