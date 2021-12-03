package code.ss.demo1;

import java.util.concurrent.ConcurrentHashMap;

public class JucDemo {

    public static void main(String[] args) {
        ConcurrentHashMap<Object, Object> cmap = new ConcurrentHashMap<>();
        String a = "1";
        int i = 0;
        for (; i < Integer.MAX_VALUE; i++) {
            Integer integer = new Integer(i);
            if (a.hashCode() == integer.hashCode()) {
                break;
            }
        }


        cmap.put(a, "1");
        cmap.put(i, "2");
    }

}
