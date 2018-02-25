package concurrency.without.pain.locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionDemo {

    private static class Shared {
        private volatile char c;
        private volatile boolean available;
        private final Lock lock;
        private final Condition condition;

        public Shared() {
            c = '\u0000';
            available = false;
            lock = new ReentrantLock();
            condition = lock.newCondition();
        }

        public Lock getLock() {
            return lock;
        }

        public char getSharedChar() {
            lock.lock();
            try {
                while (!available) {
                    try {
                        condition.await();
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
                available = false;
                condition.signal();
            } finally {
                lock.unlock();
                return c;
            }
        }

        public void setSharedChar(char c) {
            lock.lock();
            try {
                while (available) {
                    try {
                        condition.await();
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
                this.c = c;
                available = true;
                condition.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    private static class Producer extends Thread {
        private final Lock l;

        // s is final because it's initialized on the main thread and accessed on the
        // producer thread.
        private final Shared s;

        Producer(Shared s) {
            this.s = s;
            l = s.getLock();
        }

        @Override
        public void run() {
            for (char ch = 'A'; ch <= 'Z'; ch++) {
                l.lock();
                s.setSharedChar(ch);
                System.out.println(ch + " produced by producer.");
                l.unlock();
            }
        }
    }

    private static class Consumer extends Thread {
        private final Lock l;

        // s is final because it's initialized on the main thread and accessed on the
        // producer thread.
        private final Shared s;

        Consumer(Shared s) {
            this.s = s;
            l = s.getLock();
        }

        @Override
        public void run() {
            char ch;
            do {
                l.lock();
                ch = s.getSharedChar();
                System.out.println(ch + " consumed by consumer.");
                l.unlock();
            } while (ch != 'Z');
        }
    }

    public static void main(String[] args) {
        Shared shared = new Shared();
        new Producer(shared).start();
        new Consumer(shared).start();
    }
}
