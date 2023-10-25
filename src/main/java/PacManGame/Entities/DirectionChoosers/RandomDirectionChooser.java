package PacManGame.Entities.DirectionChoosers;

import PacManGame.Entities.Ghosts.Ghost;
import PacManGame.Entities.MovableEntity;
import PacManGame.Entities.MovementDirection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A concrete implementation, which represent chooser for Ghost.
 * it works on the basis of random selection.
 */
public class RandomDirectionChooser implements IDirectionChooser{
    private final Random rand = new Random();
    // helps find out opposite direction, used when selecting possible direction
    private final static Map<MovementDirection,MovementDirection> oppositesDirection;
    static {
        oppositesDirection = new HashMap<>();
        oppositesDirection.put(MovementDirection.UP,MovementDirection.DOWN);
        oppositesDirection.put(MovementDirection.DOWN,MovementDirection.UP);
        oppositesDirection.put(MovementDirection.RIGHT,MovementDirection.LEFT);
        oppositesDirection.put(MovementDirection.LEFT,MovementDirection.RIGHT);
    }

    /**
     * Random chooses movement direction for ghost.
     * @param possibleDirections list of movements direction, from which it chooses
     * @param entity movement entity,for which it chooses direction
     * @return a new chosen movement direction
     */
    @Override
    public MovementDirection chooseDirection(List<MovementDirection> possibleDirections, MovableEntity entity) {
        if (possibleDirections.size() == 1){
            return possibleDirections.get(0);
        }
        // removes possibility to chooses direction, from which ghost arrived
        var oppositeDirection = oppositesDirection.get(entity.getCurrDirection());
        possibleDirections.remove(oppositeDirection);

        // if ghost is in the cage and obtains permission to leave it choose direction to get out
        Ghost ghost = (Ghost) entity;
        if (ghost.isInCage()){
            if (possibleDirections.contains(MovementDirection.UP)){
                ghost.getOutFromCage();
                return MovementDirection.UP;
            }
        }

        return possibleDirections.get(rand.nextInt(possibleDirections.size()));
    }
}
