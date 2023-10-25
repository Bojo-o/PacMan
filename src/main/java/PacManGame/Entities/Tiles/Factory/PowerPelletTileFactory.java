package PacManGame.Entities.Tiles.Factory;

import PacManGame.Entities.Animation.Gif;
import PacManGame.Entities.Tiles.PowerPellet;
import PacManGame.Entities.Tiles.Tile;
import PacManGame.Game;
import java.awt.*;

/**
 * A concrete implementation of factory method.
 * Also, during constructing it notify game map that a new coin tile is created, that map knows
 * how many coins tiles exist and how many must PacMan collects to clean level.
 */
public class PowerPelletTileFactory implements ITileFactory{
    private static final String POWER_PELLET_TILE_PNG = "/Entities/Objects/PowerPellet.png";
    private static final Gif POWER_PELLET_GIF = new Gif(new String[]{
            "/Entities/Objects/PowerPellet.png",
                "/Tiles/EmptyTile.png"});
    @Override
    public Tile ConstructTile(Point location, int size,Game game) {
        game.getGameMap().increaseCoinsCount();
        return new PowerPellet(location,size, POWER_PELLET_TILE_PNG, POWER_PELLET_GIF,game);
    }
}
