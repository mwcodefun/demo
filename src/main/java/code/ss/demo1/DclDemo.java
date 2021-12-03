package code.ss.demo1;

import java.util.HashMap;

public class DclDemo {
    private   static DclDemo INSTANCE;

    private DclDemo()   {
    }

    public int a;


    public static DclDemo getInstance() {
        if (INSTANCE == null) {
            synchronized (DclDemo.class) {
                if (INSTANCE == null) {
                        //对d的store操作
                        //alloc memo
                        //init method
                        //store to d
                        //aload_1
                        //putstatic wirteLoad
                        INSTANCE = new DclDemo();
                }
            }
        }
        return INSTANCE;
    }

    public static void main(String[] args) {
        String a = "a";
        Integer integer = new Integer(1);
        integer.hashCode();
        for (; ;integer++ ) {
            if (integer.hashCode() == a.hashCode()) {
                break;
            }

        }
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put(new Object(), new Object());
        objectObjectHashMap.put("1", "2");
        objectObjectHashMap.put("1", "3");
        objectObjectHashMap.put(a, "1");
        objectObjectHashMap.put(integer, "2");
//        objectObjectHashMap.put(new Object(), new Object());
    }

    static class R implements Runnable{
        @Override
        public void run() {
            System.out.println(DclDemo.getInstance().a);
        }
    }
}
