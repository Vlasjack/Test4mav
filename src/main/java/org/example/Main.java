package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        ClipboardLogger clipboardLogger = new ClipboardLogger();
        KeyLogger KeyLogger = new KeyLogger();
        AppTracker appTracker = new AppTracker();
        FileTracker fileTracker = new FileTracker();
        ScreenshotTaker screenshotTaker = new ScreenshotTaker();
        WorkTracker workTracker = new WorkTracker();

        fileTracker.addDirectory("E:\\TEST\\TEST2");
        fileTracker.addDirectory("E:\\TEST");

        Thread clipboardThread = new Thread(new Runnable() {
            public void run() {
                try {
                    clipboardLogger.startLogging();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Thread keyloggerThread = new Thread(new Runnable() {
            public void run() {
                try {
                    KeyLogger.startLogging();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread appTrackerThread = new Thread(new Runnable() {
            public void run() {
                try {
                    appTracker.startTracking();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread fileTrackerThread = new Thread(new Runnable() {
            public void run() {
                try {
                    fileTracker.startTracking();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread screenshotThread = new Thread(new Runnable() {
            public void run() {
                try {
                    screenshotTaker.startScreenshotting();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread workTrackerThread = new Thread(new Runnable() {
            public void run() {
                try {
                    workTracker.startTracking();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        // clipboardThread.start();
        // keyloggerThread.start();
        // appTrackerThread.start();
        // fileTrackerThread.start();
        // screenshotThread.start();
        // workTrackerThread.start();
    }
}
