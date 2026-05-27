package composite;

public class MenuTestDrive {

    public static void main(String[] args) {
        MenuComponent pancakeHouseMenu =
                new Menu("팬케이크 하우스 메뉴", "아침 메뉴");

        MenuComponent dinerMenu = new Menu("객체마을 식당 메뉴", "점심 메뉴");

        MenuComponent cafeMenu =
                new Menu("카페 메뉴", "저녁 메뉴");

        MenuComponent dessertMenu =
                new Menu("디저트 메뉴", "디저트를 즐겨보세요");

        MenuComponent allMenus =
                new Menu("전체 메뉴", "전체 메뉴 모음");

        allMenus.add(pancakeHouseMenu);
        allMenus.add(dinerMenu);
        allMenus.add(cafeMenu);

        pancakeHouseMenu.add(new MenuItem(
                "팬케이크 세트",
                "스크램블 에그와 토스트가 함께 나오는 팬케이크",
                true,
                2.99
        ));

        pancakeHouseMenu.add(new MenuItem(
                "블루베리 팬케이크",
                "신선한 블루베리와 시럽이 들어간 팬케이크",
                true,
                3.49
        ));

        dinerMenu.add(new MenuItem(
                "채식주의자용 BLT",
                "통밀빵에 양상추, 토마토, 베이컨 대신 콩고기",
                true,
                2.99
        ));

        dinerMenu.add(new MenuItem(
                "BLT",
                "통밀빵에 베이컨, 양상추, 토마토",
                false,
                2.99
        ));

        dinerMenu.add(new MenuItem(
                "오늘의 수프",
                "감자 샐러드를 곁들인 오늘의 수프",
                false,
                3.29
        ));

        dinerMenu.add(dessertMenu);

        dessertMenu.add(new MenuItem(
                "애플 파이",
                "바닐라 아이스크림이 올라간 애플 파이",
                true,
                1.59
        ));

        cafeMenu.add(new MenuItem(
                "베지 버거와 감자튀김",
                "통밀빵에 양상추와 토마토가 들어간 베지 버거",
                true,
                3.99
        ));

        Waitress waitress = new Waitress(allMenus);

        waitress.printMenu();
    }
}