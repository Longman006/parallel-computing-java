package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by longman on 13.12.18.
 */
public class Utils {
    private static long startTime;
    private static long endTime;

    public static int getNumberOfProcessors(){
        int numOfProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("Number of processors : "+numOfProcessors);
        return numOfProcessors;

    }
    public static void measureTimeStart() {
        startTime = System.nanoTime();
    }
    public static long measureTimeStop(){
        endTime = System.nanoTime();
        return endTime-startTime;
    }
    public static List<Double> getRandomDoubleList(int size, double max){
        List<Double> newList = new ArrayList<>(size);
        Random random = new Random();

        for (int i = 0; i < size ; i++) {
            double newElement = random.nextDouble()*max;
            newList.add(newElement);
        }

        return newList;
    }
    public static void writeToFile(String fileName,List<Long> time, List<Integer> size)
        throws IOException {

            FileWriter fileWriter = new FileWriter(fileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("Size\tTime\n");
        for (int i = 0; i < time.size() ; i++) {
            printWriter.printf("%d\t%d\n", size.get(i), time.get(i));
        }
        printWriter.close();
    }
    public static List<List<Double>> divideList(List<Double> list, int n){
        int delta = list.size()/n;
        List<List<Double>> lists = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            lists.add(new ArrayList<>(delta));
            for (int j = i*delta; j < (i+1)*delta  ; j++) {
                lists.get(i).add(list.get(j));
            }
        }
        return lists;
    }

}
