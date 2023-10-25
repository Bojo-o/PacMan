package PacManGame.Entities;

import PacManGame.Entities.DirectionChoosers.IDirectionChooser;
import PacManGame.Entities.Tiles.Door.IHasKey;
import PacManGame.Entities.Tiles.Door.Key;
import PacManGame.Map.GameMap;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Object represent entity , which is able to move on the map.
 * Extend from entity and add to it mechanism for moving.
 */
public abstract class MovableEntity extends Entity implements IHasKey {
    // point where entity the first located
    protected Point startingPoint;
    // game map , so entity can look at the map, it then can move around
    protected GameMap gameMap;
    // speed of entity
    protected int movementSpeed;
    // key, it is used when game tile has lock and movable entity must have key from it fo passing over that tile
    protected Key key;
    // direction chooser, it chooses direction if entity comes to the signpost
    private final IDirectionChooser directionChooser;
    // direction of moving , default is Right
    protected MovementDirection currDirection = MovementDirection.RIGHT;
    // used in moving mechanism, indicates if entity moves one tile distance
    protected boolean inTileMove = false;
    // used in moving mechanism, how many pixels must entity to pass to move one tile distance
    private int remainingPxlToMove = 0;
    // used in moving mechanism, indicates if entity comes to teleport zone
    private boolean inTeleportZone = false;
    // stores directions
    private static final Map<MovementDirection,Point> directionVectors = new HashMap<>();
    static{
        directionVectors.put(MovementDirection.UP,new Point(0,-1));
        directionVectors.put(MovementDirection.DOWN,new Point(0,1));
        directionVectors.put(MovementDirection.LEFT,new Point(-1,0));
        directionVectors.put(MovementDirection.RIGHT,new Point(1,0));
    }

    /**
     * Sets movement entity attributes.
     * @param location point of where entity should locate
     * @param size size of sprite, how big will be drawn frames
     * @param spriteRes path to image, which is stores in resource
     * @param gameMap game map
     * @param movementSpeed speed of entity
     * @param directionChooser chooser of direction
     */
    public MovableEntity(Point location, int size, String spriteRes, GameMap gameMap, int movementSpeed,IDirectionChooser directionChooser) {
        super(location, size,spriteRes);
        this.startingPoint = new Point(location.x, location.y);
        this.gameMap = gameMap;
        this.movementSpeed = movementSpeed;
        this.directionChooser = directionChooser;
        this.key = null;
    }
    /*
    public void setStartingPoint(Point newPoint){
        this.startingPoint = newPoint;
    }
     */

    /**
     * Sets location to starting point,direction to default and resets attributes for moving mechanism.
     */
    public void reset(){
        location = new Point(startingPoint.x, startingPoint.y);
        currDirection = MovementDirection.RIGHT;
        inTileMove = false;
        remainingPxlToMove = 0;
        inTeleportZone = false;
    }

    /**
     * Obtain grid location od entity , for access to map
     * @return grid of location
     */
    protected Point getCurrentGridLocation(){
        return new Point(location.x / size, location.y/ size);
    }

    /**
     * Moves entity in the current direction.
     * @param pxlToMove how many pixels' entity shift
     */
    private void shift(int pxlToMove){
        location.x += pxlToMove * directionVectors.get(currDirection).x;
        location.y += pxlToMove* directionVectors.get(currDirection).y;
    }
    public MovementDirection getCurrDirection(){
        return currDirection;
    }

    /**
     * Obtain all currently possible direction.
     * @return list of all currently possible direction, which entity is able to move
     */
    private List<MovementDirection> getPossibleDirections(){
        List<MovementDirection> possibleDirections = new ArrayList<>();
        var currGrid = getCurrentGridLocation();
        for (Map.Entry<MovementDirection,Point> entry : directionVectors.entrySet()){
            var direction = entry.getKey();
            var nextGrid = new Point(currGrid.x + directionVectors.get(direction).x,currGrid.y + directionVectors.get(direction).y);
            if (gameMap.existsTile(nextGrid)){
                if (gameMap.isTileTransitional(nextGrid,this)){
                    possibleDirections.add(direction);
                }
            }
        }
        return possibleDirections;
    }

    /**
     * Invokes ,when entity changes direction , it used for animation.
     */
    public abstract void notifyDirectionChanges();

    /**
     * Process direction changes.
     * From all possible directions the chooser select ones
     */
    private void tryChangeDirection(){
        var possibleDirections = getPossibleDirections();
        if (!possibleDirections.isEmpty()){
            currDirection = directionChooser.chooseDirection(possibleDirections,this);
            notifyDirectionChanges();
        }
    }
    @Override
    public Key getKey() {
        return key;
    }

    @Override
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * Moves entity.
     * Its entity move mechanism, it works on the basic "tile shift", which means
     * entity is as well as tile, it looks at the game map and from it obtains all possible directions.
     * From that direction chooser chooses one and then starts to move that direction exactly one tile size distance,
     * so again will be entity as well as tile and a whole process repeats.
     *
     * If ii reads from map that entity is in teleport zone, it moves to the teleport and then teleport entity on
     * the other side of game map, in this process it can not change movement direction.
     */
    public void Move() {
        if (inTileMove) {
            // process move to across one tile distance
            if (remainingPxlToMove - movementSpeed > 0) {
                remainingPxlToMove -= movementSpeed;
                shift(movementSpeed);
            } else {
                inTileMove = false;
                shift(remainingPxlToMove);
            }
        } else {
            // process teleport zone
            if (inTeleportZone) {
                if (currDirection == MovementDirection.LEFT) {
                    location.x = gameMap.getWidth();
                } else {
                    location.x = -size;
                }
                inTeleportZone = false;
                return;
            }
            // find a new direction and moves in that direction
            tryChangeDirection();
            var currGrid = getCurrentGridLocation();
            var nextGrid = new Point(currGrid.x + directionVectors.get(currDirection).x, currGrid.y + directionVectors.get(currDirection).y);

            if (gameMap.existsTile(nextGrid)){
                if (gameMap.isTileTransitional(nextGrid,this)){
                    remainingPxlToMove = size - movementSpeed;
                    shift(movementSpeed);
                    inTileMove = true;
                }
            }else{
                // indicates that entity is in the teleport zone
                remainingPxlToMove = size - movementSpeed;
                shift(movementSpeed);
                inTileMove = true;
                inTeleportZone=true;
            }
        }
    }
}
