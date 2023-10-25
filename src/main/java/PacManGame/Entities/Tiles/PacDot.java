package PacManGame.Entities.Tiles;

import PacManGame.Game;
import java.awt.*;

/**
 * Object representing Pac-Doc tile, it's not override "canEnter", which means
 * every movement entity can move over this tile.
 */
public class PacDot extends Tile{
    // For PacDoc players gets 10 points
    private static final int POINTS_FOR_OBTAINING = 10;

    public PacDot(Point location, int size, String spriteRes, Game game) {
        super(location, size,spriteRes,game);
    }

    /**
     * Add points to the score.
     * @return true, because this is tile which represents coin
     */
    @Override
    public boolean coinTileProcess() {
        game.incScore(POINTS_FOR_OBTAINING);
        return true;
    }
}
