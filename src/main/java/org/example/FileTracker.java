package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileTracker {
    private List<String> directories;
    private WatchService watchService;
    private File folder;

    public FileTracker() {
        this.directories = new ArrayList<>();
    }

    public void addDirectory(String directory) {
        this.directories.add(directory);
    }

    public void startTracking() throws IOException, InterruptedException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(new Date());
        this.folder = new File("FileTracker за "+date);
        this.folder.mkdir();
        this.watchService = FileSystems.getDefault().newWatchService();

        for (String directory : directories) {
            Path path = Paths.get(directory);
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
        }

        while (true) {
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                String eventType = event.kind().name();
                Path filePath = (Path) event.context();
                String fileName = filePath.toString();
                String directory = key.watchable().toString();
                logEvent(directory, fileName, eventType);
            }
            key.reset();
        }
    }

    private void logEvent(String directory, String fileName, String eventType) {

        File logFile = new File(this.folder.getAbsolutePath() + "\\file-events.log");;
        try (FileWriter fileWriter = new FileWriter(logFile, true)) {
            fileWriter.write(directory+ ": " + eventType + ": " + fileName + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
