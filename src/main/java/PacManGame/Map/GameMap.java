package PacManGame.Map;

import PacManGame.Entities.Tiles.Door.IHasKey;
import PacManGame.Entities.Tiles.Door.Key;
import PacManGame.Entities.Tiles.Factory.*;
import PacManGame.Entities.Tiles.Tile;
import PacManGame.Game;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Represent whole game world.
 * Provides all information about game world such as starting points od ghost and pacman,
 * how many Pac-Dot and Power-Pellet must be eaten to clear level or to spawn fruit.
 * It stores all game tiles in two dim array.
 */
public class GameMap {
    private final Game game;
    // world map data
    private int width;
    private int height;
    private Tile[][] tiles;
    private final int tileSize;
    // key from cage door
    private Key doorKey;
    // starting grid points
    private Point pacManStartingGrid;
    private Point blinkyStartingGrid;
    private Point inkyStartingGrid;
    private Point pinkyStartingGrid;
    private Point clydeStartingGrid;
    private Point fruitGrid;
    // if already has been loaded game map
    private boolean isLoaded = false;
    // how many coins are needed to clear level
    private int coinsCount = 0;
    // how many coins are needed to spawn fruit
    private int neededCoinForFruitSpawn = 0;
    // stores all tile factories
    private static final Map<Character,ITileFactory> tilesFactories = new HashMap<>();
    static {
        tilesFactories.put('X',new WallTileFactory());
        tilesFactories.put('.',new EmptyTileFactory());
        tilesFactories.put('o',new PacDotTileFactory());
        tilesFactories.put('O',new PowerPelletTileFactory());
        tilesFactories.put('D',new DoorTileFactory());
    }

    public GameMap(int tileSize, Game game){
        this.game = game;
        this.tileSize = tileSize;
        this.doorKey = null;
    }

    /**
     * Reads provided file and construct game map from it.
     * If success, sets attribute representing, if is map loaded, to be true.
     * @param gameMapData file, which contains game map data
     * @throws GameMapException throws if game map file was not found or data is corrupted.
     */
    public void loadGameMap(File gameMapData) throws GameMapException {
        isLoaded = false;
        coinsCount = 0;

        try( var fileReader = new Scanner((gameMapData))){
            var mapSizes = fileReader.nextLine().split(" ");
            width = Integer.parseInt(mapSizes[0]);
            height = Integer.parseInt(mapSizes[1]);
            tiles = new Tile[height][width];

            for (int y = 0;y < height;y++){
                var row = fileReader.nextLine();
                for (int x = 0;x < row.length();x++){
                    char ch = row.charAt(x);
                    if(tilesFactories.containsKey(ch)){
                        tiles[y][x] = tilesFactories.get(row.charAt(x)).ConstructTile(new Point(x*tileSize,y*tileSize),tileSize,game);
                    }else{
                        switch (ch) {
                            case 'M' -> pacManStartingGrid = new Point(x, y);
                            case 'B' -> blinkyStartingGrid = new Point(x, y);
                            case 'I' -> inkyStartingGrid = new Point(x, y);
                            case 'P' -> pinkyStartingGrid = new Point(x, y);
                            case 'C' -> clydeStartingGrid = new Point(x, y);
                            case 'F' -> fruitGrid = new Point(x, y);
                        }
                        tiles[y][x] = tilesFactories.get('.').ConstructTile(new Point(x*tileSize,y*tileSize),tileSize,game);
                    }
                }
            }

        } catch (Exception e) {
            throw new GameMapException(e.getMessage());
        }
        isLoaded = true;
    }

    /**
     * Obtain value of how many coins have to PacMan eats to clear level.
     * @return coins count
     */
    public int getRemainingCoinsCount(){
        return coinsCount;
    }

    /**
     * Replace tile for the new tile.
     * @param grid point of tile on the game map
     * @param newTile new tile , which will replace the old one
     */
    private void changeTile(Point grid,Tile newTile){
        if (existsTile(grid)){
            tiles[grid.y][grid.x] = newTile;
        }
    }

