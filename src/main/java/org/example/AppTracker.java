package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AppTracker {


    private FileWriter writer;

    private Set<String> registeredProcesses = new HashSet<>();
    private SimpleDateFormat sdf;
    private File folder;

    public AppTracker() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(new Date());
        this.folder = new File("App Tracker за "+date);
        this.folder.mkdir();
        sdf = new SimpleDateFormat("HH:mm:ss");
    }
    public void startTracking() {
        try {
            File file = new File(folder,"processes.txt");
            writer = new FileWriter(file, true);
            sdf = new SimpleDateFormat("HH:mm:ss");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            List<String> runningProcesses = getRunningProcesses();
            for (String process : runningProcesses) {
                if (!registeredProcesses.contains(process)) {
                    try {
                        writer.write("[" + sdf.format(new Date()) + "] " + process + " started.\n");
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    registeredProcesses.add(process);
                }
            }

            // check for closed processes
            Set<String> closedProcesses = new HashSet<>(registeredProcesses);
            closedProcesses.removeAll(runningProcesses);
            for (String closedProcess : closedProcesses) {
                try {
                    writer.write("[" + sdf.format(new Date()) + "] " + closedProcess + " closed.\n");
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                registeredProcesses.remove(closedProcess);
            }

            // sleep for a little while before checking again
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private List<String> getRunningProcesses() {
        List<String> processes = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec("tasklist");
            Scanner scanner = new Scanner(process.getInputStream());
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Image Name")) {
                    continue;
                }
                processes.add(line.split(" ")[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return processes;
    }



}
