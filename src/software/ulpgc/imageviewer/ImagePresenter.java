package software.ulpgc.imageviewer;

import software.ulpgc.imageviewer.ImageDisplay.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePresenter {
    private final ImageDisplay display;
    private Image image;
    private BufferedImage bufferedImage;

    public ImagePresenter(ImageDisplay display) {
        this.display = display;
        this.display.on((Shift) this::shift);
        this.display.on((Released) this::released);
    }

    private void shift(int offset) {
        display.clear();
        display.paint(image.id(), offset, bufferedImage);
        if (offset > 0)
            display.paint(image.next().id(), offset - display.getWidth(), load(image.prev().id()));
        else
            display.paint(image.prev().id(), display.getWidth() + offset, load(image.next().id()));

    }

    private void released(int offset) {
        if (Math.abs(offset) >= display.getWidth() / 2)
            image = offset > 0 ? image.prev() : image.next();
        repaint();
    }

    public void show(Image image) {
        this.image = image;
        repaint();
    }

    private void repaint() {
        this.display.clear();
        bufferedImage = load(image.id());
        this.display.paint(image.id(), 0, bufferedImage);
    }

    private BufferedImage load(String image) {
        try {
            return ImageIO.read(new File(image));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}