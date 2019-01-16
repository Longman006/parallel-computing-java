package com.company;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Created by longman on 11.01.19.
 */
public class RaceSum implements Callable<Double>{
    AtomicIntegerArray flags;
    List<Double> tableToSum;
    double localSum;
    int nAverage;

    @Override
    public Double call() throws Exception {
        Double tempSum= Double.valueOf(0);
        for (int i = 0; i < tableToSum.size(); i++) {
            if(flags.get(i) == 0){
                /**
                 * Dany element jest wolny ustawmy ze się nim zajmiemy
                 */
                if(flags.compareAndSet(i,0,1 ) == Boolean.TRUE){
                    for (int j = 0; j < nAverage; j++) {
                        tempSum+=Math.log(tableToSum.get(i));

                    }
                    localSum+=tempSum/nAverage;
                }
            }
        }
        return localSum;
    }

    public RaceSum(List<Double> tableToSum,AtomicIntegerArray flags, int nAverage){
        this.flags = flags;
        this.tableToSum = tableToSum;
        this.nAverage = nAverage;

    }



}
