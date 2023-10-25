package PacManGame.Entities.Tiles.Factory;

import PacManGame.Entities.Tiles.Empty;
import PacManGame.Entities.Tiles.Tile;
import PacManGame.Game;
import java.awt.*;

/**
 * A concrete implementation of factory method.
 */
public class EmptyTileFactory implements ITileFactory{
    private static final String EMPTY_TILE_PNG = "/Tiles/EmptyTile.png";
    @Override
    public Tile ConstructTile(Point location, int size, Game game) {
        return new Empty(location,size, EMPTY_TILE_PNG,game);
    }
}
