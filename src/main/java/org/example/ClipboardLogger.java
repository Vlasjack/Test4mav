package org.example;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

public class ClipboardLogger {

    private File folder;
    private File textFile;
    private FileWriter textWriter;
    private SimpleDateFormat sdf;
    private String previousClipboardContent;
    private BufferedImage previousImage;

    public ClipboardLogger() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(new Date());
        this.folder = new File("Буфер Обмена за "+date);
        this.folder.mkdir();
        textFile = new File(this.folder,"clipboard_log.txt");
        textWriter = new FileWriter(textFile);
        sdf = new SimpleDateFormat("HH:mm:ss");
        previousClipboardContent = "";
        previousImage = null;
    }

    public void startLogging() throws Exception {
        while (true) {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                String clipboardContent = (String) clipboard.getData(DataFlavor.stringFlavor);
                if (!clipboardContent.equals(previousClipboardContent)) {
                    textWriter.write(sdf.format(new Date()) + ": " + clipboardContent);
                    textWriter.write("\n");
                    textWriter.flush();
                    previousClipboardContent = clipboardContent;
                }
            }
            if (clipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
                Image image = (Image) clipboard.getData(DataFlavor.imageFlavor);
                BufferedImage currentImage = toBufferedImage(image);
                if(previousImage == null || !compareImages(previousImage, currentImage)) {
                    //save image
                    File outputfile = new File(folder,"image_log_" + sdf.format(new Date()) + ".png");
                    ImageIO.write(currentImage, "png", outputfile);
                    previousImage = currentImage;
                }
            }
            Thread.sleep(1000);
        }
    }
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }
    public static boolean compareImages(BufferedImage imgA, BufferedImage imgB) {
        // The images must be the same size.
        if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight()) {
            return false;
        }
        int width = imgA.getWidth();
        int height = imgA.getHeight();
        // Loop over every pixel.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Compare the pixels for equality.
                if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }
}