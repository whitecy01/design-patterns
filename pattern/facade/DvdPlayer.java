public class DvdPlayer {

    public void on() {
        System.out.println("DVD 플레이어를 켭니다");
    }

    public void off() {
        System.out.println("DVD 플레이어를 끕니다");
    }

    public void play(String movie) {
        System.out.println(movie + " 영화를 재생합니다");
    }

    public void stop() {
        System.out.println("DVD 재생을 멈춥니다");
    }

    public void eject() {
        System.out.println("DVD를 꺼냅니다");
    }
}