    /**
     * Sets how many coins have to PacMan eats, just fruit to be spawn.
     * @param neededCoinForFruitSpawn value of coins
     */
    public void setNeededCoinForFruitSpawn(int neededCoinForFruitSpawn) {
        this.neededCoinForFruitSpawn = neededCoinForFruitSpawn;
    }

    /**
     * Indicates if PacMan already has eaten enough coins, so fruit can be spawn.
     * @return true if It's time to spawn fruit object in the game
     */
    public boolean wasEatenAlreadyEnoughCoinsForFruitToBeSpawn(){
        return neededCoinForFruitSpawn == 0;
    }

    /**
     * Try check if in provided tile is coin tile and when is that's true, it collects coin and replace coin tile for
     * empty tile.
     * @param grid grid point of location where is PacMan if he is in the center of tile
     */
    public void tryTakeCoin(Point grid){
        if (!existsTile(grid)){return;}
        var tile = tiles[grid.y][grid.x];
        if (tile.coinTileProcess()){
            coinsCount--;
            if (neededCoinForFruitSpawn !=0){
                if (!game.isFruitSpawn()){
                    neededCoinForFruitSpawn--;
                }
            }
            changeTile(grid,tilesFactories.get('.').ConstructTile(new Point(grid.x*tileSize,grid.y*tileSize),tileSize,game));
        }
    }
    public int getWidth(){
        return width*tileSize;
    }
    public int getHeight(){
        return height*tileSize;
    }
    public Point GetPacManStartingLocation(){
        return new Point(pacManStartingGrid.x * tileSize, pacManStartingGrid.y * tileSize);
    }

    public Point getBlinkyStartingPoint() {
        return new Point(blinkyStartingGrid.x * tileSize, blinkyStartingGrid.y * tileSize);
    }

    public Point getInkyStartingPoint() {
        return new Point(inkyStartingGrid.x * tileSize, inkyStartingGrid.y * tileSize);
    }

    public Point getClydeStartingPoint() {
        return new Point(clydeStartingGrid.x * tileSize, clydeStartingGrid.y * tileSize);
    }

    public Point getPinkyStartingPoint() {
        return new Point(pinkyStartingGrid.x * tileSize, pinkyStartingGrid.y * tileSize);
    }

    public Point getFruitPoint() {
        return new Point(fruitGrid.x*tileSize, fruitGrid.y*tileSize);
    }

    /**
     * During creating game map , if is constructed Pac-Dot or Power-Pellet its factories invoke this method to get knows
     * game map about new coin tile , so game map then knows how many coins must eat PacMan to clear level floor.
     */
    public void increaseCoinsCount(){
        coinsCount++;
    }

    /**
     * Obtain key from cage door.
     * @return key from lock
     */
    public Key getDoorKey(){
        return this.doorKey;
    }

    /**
     * Indicates if mas is in state of use.
     * @return true if map was usefully loaded
     */
    public boolean isLoaded() {
        return isLoaded;
    }

    /**
     * Sets key from door lock, later that keys from this method obtain ghosts, so they can get out from cage.
     * @param doorKey key from cage door
     */
    public void setDoorKey(Key doorKey) {
        this.doorKey = doorKey;
    }

    /**
     * Check method , if exist tile at this provided grid coordinates.
     * @param grid grid point of tile on the map
     * @return true if tile exists
     */
    public boolean existsTile(Point grid){
        return grid.x >= 0 && grid.x < width && grid.y >= 0 && grid.y < height;
    }

    /**
     * Obtain info if a certain tile is transitional for provided entity.
     * @param grid grid point of tile on the map
     * @param entityWithKey entity which May has key
     * @return true if entity can move over that tile
     */
    public boolean isTileTransitional(Point grid, IHasKey entityWithKey){
        return tiles[grid.y][grid.x].canEnter(entityWithKey);
    }

    /**
     * Draws whole game map.
     * @param g2 2D graphics, into which its drawn
     * @param topShift shift from top
     */
    public void Draw(Graphics2D g2,int topShift){
        for (int y = 0;y< height;y++){
            for (int x=0;x< width ;x++){
                tiles[y][x].draw(g2,topShift);
            }
        }
    }
}
