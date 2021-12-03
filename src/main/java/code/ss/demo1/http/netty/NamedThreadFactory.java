package code.ss.demo1.http.netty;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {

    final String prefix;
    private AtomicInteger atomicInteger = new AtomicInteger();


    public NamedThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(prefix + "-" + atomicInteger.getAndIncrement());
        return thread;
    }
}
