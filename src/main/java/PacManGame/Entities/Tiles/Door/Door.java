package PacManGame.Entities.Tiles.Door;

import PacManGame.Entities.Tiles.Tile;
import PacManGame.Game;
import java.awt.*;

/**
 * Object representing door, tile where entity needs has key for moves over it.
 * PacMan never gets key from it, Ghost got it once when the time is up.
 */
public class Door extends Tile {
    public Door(Point location, int size,String spriteRes, Game game) {
        super(location, size,spriteRes, game);
        key = new Key();
    }

    /**
     * Method to check if entity has unique key from lock.
     * @param entityWithKey entity which May has key
     * @return true if entity has key from lock
     */
    @Override
    public boolean canEnter(IHasKey entityWithKey) {
        return entityWithKey.getKey() == this.key;
    }
}
