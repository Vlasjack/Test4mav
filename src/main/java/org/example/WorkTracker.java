package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WorkTracker {
    private static final int START_HOUR = 6;
    private static final int END_HOUR = 18;
    private static final int MAX_SLEEP_DURATION = 30; // in minutes

    private FileWriter writer;
    private FileWriter violationWriter;
    private SimpleDateFormat sdf;
    private boolean isTracking;
    private Timer timer;
    private long sleepStartTime;

    public WorkTracker() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(new Date());
        File folder = new File("Work Tracker за " + date);
        folder.mkdir();
        File logFile = new File(folder, "log.txt");
        File violationFile = new File(folder, "violations.txt");
        try {
            writer = new FileWriter(logFile, true);
            violationWriter = new FileWriter(violationFile, true);
            sdf = new SimpleDateFormat("HH:mm:ss");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startTracking() {
        if (isTracking) {
            return;
        }

        logMessage("Tracking started.");
        isTracking = true;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                if (hour < START_HOUR || hour >= END_HOUR) {
                    logViolation("Working outside of designated hours.");
                }

                if (sleepStartTime > 0) {
                    long currentTime = System.currentTimeMillis();
                    long sleepDuration = (currentTime - sleepStartTime) / (60 * 1000);
                    if (sleepDuration > MAX_SLEEP_DURATION) {
                        logViolation("Slept for more than " + MAX_SLEEP_DURATION + " minutes.");
                    }
                    sleepStartTime = 0;
                }
            }
        }, 0, 60000);
    }

    public void stopTracking() {
        if (!isTracking) {
            return;
        }

        logMessage("Tracking stopped.");
        isTracking = false;
        timer.cancel();
    }

    public void computerSleep() {
        sleepStartTime = System.currentTimeMillis();
        logMessage("Computer went to sleep.");
    }

    private void logMessage(String message) {
        try {
            writer.write("[" + sdf.format(new Date()) + "] " + message + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logViolation(String message) {
        try {
            violationWriter.write("[" + sdf.format(new Date()) + "] " + message + "\n");
            violationWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
