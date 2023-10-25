package PacManGame.Entities;

import PacManGame.PacManGame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Object represent entity.
 * Entity has location, size and sprite image.
 * It provides method to draw this entity to graphics.
 */
public abstract class Entity {
    protected Point location;
    protected BufferedImage sprite;
    protected int size;

    /**
     * Creates entity and sets attributes.
     * @param location point of where entity should locate
     * @param size size of image / sprite , which is drawn
     * @param spriteRes path to image, which is stores in resource
     */
    public Entity(Point location,int size,String spriteRes){
        this.location = location;
        this.size = size;
        sprite = loadEntitySprite(spriteRes);
    }

    /**
     * Obtain distance from one entity to the seconds.
     * @param a entity a
     * @param b entity b
     * @return distance between provided entities
     */
    public static int getDistance(Entity a, Entity b){
        int xDistance = Math.abs(a.location.x - b.location.x);
        int yDistance = Math.abs(a.location.y - b.location.y);
        return (int)Math.sqrt(xDistance*xDistance+yDistance*yDistance);
    }

    /**
     * Load sprite image.
     * @param spriteRes path to image,which is stores in resource.
     * @return BufferedImage or null, if image can not be loaded.
     */
    protected BufferedImage loadEntitySprite(String spriteRes){
        try {
            return ImageIO.read(Objects.requireNonNull(Entity.class.getResource(spriteRes)));
        } catch (IOException e) {
            System.out.println("Can not load sprite : " + spriteRes);

        }catch (NullPointerException e){
            System.out.println("Can not find sprite : " + spriteRes);

        }
        return null;
    }

    public Point getLocation() {
        return location;
    }

    /**
     * Draw entity into graphics.
     * @param g2 2D graphics
     * @param topShift shift from top
     */
    public void draw(Graphics2D g2, int topShift){
        g2.drawImage(sprite,location.x, location.y + topShift, size,size,null);
    }
}
