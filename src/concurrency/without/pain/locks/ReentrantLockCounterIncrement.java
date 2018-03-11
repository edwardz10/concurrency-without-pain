package concurrency.without.pain.locks;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockCounterIncrement {

    private final ReentrantLock lock = new ReentrantLock();
    private int count = 0;

    public int incrementAndGet() {
        System.out.println("IsLocked: " + lock.isLocked());
        System.out.println("IsHeldByTheCurrentThread: " + lock.isHeldByCurrentThread());

        boolean isAcquired = lock.tryLock();
        System.out.println("Lock Acquired: " + isAcquired);

        if (isAcquired) {
            try {
                Thread.sleep(2000L);
                count++;
            } catch (InterruptedException ie) {
                throw new IllegalStateException(ie);
            } finally {
                lock.unlock();
            }
        }

        return count;
    }
}

