package code.ss.demo1.juc1;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class Mutex {
    static class Sync extends AbstractQueuedSynchronizer{
        @Override
        protected boolean tryAcquire(int arg) {
            return compareAndSetState(0, 1);
        }

        @Override
        protected boolean tryRelease(int arg) {
            return compareAndSetState(1, 0);
        }
    }

    Sync sync = new Sync();
    public void lock() {
        sync.acquire(1);
    }

    public void unLock() {
        sync.tryRelease(1);
    }
}
