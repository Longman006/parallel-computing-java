package com.company;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * Created by longman on 18.12.18.
 */
public class SumTableCallableTask implements Callable<Double> {
    private static final Logger logger = Logger.getLogger("SumTableCallableTask");
    int numberOfTimesAveraged;
    List<Double> tableToSum;
    Double sum;

    public SumTableCallableTask(int numberOfTimesAveraged, List<Double> tableToSum) {
        this.numberOfTimesAveraged = numberOfTimesAveraged;
        this.tableToSum = tableToSum;
        this.sum=Double.valueOf(0);
    }

    private Double SumTable(){
        for (int i = 0; i < numberOfTimesAveraged ; i++) {
            for (Double toSum:
                 tableToSum) {
                sum+=toSum;
            }
        }
        sum=sum/numberOfTimesAveraged;
        return sum;
    }
    @Override
    public Double call() throws Exception {
        return SumTable();
    }
}
