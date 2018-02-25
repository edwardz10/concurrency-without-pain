package concurrency.without.pain.executors;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {

    static Executor pool = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(9000);

        while(true) {
            final Socket s = socket.accept();

            pool.execute(() -> {
                doWork(s);
            });
        }
    }

    private static void doWork(Socket s) {
    }
}
