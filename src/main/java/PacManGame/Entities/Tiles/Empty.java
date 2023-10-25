package PacManGame.Entities.Tiles;
import PacManGame.Game;
import java.awt.*;

/**
 * Object representing Empty tile, it's not override "canEnter", which means
 * every movement entity can move over this tile.
 */
public class Empty extends Tile{
    public Empty(Point location, int size, String spriteRes, Game game) {
        super(location, size,spriteRes,game);
    }

}
