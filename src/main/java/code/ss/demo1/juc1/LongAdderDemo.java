package code.ss.demo1.juc1;


public class LongAdderDemo {
    static long valueOffset;

    static {
        try {
            Class<?> ak = LongAdderDemo.class;
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    void cas(long cur,long newv) {
    }


    public static void main(String[] args) {
        LongAdderDemo longAdderDemo = new LongAdderDemo();
        longAdderDemo.cas(1,1);
    }
}
