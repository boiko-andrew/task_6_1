package ru.netology;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        long startTs = System.currentTimeMillis();  // start time
        for (String text : texts) {
            int maxSize = 0;
            for (int i = 0; i < text.length(); i++) {
                for (int j = 0; j < text.length(); j++) {
                    if (i >= j) {
                        continue;
                    }
                    boolean bFound = false;
                    for (int k = i; k < j; k++) {
                        if (text.charAt(k) == 'b') {
                            bFound = true;
                            break;
                        }
                    }
                    if (!bFound && maxSize < j - i) {
                        maxSize = j - i;
                    }
                }
            }
            System.out.println(text.substring(0, 100) + " -> " + maxSize);
        }
        long endTs = System.currentTimeMillis();  // end time

        double regularTime = (double) (endTs - startTs);
        System.out.println("Time without threads: " + regularTime + " ms.");
        System.out.println();


        List<Thread> threadList = new ArrayList<>();

        startTs = System.currentTimeMillis();  // start time

        for (String text : texts) {
            threadList.add(getNewThread(text));
        }

        for (Thread thread : threadList) {
            thread.start();
        }

        for (Thread thread : threadList) {
            thread.join();
        }

        endTs = System.currentTimeMillis();  // end time
        double threadsTime = (double) (endTs - startTs);

        double timeRatio;
        if (threadsTime != 0) {
            timeRatio = regularTime / threadsTime;
        } else {
            timeRatio = 1;
        }

        String stringTimeRatio = String.format("%.2f", timeRatio);

        System.out.println("Time with threads: " + (endTs - startTs) + " ms.");
        System.out.println("Time ratio: " + stringTimeRatio + ".");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static Thread getNewThread(String text) {
        return new Thread(
                () -> {
                    int maxSize = 0;
                    for (int i = 0; i < text.length(); i++) {
                        for (int j = 0; j < text.length(); j++) {
                            if (i >= j) {
                                continue;
                            }
                            boolean bFound = false;
                            for (int k = i; k < j; k++) {
                                if (text.charAt(k) == 'b') {
                                    bFound = true;
                                    break;
                                }
                            }
                            if (!bFound && maxSize < j - i) {
                                maxSize = j - i;
                            }
                        }
                    }
                    System.out.println(text.substring(0, 100) + " -> " + maxSize);
                }
        );
    }
}