package PacManGame.Entities.Tiles.Factory;

import PacManGame.Entities.Tiles.Tile;
import PacManGame.Game;
import java.awt.*;

/**
 * Represent interface for factory method pattern, which productstile
 */
public interface ITileFactory {
    /**
     * Creates a tile entity.
     * @param location location, where tile is located
     * @param size size of sprite
     * @param game game
     * @return
     */
    Tile ConstructTile(Point location, int size, Game game);
}
