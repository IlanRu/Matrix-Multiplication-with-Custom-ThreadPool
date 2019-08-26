package CustomThreadPool;

import java.util.concurrent.CountDownLatch;

/**
 * A simple runnable imitating java's Task class.
 * Holds a small snippet of code to be performed once run is called.
 */

public class MatMulTask implements Runnable {
    private Matrix a;
    private Matrix b;
    private CountDownLatch latch;
    public MatMulTask(Matrix left, Matrix right, CountDownLatch latch){
        a = left;
        b = right;
        this.latch = latch;
    }
    @Override
    public void run() {
        Matrix product = a.matMul(b);//calculate the product
        synchronized(System.out){//just to make sure the calls aren't interspersed with different print calls
            System.out.println("Thread: " + Thread.currentThread().getName()+
                    " has produced the following product:" + System.lineSeparator() + product.toString());
        }
        latch.countDown();//reduce the count so that the producer thread can wake up once all tasks are done
        //System.out.println("Latch count: " + latch.getCount());
    }
}
