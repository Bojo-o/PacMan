package PacManGame.Entities.Tiles.Factory;

import PacManGame.Entities.Tiles.PacDot;
import PacManGame.Entities.Tiles.Tile;
import PacManGame.Game;

import java.awt.*;
/**
 * A concrete implementation of factory method.
 * Also, during constructing it notify game map that a new coin tile is created, that map knows
 * how many coins tiles exist and how many must PacMan collects to clean level.
 */
public class PacDotTileFactory implements  ITileFactory{
    private static final String PAC_DOC_TILE_IMAGE = "/Entities/Objects/PacDoc.png";
    @Override
    public Tile ConstructTile(Point location, int size, Game game) {
        game.getGameMap().increaseCoinsCount();
        return new PacDot(location,size, PAC_DOC_TILE_IMAGE,game);
    }
}
