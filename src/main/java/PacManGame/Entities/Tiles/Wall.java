package PacManGame.Entities.Tiles;

import PacManGame.Entities.Tiles.Door.IHasKey;
import PacManGame.Game;
import java.awt.*;

/**
 * Object representing wall, not transitional tile.
 */
public class Wall extends Tile{
    public Wall(Point location, int size, String spriteRes, Game game) {
        super(location, size,spriteRes,game);
    }

    /**
     * Nothing can not move over wall.
     * @param entityWithKey entity which May has key
     * @return always false
     */
    @Override
    public boolean canEnter(IHasKey entityWithKey) {
        return  false;
    }
}
