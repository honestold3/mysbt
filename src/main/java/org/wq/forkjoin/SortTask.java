package org.wq.forkjoin;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * Created by wq on 14-6-11.
 */
public  class SortTask extends RecursiveAction {
    final long[] array;
    final int lo;
    final int hi;
    private int THRESHOLD = 0; //For demo only

    public SortTask(long[] array) {
        this.array = array;
        this.lo = 0;      //0
        this.hi = array.length - 1;
    }

    public SortTask(long[] array, int lo, int hi) {
        this.array = array;
        this.lo = lo;
        this.hi = hi;
    }

    protected void compute() {

        //如果数据量比较少,就直接使用系统的排序方式
        if (hi - lo < THRESHOLD)
            sequentiallySort(array, lo, hi);
        else {
            int pivot = partition(array, lo, hi);
            //把数组切分成两部分,然后在分别做排序
            this.invokeAll(new SortTask(array,
                    pivot + 1, hi), new SortTask(array, lo, pivot - 1))   ;


        }
    }

    private int partition(long[] array, int lo, int hi) {

        long x = array[hi];
        //-1
        int i = lo - 1;
        for (int j = lo; j < hi; j++) {
            if (array[j] <= x) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, hi);
        return i + 1;
    }

    private void swap(long[] array, int i, int j) {
        if (i != j) {
            long temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    private static void sequentiallySort(long[] array, int lo, int hi) {
        Arrays.sort(array, lo, hi + 1);
    }

    public static void main(String agrs[]) throws InterruptedException {

        int length=90960000;
        Random r=new Random();
        long array[]=new long[length] ;

        for(int i=0;i<length;i++){
            array[i]=r.nextInt(19087);
        }

        long t1=System.currentTimeMillis();

        sequentiallySort(array,0,array.length-1);

        System.out.println("自然排序消耗时间为：" +( System.currentTimeMillis() - t1) );


        //重新赋值

//        for(int i=0;i<length;i++){
//            array[i]=r.nextInt(19087);
//        }
//        long t2=System.currentTimeMillis();
//        ForkJoinTask sort = new SortTask(array);
//        ForkJoinPool fjpool = new ForkJoinPool();
//        fjpool.submit(sort);
//        fjpool.shutdown();
//        fjpool.awaitTermination(30, TimeUnit.SECONDS);
//        System.out.print("fork/join并行消耗时间为："+(System.currentTimeMillis()-t2));
    }
}