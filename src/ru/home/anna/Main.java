package com.company;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int arrSize, numTreads;
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int[] data;
//        int sum = 0;

        System.out.println("Enter number of the array elements: ");
        arrSize = scanner.nextInt();
        System.out.println("Enter number of threads: ");
        numTreads = scanner.nextInt();

        data = new int[arrSize];


//        System.out.println("Before");
        for (int i = 0; i < arrSize; i++) {
            data[i] = random.nextInt(10) + 1;
//            System.out.println(i + ". " + data[i]);
        }

        long time = System.currentTimeMillis();
        getMathSquaresInThreads(arrSize, numTreads, data);
//        getSumInThreads(arrSize, numTreads, data);
        long res1 = (System.currentTimeMillis() - time);
        System.out.println("time: " + res1);

        long time2 = System.currentTimeMillis();
        getMathSquares(arrSize, data);
        long res2 = (System.currentTimeMillis() - time2);
        System.out.println("time 2: "+res2);

    }

    static void getMathSquares(int arrSize, int[] data) {
//        System.out.println("After 2");
        for (int i = 0; i < arrSize; i++) {
            data[i] = data[i] * data[i];
//            System.out.println(i + ". " + data[i]);
        }
    }

    static void getMathSquaresInThreads(int arrSize, int numTreads, int[] data) {
        Thread[] threads = new Thread[numTreads];
        int numElementsInArr = arrSize / numTreads;
        int rem = arrSize % numTreads;
        for (int i = 0; i < rem; i++) {
            int index = arrSize - 1 - i;
            data[index] = data[index] * data[index];
        }
        for (int i = 0; i < numTreads; i++) {
            int start = i * numElementsInArr;
            int end = start + numElementsInArr;

            threads[i] = new Thread(new SquareThread(data, start, end));
            threads[i].start();
        }
        for (int i = 0; i < numTreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        System.out.println("After");
        for (int i = 0; i < arrSize; i++) {
//            System.out.println(i + ". " + data[i]);
        }
    }

    static void getSumInThreads(int arrSize, int numTreads, int[] data) {
        int sum = 0;
        Thread[] threads = new Thread[numTreads];
        int numElementsInArr = arrSize / numTreads;
        int rem = arrSize % numTreads;
        SumThread []arrSum = new SumThread[numElementsInArr];

        for (int i = 0; i < numTreads; i++) {
            int start = i * numElementsInArr;
            int end = start + numElementsInArr;
            SumThread sumThread = new SumThread(data, start, end);
            arrSum[i] = sumThread;
            threads[i] = new Thread(arrSum[i]);
                threads[i].start();
        }
        for (int i = 0; i < rem; i++) {
            int index = arrSize - 1 - i;
            sum = sum+ data[index];
        }
        for (int i = 0; i < numTreads; i++) {
            try {
                threads[i].join();
                System.out.println("sum tread = "+arrSum[i].getSum());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sum = sum+arrSum[i].getSum();
        }
        System.out.println("sum = "+sum);
    }
}


class SquareThread implements Runnable {
    private int[] data;
    private int start, end;

    public SquareThread(int[] data, int start, int end) {
        this.data = data;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            data[i] = data[i] * data[i];
        }
    }
}

class SumThread implements Runnable {
    private int[] data;
    private int start, end, sum;


    public SumThread(int[] data, int start, int end) {
        this.data = data;
        this.start = start;
        this.end = end;
        this.sum = 0;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            sum = sum+ data[i];
        }
    }

    int getSum() {
        return sum;
    }
}
