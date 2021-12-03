package code.ss.demo1.juc1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class AqsDemo extends AbstractQueuedSynchronizer {

    public void lock() {
        acquire(1);
    }

    public void unlock() {
        tryRelease(1);
    }

    @Override
    protected boolean isHeldExclusively() {
        return getExclusiveOwnerThread() == Thread.currentThread();
    }

    @Override
    protected boolean tryAcquire(int arg) {
        Thread thread = Thread.currentThread();
        int state = getState();
        if (state == 0) {
            boolean b = compareAndSetState(0, arg);
            if (b) {
                setExclusiveOwnerThread(thread);
                return b;
            }
        } else {
            Thread exclusiveOwnerThread = getExclusiveOwnerThread();
            if (exclusiveOwnerThread == thread) {
                boolean b = compareAndSetState(state, state + arg);
                return b;
            }
        }
        return false;
    }

    @Override
    protected boolean tryRelease(int arg) {
        if (getExclusiveOwnerThread() != Thread.currentThread()) {
            throw new IllegalMonitorStateException();
        }
        int state = getState() - arg;
        boolean free = false;
        if (state == 0) {
            setExclusiveOwnerThread(null);
            free = true;
        }
        setState(arg);
        return free;
    }

    static ExecutorService executor = Executors.newFixedThreadPool(3);


    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                reentrantLock.lock();
            }
        });
        Condition condition = reentrantLock.newCondition();

        reentrantLock.lock();
        try {
            reentrantLock.lockInterruptibly();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
