package multithreading.thread_advanced_concepts;

public class CustomThreadUsingInterface implements Runnable{

    @Override
    public void run() {
        System.out.println("This is our custom thread : "  + Thread.currentThread().getName());
    }
}
