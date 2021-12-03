package code.ss.demo1.jvm;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class BiasLock {

    public static final int SPIN_THRESHOLD_TIMES = 300;

    static long a = 0;

    static class LockThread extends Thread {
        AtomicReference<LockObject> lock = new AtomicReference<>();

        public LockThread(Runnable target) {
            super(target);
        }
    }

    public static void testMultiThread() throws InterruptedException {
        LockObject lockObject = new LockObject();
        ArrayList<String> strings = new ArrayList<>();

        int size = 30;
        Thread[] threads = new Thread[size];

        for (int i = 0; i < size; i++) {
            threads[i] =
                    new LockThread(() -> {
                        int b = 0;
                        while (b <= 10) {
                            lock(lockObject);
                            a++;
                            strings.add(String.valueOf(a));
                            unlock(lockObject);
                            b++;
                        }

                    });
        }
        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        assert a == (size * 10);

    }

    public static void main(String[] args) throws InterruptedException {
        LockObject lockObject = new LockObject();
        ArrayList<String> strings = new ArrayList<>();
        int size = 30;
        LockThread lock = new LockThread(() -> {
            lock(lockObject);
            System.out.println("lock");
            unlock(lockObject);
        });
        lock.start();
        lock.join();

        System.out.println("size:" + strings.size());
        System.out.println(a);
        testMultiThread();
    }


    static class LockObject {
        //0 is not biased,1 is biased
        AtomicInteger lockStatus = new AtomicInteger(0);
        AtomicInteger baisedThreadId = new AtomicInteger(-1);
        ReentrantLock reentrantLock = new ReentrantLock();
        AtomicReference<Thread> smallLockReference = new AtomicReference();
        AtomicInteger spainThreadCount = new AtomicInteger(0);
        AtomicInteger spinCount = new AtomicInteger(0);
    }

    public static final int BlOCK = 5;
    public static final int SPIN_LOCK = 2;
    public static final int NO_LOCK = 0;
    public static final int LOCK_BAISED = 1;

    static public void unlock(LockObject lock) {
        int status = lock.lockStatus.get();
        if (status == NO_LOCK) {
            return;
        }
        if (status == LOCK_BAISED) {
            //解除偏向
            int i = lock.baisedThreadId.get();
            if (lock.baisedThreadId.compareAndSet(i, -1)) {
                lock.lockStatus.compareAndSet(LOCK_BAISED, NO_LOCK);
            }
        }
        if (status == SPIN_LOCK) {
            Thread thread = Thread.currentThread();
            lock.smallLockReference.compareAndSet(thread, null);
        }
        if (status == BlOCK) {
            if (lock.smallLockReference.get() == Thread.currentThread()) {
                //spin lock
                lock.smallLockReference.set(null);
            } else {
                lock.reentrantLock.unlock();
            }
        }
    }

    static public void lock(LockObject lock) {
        int status = lock.lockStatus.get();
        System.out.println("status:" + status);
        if (status == BlOCK) {
            lock.reentrantLock.lock();
        } else if (status == SPIN_LOCK) {
            raiseSpinLock(lock);
        } else if (status == NO_LOCK) {
            if (lock.baisedThreadId.get() == -1) {
                boolean b = lock.baisedThreadId.compareAndSet(-1, Thread.currentThread().hashCode());
                if (b) {
                    if (lock.lockStatus.compareAndSet(NO_LOCK, LOCK_BAISED)) {
                        System.out.println("baised success");
                        return;
                    }
                }
            }
            System.out.println("baised fail");
            raiseSpinLock(lock);
        } else if (status == LOCK_BAISED) {
            //已偏向
            if (lock.baisedThreadId.get() == Thread.currentThread().hashCode()) {
                return;
            }
            lock.lockStatus.set(SPIN_LOCK);
            //升级到轻量级锁
            raiseSpinLock(lock);
        }

    }

    private static void notify_lock(LockObject lock) {
//        LockSupport.unpark();
    }

    private static void block_lock(LockObject lock) {
//        LockSupport.park();
    }

    private static void raiseSpinLock(LockObject lock) {
        while (true) {
            int status = lock.lockStatus.get();
            if (status == BlOCK) {
                lock.reentrantLock.lock();
                return;
            }

            if (status <= SPIN_LOCK) {
                if (lock.lockStatus.compareAndSet(status, SPIN_LOCK)) {
                    break;
                }
            }
        }


        LockThread c = (LockThread) Thread.currentThread();
        c.lock.compareAndSet(null, lock);
        int spinCount = 0;
        while (true) {
            if (spinCount >= SPIN_THRESHOLD_TIMES) {
                lock.lockStatus.set(BlOCK);
                lock.reentrantLock.lock();
                break;
            }
            int i = lock.spainThreadCount.incrementAndGet();
            if (i > 3) {
                System.out.println("// stop spin cause too many thread contend,go to block lock");
                lock.lockStatus.set(BlOCK);
                lock.reentrantLock.lock();
                break;
            }
            if (lock.smallLockReference.compareAndSet(null, c)) {
                //get lock success
                lock.spainThreadCount.decrementAndGet();
                System.out.println("//spin :" + spinCount);
                break;
            } else {
                Thread.yield();
                spinCount++;
            }
            lock.spinCount.incrementAndGet();
        }
    }


}
