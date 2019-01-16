package com.company;



import javax.print.DocFlavor;
import javax.xml.crypto.dom.DOMCryptoContext;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by longman on 13.12.18.
 */
public class LogSumTable {
    ExecutorService executor;
    private int nMean;
    private int nThreads;
    private List<Double> toSum;
    List<List<Double>> divededTables ;
    List<Runnable> runners;
    List<Thread> threads;
    private double sum = 0;


    public LogSumTable(int nMean, List<Double> toSum , int nThreads, ExecutorService executor) {
        this.nMean=nMean;
        this.toSum=toSum;
        this.nThreads = nThreads;
        this.executor = executor;
        this.divededTables=Utils.divideList(toSum,nThreads);
        System.out.println("Moj Executor : "+ executor);

    }

    public List<Thread> getThreads() {
        return threads;
    }

    public void startSum() {
        runners = new ArrayList<>(nThreads);
        threads = new ArrayList<>(nThreads);
        for (int j = 0; j < nThreads ; j++) {
            int finalJ = j;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    sumTable(divededTables.get(finalJ),nMean);
                }
            };
            runners.add(runnable);
            Future<?> future = null;
            if(executor != null)
                System.out.println(runnable);
                future = executor.submit(runnable);
        }
        for (int i = 0; i < nThreads; i++) {
            threads.add(new Thread(runners.get(i)));
            threads.get(i).start();
        }


    }
    private double sumTable(List<Double> toSum, int nMean){
        double sumTemp=0;
        for (int i = 0; i < nMean; i++) {
            sumTemp = 0;
            for (Double number :
                    toSum) {
                sumTemp += Math.log(number);
            }
            sumTemp = sumTemp/nMean;
        }
        sum+=sumTemp;
        return sumTemp;
    }
    public double getSum(){
        return sum;
    }
}
