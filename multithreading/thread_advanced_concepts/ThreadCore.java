package multithreading.thread_advanced_concepts;

/*
 * This class has all core concepts of threads specifically in java.
 * */

public class ThreadCore {

    public static void main(String[] args)  {

        // The below instructions will be executed by main / primordial thread
        System.out.println("Current thread: " + Thread.currentThread().getName());

        /*
         * Here we are doing the creation of our custom thread using @Thread class
         * While customThread creation below things are done internally :
         *
         *     1. Instantiation of Thread class object , no OS level thread has been created till now
         *     2. If no threadGroup is passed , then our custom thread will be part of main thread group
         *     3. Each newly created thread is contained inside a holder @FieldHolder class having below properties :
         *            -> ThreadGroup group
         *            -> Runnable task
         *            -> long stackSize
         *            -> int priority
         *            -> boolean daemon
         *     4.FieldHolder act as an Encapsulator for Thread class properties
         *
         *     5.Once we call the #start() method on the thread object then the execution of thread starts :
         *            -> start method is wrapped with a #synchronised(this) block so that no 2 same threads can be started again
         *            -> @threadStatus property of @FieldHolder class is checked everyTime #start() method is called on a thread object;
         *                          if(@threadStatus != 0)
         *                               throw Exception
         *                          else
         *                             start the execution by creating a new OS level thread
         *            -> The creation of OS level thread is done in native code
         *            -> Stack memory is assigned to the thread.
         *            -> Now there 2 ways in which the task can be performed by a thread :
         *                  1. Overriding #run() method.
         *                  2. Provide runnable implementation as lambda expression inside @Thread class constructor. The task is the assigned to a task property of
         *                     @FieldHolder class which is created while new thread object creation.
         *           -> Once the thread completes the task provided it gets terminated and it's resources are cleaned up
         *
         * */

        // By extending the Thread class directly
        CustomThreadUsingClass customThread = new CustomThreadUsingClass();
       // customThread.start();

        // For creating a custom thread using @Interface we need to pass the runnable impl in Thread class constructor.
        CustomThreadUsingInterface customThreadUsingInterface = new CustomThreadUsingInterface();
        Thread thread = new Thread(customThreadUsingInterface);
       // thread.start();

        // We can also pass runnable impl as a lambda expression in Thread class constructor
        // this is the most concise way to create a custom thread.
        Thread thread2 = new Thread(() -> System.out.println("This is our custom thread : "  + Thread.currentThread().getName()));
        // thread2.start();

        threadInteruption();

    }


    private static void threadInteruption() {

        // creating and starting a new thread and providing it an intensive task (infinite loop)
        Thread monitoringThread = new Thread(ThreadCore::longRunningTask);
        try {
            monitoringThread.start();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        monitoringThread.interrupt();
    }

    private static void longRunningTask() {

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {

        }

        while(true){
            System.out.println("Thread Name : " + Thread.currentThread().getName() + "doing Intensive I/O operation");
            // this act a long running task probably mimiking of a thread running in loop or thread blockage
        }
    }


}
