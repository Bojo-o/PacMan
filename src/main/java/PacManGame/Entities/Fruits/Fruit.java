package PacManGame.Entities.Fruits;

import PacManGame.Entities.Entity;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Object representing "Fruit" entity in PacMan game.
 */
public class Fruit extends Entity {
    private boolean isVisible = false;
    private final Map<Fruits,BufferedImage> sprites = new HashMap<>();
    private final Map<Fruits,Integer> points = new HashMap<>();
    private final List<Fruits> registerFruits = new ArrayList<>();

    public Fruit(Point location, int size, String spriteRes) {
        super(location, size, spriteRes);
    }

    public boolean isVisible(){
        return isVisible;
    }

    public void setVisibility(boolean visibility){
        isVisible = visibility;
    }

    /**
     * Stores a new type of fruit.
     * @param fruit type of fruit
     * @param spriteRes path to sprite, which is stored in resource
     * @param points points value, which player obtain after eating fruit
     */
    public void registerNewFruit(Fruits fruit,String spriteRes,int points){
        sprites.put(fruit,loadEntitySprite(spriteRes));
        this.points.put(fruit,points);
        registerFruits.add(fruit);
    }

    /**
     * Obtain point value, which is the last registered
     * @return points for fruits
     */
    public int getPoints(){
        return points.get(registerFruits.get(registerFruits.size()-1));
    }

    /**
     * If fruit is visible,which is the last registered, its draws it.
     * @param g2 2D graphics, it will be drawn into it
     * @param topShift shift from top
     */
    @Override
    public void draw(Graphics2D g2, int topShift) {
        if (isVisible){
            if (!registerFruits.isEmpty()){
                g2.drawImage(sprites.get(registerFruits.get(registerFruits.size()-1)),location.x, location.y + topShift, size,size,null);
            }
        }
    }

    /**
     * Draws fruit info, all registered fruits.
     * @param g2 2D graphics, it will be drawn into it
     * @param startingLocation point of coordinates where it begins to draw
     */
    public void drawFruitInfo(Graphics2D g2,Point startingLocation){
        var location = new Point(startingLocation.x,startingLocation.y);
        for (Fruits registerFruit : registerFruits) {
            g2.drawImage(sprites.get(registerFruit), location.x, location.y, size, size, null);
            location.x -= size;
        }
    }
}
