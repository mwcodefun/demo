package code.ss.demo1.juc1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class FobaConditionDemo {

    volatile   int count;
    ReentrantLock reentrantLock = new ReentrantLock();
    Condition bared = reentrantLock.newCondition();
    Condition fooed = reentrantLock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        FobaConditionDemo fobaConditionDemo = new FobaConditionDemo(10);
        new Thread(() -> {
            fobaConditionDemo.bar();
        }).start();
        new Thread(() -> {
            fobaConditionDemo.foo();
        }).start();
        while (fobaConditionDemo.count > 0);
    }

    public FobaConditionDemo(int count) {
        this.count = count;
    }

    public void bar() {
        while (count > 0) {
            try {
                reentrantLock.lock();
                fooed.await();
                if (count <= 0) {
                    reentrantLock.unlock();
                    return;
                }
                sayBar();
                bared.signal();
                count--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
//                reentrantLock.unlock();
            }
        }

    }
    public void foo() {
        while (count > 0) {
            try {
                reentrantLock.lock();
                if (count <= 0) {
                    reentrantLock.unlock();
                    return;
                }
                sayFoo();
                fooed.signal();
                bared.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
//                reentrantLock.unlock();
            }
        }

    }

    public void sayFoo() {
        System.out.println("foo" + count);
    }

    public void sayBar() {
        System.out.println("bar" + count);
    }

}
