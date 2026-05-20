import com.cache.core.Cache;

public class Main {

    public static void main(String[] args) {

        Cache cache = new Cache(2);

        cache.set(1, 100,2);
        //cache.set(2, 200,10);
       // cache.set(3, 300);

       // System.out.println("Get 1: " + cache.get(1));


        try {
            Thread.sleep(5000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        //cache.set(3, 300);


        //System.out.println("Get 2: " + cache.get(2));
        System.out.println("Get 1: " + cache.get(1));
        //System.out.println("Get 3: " + cache.get(3));
        cache.shutdown();
    }
}

