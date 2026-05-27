public class Amplifier {

    public void on() {
        System.out.println("앰프를 켭니다");
    }

    public void off() {
        System.out.println("앰프를 끕니다");
    }

    public void setVolume(int level) {
        System.out.println("볼륨을 " + level + "로 설정합니다");
    }

    public void setDvd(DvdPlayer dvd) {
        System.out.println("앰프 입력을 DVD로 설정합니다");
    }
}