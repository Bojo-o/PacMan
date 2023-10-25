package PacManGame.Entities.Tiles;

import PacManGame.Entities.Animation.Animation;
import PacManGame.Entities.Animation.Gif;
import PacManGame.Game;
import java.awt.*;

/**
 * Object representing Power-Pellet tile, it's not override "canEnter", which means
 * every movement entity can move over this tile.
 */
public class PowerPellet extends Tile{
    // For PowerPellet player gets 50 points
    private static final int POINTS_FOR_OBTAINING = 50;
    // this tile has animation
    private final Animation animation;
    public PowerPellet(Point location, int size, String spriteRes, Gif gif, Game game) {
        super(location, size,spriteRes,game);
        animation = new Animation(gif,size,350);
        animation.startAnimation();
    }

    @Override
    public void draw(Graphics2D g2,int topShift) {
        animation.draw(g2,new Point(location.x, location.y + topShift));
    }
    /**
     * Add points to the score.
     * Call methods for processing event, that PacMan eats power pellet.
     * @return true, because this is tile which represents coin
     */
    @Override
    public boolean coinTileProcess() {
        game.incScore(POINTS_FOR_OBTAINING);
        game.powerPelletWasEaten();
        return true;
    }


}
