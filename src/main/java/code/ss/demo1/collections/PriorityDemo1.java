package code.ss.demo1.collections;

import java.util.PriorityQueue;

public class PriorityDemo1 {

    public static void main(String[] args) {
        PriorityQueue<Integer> integers = new PriorityQueue<>((a,b) ->{
            return a - b;
        }) ;

        integers.poll();
    }

}
