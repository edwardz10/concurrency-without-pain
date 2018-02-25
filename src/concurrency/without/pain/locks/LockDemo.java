package concurrency.without.pain.locks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class LockDemo {

    private static class Worker implements Runnable {

        private String name;
        private ReentrantLock rl;

        public Worker(String name, ReentrantLock rl) {
            this.name = name;
            this.rl = rl;
        }

        @Override
        public void run() {
            rl.lock();
            try {
                if (rl.isHeldByCurrentThread()) {
                    System.out.printf("Thread %s has entered its critical section.%n",
                            name);
                }

                System.out.printf("Thread %s is performing work for 2 seconds.%n", name);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
                System.out.printf("Thread %s has finished working.%n", name);
            } finally {
                rl.unlock();
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        final ReentrantLock rl = new ReentrantLock();

        executor.execute(new Worker("A", rl));
        executor.execute(new Worker("B", rl));

        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        executor.shutdownNow();
    }
}
