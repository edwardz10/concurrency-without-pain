package concurrency.without.pain.executors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ReadWebPage {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("usage: java ReadWebPage url");
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<List<String>> callable = new Callable<List<String>>() {
            @Override
            public List<String> call() throws IOException, MalformedURLException {
                List<String> lines = new ArrayList<>();
                URL url = new URL(args[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                InputStreamReader isr = new InputStreamReader(con.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                String line;

                while ((line = br.readLine()) != null)
                    lines.add(line);
                return lines;
            }
        };

        Future<List<String>> future = executor.submit(callable);

        try {
            List<String> lines = future.get(5, TimeUnit.SECONDS);

            for (String line : lines) {
                System.out.println(line);
            }
        } catch (ExecutionException ee) {
            System.err.println("Callable through exception: "+ee.getMessage());
        } catch (InterruptedException | TimeoutException eite) {
            System.err.println("URL not responding");
        }

        executor.shutdown();
    }
}
