package code.ss.demo1.juc1;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class CountDownDemo {

    Sync sync = new Sync(5);

    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    public void countDown(int value) {
        sync.releaseShared(value);
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownDemo cLock = new CountDownDemo();
        cLock.countDown(1);
        Thread thread = new Thread(() -> {
            System.out.println("start clock");
            try {
                cLock.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("lock released!");
        });
        new Thread(() -> {cLock.countDown(4);}).start();
        thread.setName("lock thread");
        thread.setDaemon(true);
        thread.start();
        TimeUnit.SECONDS.sleep(2L);
//        cLock.countDown(4);
        System.out.println("countdown finish");
        thread.join();
    }

    static class Sync extends AbstractQueuedSynchronizer{

        public Sync(int value) {
            setState(value);
        }

        @Override
        protected int tryAcquireShared(int arg) {
            return getState() == 0 ? 1: -1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            for (; ; ) {
                int state = getState();
                if (state == 0) {
                    return false;
                }
                int next = state - arg;
                if (compareAndSetState(state, next)) {
                    System.out.println("c+" + getState());
                    return next == 0;
                }
            }

        }
    }

}
