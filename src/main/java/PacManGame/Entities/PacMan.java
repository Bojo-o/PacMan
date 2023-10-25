package PacManGame.Entities;

import PacManGame.Entities.Animation.Animation;
import PacManGame.Entities.Animation.OrientatedGif;
import PacManGame.Entities.DirectionChoosers.PacManDirectionChooser;
import PacManGame.Map.GameMap;
import java.awt.*;

/**
 * Object representing PacMan.
 */
public class PacMan extends MovableEntity{
    private final OrientatedGif orientedGifs;
    private final Animation animation;
    // default next direction is Right
    private MovementDirection nextDirection = MovementDirection.RIGHT;
    private int lives;

    /**
     * Create PacMan movement entity, sets his lives to 3.
     * Sets animation to be symmetric.
     * @param location point where PacMan will be spawned, it is also starting point, when ghost will kill PacMan or
     *                 PacMan clear level
     * @param size size of sprite, how big will be drawn frames
     * @param spriteRes path to default image, which is stores in resource
     * @param gameMap game map
     * @param orientedGifs gifs for all movement direction, to animated it when pacMan moves that direction
     */
    public PacMan(Point location, int size, String spriteRes, GameMap gameMap, OrientatedGif orientedGifs) {
        super(location, size,spriteRes,gameMap,4,new PacManDirectionChooser());
        this.lives = 3;
        this.startingPoint = new Point(location.x,location.y);
        this.orientedGifs = orientedGifs;
        animation = new Animation(orientedGifs.getGif(MovementDirection.RIGHT),size,300);
        animation.setSymmetry(true);
        animation.startAnimation();
    }

    /**
     * PacMan gets one more live.
     * Its invoke when player gets a certain points of score.
     * Concrete 10 000.
     */
    public void getLive(){
        lives++;
    }

    /**
     * PacMan loss one live.
     * Its invoke when ghost kills PacMan.
     */
    public void loseLive(){
        lives--;
    }

    /**
     * Sets next direction, which is invoked from keyboard inputs.
     * @param direction a new direction of movement
     */
    public void setNextDirection(MovementDirection direction){
        nextDirection = direction;
    }
    /**
     * Methods which is called when Pacman change direction, so it can change animation image.
     */
    @Override
    public void notifyDirectionChanges() {
        animation.changeGif(orientedGifs.getGif(getCurrDirection()));
    }

    public MovementDirection getNextDirection(){
        return nextDirection;
    }

    /**
     * Process PacMan collisions with coins tiles.
     */
    public void coinCollisions(){
        if (!inTileMove){
            gameMap.tryTakeCoin(getCurrentGridLocation());
        }
    }

    /**
     * Obtain info if PacMan has still some live.
     * @return true if Pacman has still live
     */
    public boolean hasLives(){
        return lives > 0;
    }
    /**
     * Draws fruit info, all registered fruits.
     * @param g2 2D graphics, it will be drawn into it
     * @param startingLocation point of coordinates where it begins to draw
     */
    public void drawLivesInfo(Graphics2D g2, Point startingLocation){
        var location = new Point(startingLocation.x,startingLocation.y);
        for (int i = 1;i < lives;i++){
            g2.drawImage(sprite,location.x,location.y,size,size,null);
            location.x+= 40;
        }
    }
    @Override
    public void draw(Graphics2D g2,int topShift) {
        animation.draw(g2,new Point(location.x, location.y + topShift));
    }
}
