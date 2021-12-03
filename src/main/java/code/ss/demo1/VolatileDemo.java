package code.ss.demo1;

public class VolatileDemo {
    private static volatile int i = 1;

    public void set() {
        this.i = 2;
    }
    public int get() {
        return i;
    }

    public static void main(String[] args) {
        VolatileDemo a = new VolatileDemo();
        for (int i = 0; i < 1000; i++) {
            a.set();
            a.get();
        }
    }

}
