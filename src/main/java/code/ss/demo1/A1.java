package code.ss.demo1;

public class A1 {

    private static ThreadLocal<Integer> currentMachine = new ThreadLocal<>();

    public Integer v = 0;
    static final int MAX_COUNT = 1000 * 10000;
    static final String[] arr = new String[MAX_COUNT];

    public void a() {
        currentMachine = new ThreadLocal<>();
        currentMachine.set(1);
        currentMachine.get();
    }
    public static void main(String[] args) {
        Integer[] data = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        long start = System.currentTimeMillis();
        for (int i = 0; i < MAX_COUNT; i++) {
//                        arr[i] = new String(String.valueOf(data[i % data.length]));
            arr[i] = new String(String.valueOf(data[i % data.length])).intern();
        }
        long end = System.currentTimeMillis();
        Object o = new Object();
        System.out.println("The time spent is:" + (end - start));
        try {
            Thread.sleep(1000000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.gc();
    }
}
