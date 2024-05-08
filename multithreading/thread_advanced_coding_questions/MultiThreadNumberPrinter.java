package multithreading.thread_advanced_coding_questions;

/* Problem Statement :
 *
 * WAP that creates three threads, each responsible for printing numbers in sequence.
 * The first thread prints all numbers divisible by 3 up to a given limit,
 * the second thread prints all numbers divisible by 5 up to the same limit,
 * and the third thread prints all numbers divisible by 7 up to the limit.
 * Your program should use proper synchronization mechanisms to ensure that the numbers are printed in the correct sequence: 3, 5, 7, 6, 9, 10, 12, 14, ..., up to the given limit.
 * */


public class MultiThreadNumberPrinter {

    private static final int maxLimit = 20;
    private static int counter = 0;
    private static final Object sharedLock = new Object();

    public static void main(String[] args) {
        // create three threads
        Thread t1 = new Thread(() -> {
            try {
                checkDivisibiltyBy3();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                checkDivisibiltyBy5();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread t3 = new Thread(() -> {
            try {
                checkDivisibiltyBy7();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        t1.setName("3-Divisible");
        t2.setName("5-Divisible");
        t3.setName("7-Divisible");
        t1.start();
        t2.start();
        // t3.start();
    }

    private static void checkDivisibiltyBy7() throws InterruptedException {
        while(counter <= maxLimit){
            synchronized (sharedLock){
                if(counter % 7 == 0) {
                    System.out.println(Thread.currentThread().getName() + " : " + counter);
                    counter ++;
                    sharedLock.wait();
                }
                counter++;
                sharedLock.notifyAll();
            }
        }
    }

    private static void checkDivisibiltyBy5() throws InterruptedException {
        while(counter <= maxLimit) {
            synchronized (sharedLock){
                if(counter % 5 == 0){
                    System.out.println(Thread.currentThread().getName() + " : " + counter);
                    counter ++;
                    sharedLock.wait();
                }
                counter++;
                sharedLock.notifyAll();
            }
        }
    }


    private static void checkDivisibiltyBy3() throws InterruptedException {
        while(counter <= maxLimit) {
            synchronized (sharedLock){
                if(counter % 3 == 0){
                    System.out.println(Thread.currentThread().getName() + " : " + counter);
                    counter ++;
                    sharedLock.wait();
                }
                counter++;
                sharedLock.notifyAll();
            }
        }
    }

}
