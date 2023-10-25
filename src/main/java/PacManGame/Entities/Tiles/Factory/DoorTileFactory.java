package PacManGame.Entities.Tiles.Factory;

import PacManGame.Entities.Tiles.Door.Door;
import PacManGame.Entities.Tiles.Tile;
import PacManGame.Game;
import java.awt.*;

/**
 * A concrete implementation of factory method.
 * Also sets key to game map from door.
 */
public class DoorTileFactory implements ITileFactory{
    private static final String DOOR_TILE_PNG = "/Tiles/Door.png";

    @Override
    public Tile ConstructTile(Point location, int size, Game game) {
        var door = new Door(location,size, DOOR_TILE_PNG,game);
        game.getGameMap().setDoorKey(door.getLockKey());
        return door;
    }
}
