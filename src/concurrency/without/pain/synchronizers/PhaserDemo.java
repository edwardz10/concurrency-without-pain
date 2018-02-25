package concurrency.without.pain.synchronizers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

public class PhaserDemo {

    private static void runTasks(List<Runnable> tasks) {
        final Phaser phaser = new Phaser(1);

        for (final Runnable task : tasks) {
            phaser.register();

            new Thread(() -> {
                try {
                    Thread.sleep(50+(int)(Math.random()*300));
                } catch (InterruptedException ie) {
                    System.out.println("interrupted thread");
                }
                phaser.arriveAndAwaitAdvance(); // await all creation
                task.run();
            }).start();
        }

        phaser.arriveAndDeregister();
    }

    public static void main(String[] args) {
        List<Runnable> tasks = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            tasks.add(() -> {
                System.out.printf("%s is running at %d\n", Thread.currentThread().getName(), System.currentTimeMillis());
            });
        }

        runTasks(tasks);
    }
}
