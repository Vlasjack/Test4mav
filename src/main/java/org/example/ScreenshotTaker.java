package org.example;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

public class ScreenshotTaker {

    private static final int INTERVAL = 1; // in min
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public void startScreenshotting() throws Exception {
        while (true) {
            takeScreenshot();
            Thread.sleep(INTERVAL * 60 * 1000);
        }
    }

    private void takeScreenshot() throws Exception {
        String date = dateFormat.format(new Date());
        File folder = new File("Screenshots за " + date);
        folder.mkdir();
        String fileName = new SimpleDateFormat("HH:mm:ss").format(new Date()) + ".jpg";
        String filePath = folder.getAbsolutePath() + "\\" + fileName;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRect = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRect);
        ImageIO.write(image, "jpg", new File(filePath));
    }
}
