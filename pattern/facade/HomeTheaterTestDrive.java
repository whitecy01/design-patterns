public class HomeTheaterTestDrive {

    public static void main(String[] args) {
        Amplifier amp = new Amplifier();
        DvdPlayer dvd = new DvdPlayer();
        Projector projector = new Projector();
        TheaterLights lights = new TheaterLights();
        Screen screen = new Screen();
        PopcornPopper popper = new PopcornPopper();

        HomeTheaterFacade homeTheater = new HomeTheaterFacade(
                amp,
                dvd,
                projector,
                lights,
                screen,
                popper
        );

        homeTheater.watchMovie("인셉션");

        System.out.println();

        homeTheater.endMovie();
    }
}