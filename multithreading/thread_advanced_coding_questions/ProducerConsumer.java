package multithreading.thread_advanced_coding_questions;

/*
 * @Author : Suraj.Yadav
 *
 * This is a custom implementation done for Producer-Consumer problem.
 * Two threads :
 *     1.Producer-Thread -> Produces data and pushes it to #arrayQueue
 *     2.Consumer-Thread -> Consumes data and pulls it from #arrayQueue
 *
 * The Producer-Consumer implementation is the base for the distributed systems like : RabbitMq , Kafka etc.
 * Thread Pools and executor services also works on this design pattern only
 *
 * */

public class ProducerConsumer {

    // this will act as a buffer storage for data
    private static final Object[] arrayQueue = new Object[10];
    // this will act as a shared lock for both threads
    private static final Object commonResource = new Object();
    // the flag is used to check if producer has started the work ,
    // if yes then we will notify the consumer thread to start consuming data so that there is no latency in data consumption
    private static volatile boolean isProducerWorking = false;
    
    
    public static void main(String[] args) {

        // Start the producer and consumer threads
        Thread producerThread = new Thread(ProducerConsumer::producer);
        Thread consumerThread = new Thread(ProducerConsumer::consumer);
        producerThread.setName("Producer-Thread");
        consumerThread.setName("Consumer-Thread");
        producerThread.start();
        consumerThread.start();
    }


    private static void producerTask() throws InterruptedException {

        int producedData = 0;
        while (true) {
        /*
         * the #commonResource act as a shared resource between producer and consumer threads
         * It will be used for inter thread communications.
         * */
            synchronized (commonResource) {
                if(isQueueFull()) {
                     System.out.println("Queue is full! please wait while consumer is working");
                     commonResource.wait();
                }
                else {
                    int fillingIndex = 0;
                    while (arrayQueue[fillingIndex] != null) {
                        fillingIndex++;
                    }
                    producedData = getProducedData(fillingIndex, arrayQueue.length , producedData);
                }
            }
        }
    }

    private static void consumerTask() throws InterruptedException {

        while (true) {
            synchronized (commonResource) {
                if(isQueueEmpty()) {
                      System.out.println("Queue is empty! please wait while producer is working");
                      commonResource.wait();
                }
                else {
                    consumeAndRemoveDataFromQueue();
                }
            }
        }
    }


    /******************** Helper Methods **********************/

    /**
     * Checks if the given array is empty.
     *
     * @return        true if the array is empty, false otherwise
     */

    private static boolean isQueueEmpty() {
         return ProducerConsumer.arrayQueue[0] == null;
    }

    /**
     * Determines if the given array is full by checking if the last element is not null.
     * @return true if the array is full, false otherwise
     */

    private static boolean isQueueFull() {
        return ProducerConsumer.arrayQueue[ProducerConsumer.arrayQueue.length - 1] != null;
    }

    /**
     * Executes the producer task in a separate thread.
     * @throws InterruptedException if the producer thread is interrupted while waiting
     */

    private static void producer() {
        try {
            producerTask();
        } catch (InterruptedException e) {
            System.out.println("Producer thread interrupted");
        }
    }

    /**
     * Executes the consumer task in a separate thread.
     * @throws InterruptedException if the consumer thread is interrupted while waiting
     */

    private static void consumer() {
        try {
            consumerTask();
        } catch (InterruptedException e) {
            System.out.println("Consumer thread interrupted");
        }
    }

    /**
     * Generates and produces data in the given array queue.
     *
     * @param  fillingIndex  the index at which the data generation should start
     * @param  queueSize     the size of the array queue
     * @param  producedData  the current value of the produced data
     * @return               the final value of the produced data after generation
     */

    private static int getProducedData(int fillingIndex, int queueSize, int producedData) {

        for(int i = fillingIndex; i < queueSize ; i++) {
            producedData  = ++producedData;
            System.out.println("Data produced by Producer : " + producedData);
            arrayQueue[i] = producedData;

            /* Once the producer has produced data in queue , it will notify the waiting consumer thread to start consuming data
             * This ensures the parallel working of consumer and producer threads for better performance and low latency
             * */
            if(!isProducerWorking){
                 isProducerWorking = true;
                 commonResource.notifyAll();
            }
        }
        return producedData;
    }

    /**
     * Consumes and removes data from the queue.
     */

    private static void consumeAndRemoveDataFromQueue() {

        int i = 0;
        while(i < arrayQueue.length && arrayQueue[i] != null) {
            System.out.println("Data consumed : " + arrayQueue[i]);
            arrayQueue[i] = null;
            i++;
            if(isProducerWorking) {
                isProducerWorking = false;
                commonResource.notifyAll();
            }
        }
    }

}