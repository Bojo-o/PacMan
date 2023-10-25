package PacManGame.Entities.DirectionChoosers;

import PacManGame.Entities.MovableEntity;
import PacManGame.Entities.MovementDirection;
import PacManGame.Entities.PacMan;
import java.util.List;

/**
 * A concrete implementation, which represent chooser for PacMan.
 */
public class PacManDirectionChooser implements IDirectionChooser{
    /**
     * Look at which direction is sets to PacMan should move and if its possible it changes direction, if not direction remains.
     * @param possibleDirections list of movements direction, from which it chooses
     * @param entity movement entity,for which it chooses direction
     * @return a new chosen movement direction
     */
    @Override
    public MovementDirection chooseDirection(List<MovementDirection> possibleDirections, MovableEntity entity) {
        var pacMan = (PacMan)entity;
        if (possibleDirections.contains(pacMan.getNextDirection())){
            return pacMan.getNextDirection();
        }
        return pacMan.getCurrDirection();
    }
}
