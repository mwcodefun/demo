package code.ss.demo1.mq1;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SyncQueue<T> {

    private final Condition notEmptyCondition;
    private ArrayList<T> queue;

    volatile int index = 0;

    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.ReadLock readLock;
    ReentrantReadWriteLock.WriteLock writeLock;

    public SyncQueue() {
        this.queue = new ArrayList<>();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
        notEmptyCondition = writeLock.newCondition();
    }

    public void enqueue(T obj) {
        try {
            writeLock.lock();
            queue.add(obj);
            notEmptyCondition.signal();
        } finally {
            writeLock.unlock();
        }
    }

    public T pull() throws InterruptedException {
        try {
            writeLock.lock();
            if (queue.size() <= 0) {
                notEmptyCondition.await();
            }
            return queue.remove(queue.size() - 1);
        } finally {
            writeLock.unlock();
        }
    }

    public int size() {
        try {
            readLock.lock();
            return queue.size();
        }finally {
            readLock.unlock();
        }
    }

    public boolean isEmpty() {
        readLock.lock();
        int i = index;
        readLock.unlock();
        return i == 0;
    }

    public static void main(String[] args) throws InterruptedException {
        SyncQueue<Integer> integerSyncQueue = new SyncQueue<>();
        new Thread(() -> {
            try {
                while (true) {
                    long beginTime = System.nanoTime();
                    Integer pull = integerSyncQueue.pull();
                    System.out.println("wating pull success:" + pull + ", waiting cost:" + (System.nanoTime() - beginTime));
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }).start();
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                integerSyncQueue.enqueue(i);
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 20; i < 40; i++) {
                integerSyncQueue.enqueue(i);
            }
        });
        thread.start();
        thread.join();
        thread2.start();
        thread2.join();
        System.out.println(integerSyncQueue.size());
        Integer pull = integerSyncQueue.pull();
        System.out.println(pull);
    }


}
