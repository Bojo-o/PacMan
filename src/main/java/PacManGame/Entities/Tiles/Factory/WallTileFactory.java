package PacManGame.Entities.Tiles.Factory;

import PacManGame.Entities.Tiles.Tile;
import PacManGame.Entities.Tiles.Wall;
import PacManGame.Game;
import java.awt.*;

/**
 * A concrete implementation of factory method.
 */
public class WallTileFactory implements ITileFactory{
    private static final String WALL_TILE_PNG = "/Tiles/WallTile.png";
    @Override
    public Tile ConstructTile(Point location, int size, Game game) {
        return new Wall(location,size, WALL_TILE_PNG,game);
    }
}
