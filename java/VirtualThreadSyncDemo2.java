import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Service;

import java.util.Collections;

@SpringBootApplication
@RestController
public class VirtualThreadSyncDemo {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(VirtualThreadSyncDemo.class);
        app.setDefaultProperties(Collections.singletonMap("spring.threads.virtual.enabled", "true"));
        app.run(args);
    }

    private final SharedInventory inventory;

    public VirtualThreadSyncDemo(SharedInventory inventory) {
        this.inventory = inventory;
    }

    @GetMapping("/add")
    public String handle() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        int newCount = inventory.processLayer(10);

        return String.format("Item added. Total: %d\n", newCount);
    }

    @Service
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
}
