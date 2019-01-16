package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger("Main");

    public static void main(String[] args) {
        /**
         * Zadanie 1
         */
       int numOfProc = Utils.getNumberOfProcessors();
        /**
         * Same as typing in bash terminal :
         * cat /proc/cpuinfo
         * whick lists available processors and more info
         *
         */

        /**
         * Zadanie 2
         */
        new Thread(new HelloWorldRunnable()).start();

        /**
         * Zadanie 3
         */
        //Zadanie3(20, 100,1,"zadanie3.txt",null);
        /**
         * Zadanie 4
         */
        //Zadanie3( 20 , 100,numOfProc,"zadanie4.txt",null);
        /**
         * Zadanie 5
         */
//        ExecutorService executor = Executors.newFixedThreadPool(8);
//        System.out.println("Executor Stworzono : "+executor);
//        Zadanie3(20,100,8,"zadanie5.txt",executor);
//
        int maxSizeOfTable=20;
        int numberOfTimesAveraged=100;
        String filename = "zadanie5.txt";
        int numberOfThreads = numOfProc;
       // CompletionService<Double> service = new ExecutorCompletionService<>(Executors.newFixedThreadPool(numberOfThreads));
        //SumTable(numberOfTimesAveraged,maxSizeOfTable,numberOfThreads,filename,service);

        /**
         * Zadanie 6
         */

        filename="zadanie6.txt";
        RaceSumTable(filename,numberOfThreads,numberOfTimesAveraged,maxSizeOfTable);


    }

    private static void RaceSumTable(String filename, int numberOfThreads, int numberOfTimesAveraged, int maxSizeOfTable) {
        List<Long> runTimes = new ArrayList<>(maxSizeOfTable);
        List<Integer> sizes = new ArrayList<>(maxSizeOfTable);
        for (int i = 2; i < maxSizeOfTable ; i++) {

            Double partialSum =Double.valueOf(0);
            Long deltaTime= Long.valueOf(0);
            List<Double> list = Utils.getRandomDoubleList((int) Math.pow(2 , i),1);
            List<RaceSum> tasksCallable = new ArrayList<>();
            AtomicIntegerArray flags = new AtomicIntegerArray(list.size());
            List<FutureTask<Double >> futureTasks = new ArrayList<>();
            List<Thread> threads = new ArrayList<>();
            sizes.add(i);

            Utils.measureTimeStart();
            /**
             * Tworzymy Taski
             */
            for (int j = 0; j < numberOfThreads; j++) {
                tasksCallable.add(new RaceSum(list,flags,numberOfTimesAveraged));
                futureTasks.add(new FutureTask<Double>(tasksCallable.get(j)));
                threads.add(new Thread(futureTasks.get(j)));
            }
            /**
             * Startujemy
             */
            for (Thread t :
                    threads) {
                t.start();
            }
            /**
             * Upewniamy się że wszystkie sie skończyły
             */

            for (FutureTask task :
                    futureTasks) {
                try {
                    task.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            deltaTime = Utils.measureTimeStop();
            runTimes.add(deltaTime);
        }
        /**
         * Zapisujemy wyniki do pliku
         */
        try {
            Utils.writeToFile(filename,runTimes,sizes);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void SumTable(int numberOfTimesAveraged, int maxSizeOfTable,int numberOfDivisions, String filename, CompletionService<Double> service) {
        List<Long> runTimes = new ArrayList<>(maxSizeOfTable);
        List<Integer> sizes = new ArrayList<>(maxSizeOfTable);
        logger.info(filename);

        for (int i = 2; i < maxSizeOfTable ; i++) {

            Double partialSum =Double.valueOf(0);
            Long deltaTime= Long.valueOf(0);
            List<Double> list = Utils.getRandomDoubleList((int) Math.pow(2 , i),1);
            List<List<Double>> dividedLists = Utils.divideList(list,numberOfDivisions);
            List<SumTableCallableTask> tasksCallable = new ArrayList<>(numberOfDivisions);
            sizes.add(i);

            Utils.measureTimeStart();
            /**
             * Tworzymy Taski
             */
            for (List<Double> smallList : dividedLists) {
                tasksCallable.add(new SumTableCallableTask(numberOfTimesAveraged,smallList));
            }
            /**
             * Dodajemy do executora
             */
            for (SumTableCallableTask task :
                    tasksCallable) {
                service.submit(task);
            }
            /**
             * Upewniamy się że wszystkie sie skończyły 
             */
            for (SumTableCallableTask task :
                    tasksCallable) {
                try {
                    try {
                        partialSum += service.take().get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            deltaTime = Utils.measureTimeStop();
            runTimes.add(deltaTime);
        }
        /**
         * Zapisujemy wyniki do pliku
         */
        try {
            Utils.writeToFile(filename,runTimes,sizes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static void Zadanie3(int size, int nMean, int nThreads, String filename, ExecutorService executor) {
        List<Long> runTimes = new ArrayList<>(size);
        List<Integer> sizes = new ArrayList<>(size);
        System.out.println("Number of Threads : "+nThreads);
        Boolean isAlive = Boolean.TRUE;
        for (int i = 2; i < size ; i++) {
            long deltaTime = 0 ;
            List<Double> list = Utils.getRandomDoubleList((int) Math.pow(2 , i),1);
            LogSumTable logSumTable = new LogSumTable(nMean,list,nThreads,executor);
            Utils.measureTimeStart();
            logSumTable.startSum();
            logSumTable.getSum();
            if(executor == null ) {
                for (Thread thread :
                        logSumTable.getThreads())
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
            else {
                executor.shutdown();
                try {
                    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                }
            }
            deltaTime= Utils.measureTimeStop()/nMean;
            runTimes.add(deltaTime);
            sizes.add(i);
            System.out.println("For size 2^"+i+" time : "+deltaTime);
        }

        try {
            Utils.writeToFile(filename,runTimes,sizes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
