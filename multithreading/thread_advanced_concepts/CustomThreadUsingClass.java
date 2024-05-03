package multithreading.thread_advanced_concepts;

/*
 * CustomThread creation using Thread class.
 * */

public class CustomThreadUsingClass extends Thread {

    public CustomThreadUsingClass() {
        super();
    }

    @Override
    public void run() {
        System.out.println("This is our custom thread : "  + Thread.currentThread().getName());
    }

}
