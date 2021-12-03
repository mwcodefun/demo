package code.ss.demo1.collections;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayQueue1 {
    static class DelayElement implements Delayed{

        private long delaySeconds;
        long value;
        public DelayElement(Long seconds) {
            this.delaySeconds = seconds;
            this.value = seconds;
        }


        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(delaySeconds, TimeUnit.SECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            if(this.getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS))
                return -1;
            else if(this.getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS))
                return 1;
            else
                return 0;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        DelayQueue<DelayElement> delayElements = new DelayQueue<>();
        delayElements.add(new DelayElement(10L));
        delayElements.add(new DelayElement(11L));
        delayElements.add(new DelayElement(12L));
        delayElements.add(new DelayElement(3L));
        delayElements.add(new DelayElement(4L));

        Thread t1 = new Thread(() -> {
            try {
                DelayElement take = delayElements.take();
                System.out.println(take.value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        Thread t2 = new Thread(() -> {
                delayElements.add(new DelayElement(1L));
        });
        t2.start();
        t1.join();




    }
}
