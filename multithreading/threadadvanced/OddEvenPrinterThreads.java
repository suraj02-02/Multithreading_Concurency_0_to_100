/*  @Author Suraj.Yadav
 *
 *  This is the implementation for odd / even thread capabilties.
 *  It is one of the most commonly asked multi-threading coding questions
 *  where knowledge of thread synchronisation , shared locking , inter-thread communication
 *  is required.
 *
 * */

public class OddEvenPrinterThreads {

    private static Object sharedLock = new Object();
    static int start = 0;
    static int max = 20;

    public static void main(String[] args) {

        // Thread created which will print only even number ranging b/w (@start to @max)
        Thread evenThread = new Thread(() -> {
            try {
                printEvenNums();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        // Thread created which will print only odd number ranging b/w (@start to @max)
        Thread oddThread = new Thread(() -> {
            try {
                printOddNums();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        evenThread.setName("EvenNumThread");
        oddThread.setName("OddNumThread");
        evenThread.start();
        oddThread.start();

    }

    // This behaviour is executed by #OddNumThread where it has the capability to print only odd numbers
    private static void printOddNums() throws InterruptedException {

        while(start <= max){
            synchronized (sharedLock){
                if(start % 2 != 0){
                    System.out.println("Thread Name : " + Thread.currentThread().getName() + " " + start);
                    start ++;
                    sharedLock.notifyAll();
                }else {
                    sharedLock.wait();
                }
            }
        }

    }

    // This behaviour is executed by #OddNumThread where it has the capability to print only even numbers
    private static void printEvenNums() throws InterruptedException {

        while(start <= max){
            synchronized (sharedLock){
                if(start % 2 == 0){
                    System.out.println("Thread Name : " + Thread.currentThread().getName() + " " + start);
                    start ++;
                    sharedLock.notifyAll();
                }else {
                    sharedLock.wait();
                }
            }
        }
    }
}