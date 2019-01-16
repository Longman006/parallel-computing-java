package com.company;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

/**
 * Created by longman on 18.12.18.
 */
public class ResultExecutableTask extends FutureTask<Double>{

    private static final Logger logger = Logger.getLogger("ResultExecutableTask");

    public ResultExecutableTask(Callable<Double> callable) {
        super(callable);
    }

}
