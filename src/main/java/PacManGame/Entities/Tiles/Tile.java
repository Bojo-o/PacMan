package PacManGame.Entities.Tiles;

import PacManGame.Entities.Entity;
import PacManGame.Entities.Tiles.Door.IHasKey;
import PacManGame.Entities.Tiles.Door.IHasLock;
import PacManGame.Entities.Tiles.Door.Key;
import PacManGame.Game;
import java.awt.*;

/**
 * Object representing tile in the game world.
 * Implements interface IHasLock, so it provides methods to sets lock keys,
 * get key and mechanism for allowing movement entity moves over tile.
 */
public abstract class Tile extends Entity implements IHasLock {
    protected Game game;
    protected Key key;

    /**
     * @param location point of where tile should locate
     * @param size size of image / sprite , which is drawn
     * @param spriteRes path to image , which is stores in resource
     * @param game game
     */
    public Tile(Point location, int size,String spriteRes,Game game) {
        super(location, size,spriteRes);
        this.game = game;
        this.key = null;
    }

    /**
     * Method for tiles, which represents Pac_Dot or Power Pellet.
     * Default is sets as false, it means that tile is not Pac_Dot or Power Pellet, it must be overridden.
     * @return true
     */
    public boolean coinTileProcess(){
        return false;
    }

    /**
     * Method for obtaining info if a certain movement entity can move over it.
     * Default is sets as true, it means that tile has not locks, it must be implemented.
     * @param entityWithKey entity which May has key
     * @return default is true
     */
    @Override
    public boolean canEnter(IHasKey entityWithKey) {
        return true;
    }

    @Override
    public Key getLockKey() {
        return key;
    }

}
