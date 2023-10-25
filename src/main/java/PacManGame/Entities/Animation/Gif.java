package PacManGame.Entities.Animation;

import PacManGame.PacManGame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Collection of buffered images / frames. It is used in animation.
 */
public class Gif {

    private final BufferedImage[] frames;
    private final int sizeOfFrame;

    /**
     * Create collection of provided images.
     * @param framesRes array of Strings, it represents path to certain sprites from resource.
     */
    public Gif(String[] framesRes){
        sizeOfFrame = framesRes.length;
        frames = new BufferedImage[sizeOfFrame];
        for (int i = 0; i< sizeOfFrame; i++){
            frames[i] = loadSprite(framesRes[i]);
        }
    }

    public int getSizeOfFrame() {
        return sizeOfFrame;
    }

    /**
     * Obtain the certain frame from collection.
     * @param index index
     * @return the certain frame or null if index is out of range.
     */
    public BufferedImage getFrame(int index) {
        if (index >= frames.length){
            return null;
        }
        return frames[index];
    }

    /**
     * Load sprite image/frame.
     * @param spriteRes path to image,which is stores in resource.
     * @return BufferedImage or null, if image can not be loaded.
     */
    private BufferedImage loadSprite(String spriteRes){
        try {
            return ImageIO.read(Objects.requireNonNull(Gif.class.getResource(spriteRes)));
        } catch (IOException e) {
            System.out.println("Can not load sprite : " + spriteRes);

        }catch (NullPointerException e){
            System.out.println("Can not find sprite : " + spriteRes);
        }
        return null;
    }
}
