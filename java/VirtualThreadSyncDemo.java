import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

public class VirtualThreadSyncDemo {

    static class SharedInventory {
        private int items = 0;

        public synchronized int addItem() {
            int currentCount = items;
            
            try {
                Thread.sleep(10); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            items = currentCount + 1;
            return items;
        }

        public int processLayer(int depth) {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (depth <= 0) {
                return addItem();
            }

            return processLayer(depth - 1);
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 8080;
        SharedInventory inventory = new SharedInventory();

        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

        server.createContext("/add", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int newCount = inventory.processLayer(10);

                String response = String.format("Item added. Total: %d\n", newCount);
                exchange.sendResponseHeaders(200, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                }
            }
        });

        System.out.println("Server started on http://0.0.0.0:" + port);
        server.start();
    }
}
