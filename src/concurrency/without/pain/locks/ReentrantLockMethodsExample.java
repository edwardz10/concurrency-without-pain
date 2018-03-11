package concurrency.without.pain.locks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReentrantLockMethodsExample {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        ReentrantLockCounterIncrement lockCounterIncrement = new ReentrantLockCounterIncrement();

        executorService.submit(() -> {
            System.out.println("IncrementCount (First Thread) : " +
                    lockCounterIncrement.incrementAndGet());
        });

        executorService.submit(() -> {
            System.out.println("IncrementCount (Second Thread) : " +
                    lockCounterIncrement.incrementAndGet());
        });

    }
}
