package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static final BlockingQueue<String> queueOne = new ArrayBlockingQueue<>(100);
    public static final BlockingQueue<String> queueTwo = new ArrayBlockingQueue<>(100);
    public static final BlockingQueue<String> queueThree = new ArrayBlockingQueue<>(100);
    public static final int AMOUNT_TEXT = 10_000;
    public static final int LENGTH_TEXT = 100_000;


    public static void main(String[] args) throws InterruptedException {

        Thread completion = new Thread(() -> {
            for (int i = 0; i < AMOUNT_TEXT; i++) {
                String text = generateText("abc", LENGTH_TEXT);
                queueOne.add(text);
                queueTwo.add(text);
                queueThree.add(text);
            }
        });

        Thread symbolA = new Thread(() -> {
            maxCount('a', queueOne);
        });


        Thread symbolB = new Thread(() -> {
            maxCount('b', queueTwo);
        });


        Thread symbolC = new Thread(() -> {
            maxCount('c', queueThree);
        });

        completion.start();
        symbolA.start();
        symbolB.start();
        symbolC.start();

        symbolB.join();
        symbolA.join();
        completion.join();
        symbolC.join();
    }

    public static void maxCount(char symbol, BlockingQueue<String> queue) {
        int maxSymbol = 0;
        for (int i = 0; i < AMOUNT_TEXT; i++) {
            try {
                int count = 0;
                String text = queue.take();
                for (int j = 0; j < LENGTH_TEXT; j++) {
                    if (text.charAt(j) == symbol) {
                        count++;
                    }
                }
                if (maxSymbol < count) {
                    maxSymbol = count;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Максимальное количество букв '" + symbol + "' - " + maxSymbol + " шт.");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}