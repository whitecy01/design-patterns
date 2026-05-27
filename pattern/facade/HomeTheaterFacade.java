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
        System.out.println("영화 볼 준비를 시작합니다");

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

    public void endMovie() {
        System.out.println("홈시어터를 종료합니다");

        popper.off();

        lights.on();

        screen.up();

        projector.off();

        amp.off();

        dvd.stop();
        dvd.eject();
        dvd.off();
    }
}