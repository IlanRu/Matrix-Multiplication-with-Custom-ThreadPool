package CustomThreadPool;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/*
* A custom thread pool loosely based around java's ExecutorServices
* * */
public class ThreadPool {
    private final int numOfThreads;
    private final List<PoolThread> threads;
    private final BlockingQueue<Runnable> tasks;
    private CountDownLatch latch;

    /*
    Populate the threads list with the required num of threads
     */
    public ThreadPool(int num) {
        this.numOfThreads = num;
        tasks = new LinkedBlockingQueue();
        threads = new LinkedList<>();

        for (int i = 0; i < numOfThreads; i++) {
            PoolThread thread = new PoolThread(i+1);
            threads.add(thread);
            thread.start();
        }
    }

    public void setLatch(CountDownLatch l){
        latch = l;
    }
    /*
    Executes a given task by adding it to the queue and notifying any blocked thread
     */
    public void execute(Runnable task) {
        synchronized (tasks) {
            tasks.add(task);
            tasks.notify();
        }
    }

    /*
    * Private worker class.
    * Each thread polls the queue for a task to perform, if none are avilable the thread is blocked and awaits a change in availability
    * */
    private class PoolThread extends Thread {
        public PoolThread(int threadNum){
            super(Integer.toString(threadNum));
        }
        public void run() {
            Runnable task;
            while (true) {
                synchronized (tasks) {
                    while (tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            System.out.println( e.getMessage());
                        }
                    }
                    task = tasks.poll();
                }
                try {
                    task.run();
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
