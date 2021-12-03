package code.ss.demo1.collections;

import java.util.HashMap;

public class HashDemo {

    public void hashdemo() {
        Object o = new Object();
        int n = 2 ^ 3;
        System.out.println(n);
        System.out.println(o.hashCode() & (n - 1));

        double loadFactor = 0.75f;
        int capcity = 1 << 4;
        int threshold = (int) (capcity * loadFactor);
        System.out.println(capcity);
        System.out.println(capcity * loadFactor);
        System.out.println(capcity << 1);
        System.out.println(threshold << 1);
        //为什么要将高位与地位进行异或运算？ 因为table本身的长度比较小 计算&操作时只使用了table长度的这些位
        System.out.println(o.hashCode() & (capcity - 1));
        System.out.println(Integer.toBinaryString(capcity - 1));
        System.out.println(Integer.toBinaryString(o.hashCode()));
        System.out.println(Integer.toBinaryString(o.hashCode() >>> 16 ^ o.hashCode()));
        System.out.println(Integer.toBinaryString((o.hashCode() >>> 16 ^ o.hashCode()) & (capcity - 1)));

        new HashMap<>();
    }

    public static void main(String[] args) {
        HashDemo hashDemo = new HashDemo();
        hashDemo.hashdemo();
    }

}
