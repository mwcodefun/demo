package code.ss.demo1.juc1.future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class CompletableFutureDemo {


    private static final ExecutorService executor = Executors.newFixedThreadPool(10, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }
    });

    public static <T> CompletableFuture<T> allOf(List<CompletableFuture<T>> futuresList) {
        CompletableFuture allFuturesResult =
                CompletableFuture.allOf(futuresList.toArray(new CompletableFuture[futuresList.size()]));

        return allFuturesResult;
    }

    public static void main(String[] args) {

        ArrayList<CompletableFuture<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int finalI = i;

            CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> {
                System.out.println("start work");
                if (finalI % 3 != 0) {
                    if (finalI % 2 == 0) {
                        System.out.println("throw Runtime Exception1");
                        throw new NullPointerException();

                    }
                    System.out.println("throw Runtime Exception");
                    throw new IllegalStateException();
                }
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("finish work");
                return finalI;
            }, executor);
            futures.add(integerCompletableFuture);
        }
        CompletableFuture<Integer> listCompletableFuture = allOf(futures);
        listCompletableFuture.whenComplete((l, t) -> {
            if (t != null) {
                System.out.println("check exception" + t.getMessage());
            }
            System.out.println("future complet");
            System.out.println(l);
            System.out.println(t);
        });
        listCompletableFuture.exceptionally((t) -> {
            System.out.println("exception ally stage :" + t);
            return null;
        });
        try {
            listCompletableFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("catch get exception");
            e.printStackTrace();
        }
    }


}
