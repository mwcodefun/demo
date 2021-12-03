public class Demo {
    private static Object lock = new Object();

    public static void main(String[] args) {
        synchronized (lock) {
            Object o = new Object();
            System.out.println(o);
        }
        System.out.println(123);
    }
}
