import composite.MenuItem;
import java.util.Iterator;

public class DinerMenu implements Menu {

    static final int MAX_ITEMS = 6;

    private int numberOfItems = 0;
    private MenuItem[] menuItems;

    public DinerMenu() {
        menuItems = new MenuItem[MAX_ITEMS];

        addItem(
                "채식주의자용 BLT",
                "통밀빵에 양상추, 토마토, 베이컨 대신 콩고기",
                true,
                2.99
        );

        addItem(
                "BLT",
                "통밀빵에 베이컨, 양상추, 토마토",
                false,
                2.99
        );

        addItem(
                "오늘의 수프",
                "감자 샐러드를 곁들인 오늘의 수프",
                false,
                3.29
        );

        addItem(
                "핫도그",
                "사워크라우트, 양파, 치즈가 들어간 핫도그",
                false,
                3.05
        );
    }

    public void addItem(String name, String description, boolean vegetarian, double price) {
        if (numberOfItems >= MAX_ITEMS) {
            System.out.println("죄송합니다. 메뉴가 꽉 찼습니다.");
        } else {
            MenuItem menuItem = new MenuItem(name, description, vegetarian, price);
            menuItems[numberOfItems] = menuItem;
            numberOfItems++;
        }
    }

    @Override
    public Iterator<MenuItem> createIterator() {
        return new DinerMenuIterator(menuItems);
    }
}