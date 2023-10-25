package PacManGame.Entities.DirectionChoosers;

import PacManGame.Entities.MovableEntity;
import PacManGame.Entities.MovementDirection;
import java.util.List;

public interface IDirectionChooser {
    /**
     * Chooses a new movement direction.
     * @param possibleDirections list of movements direction, from which it chooses
     * @param entity movement entity,for which it chooses direction
     * @return a new chosen movement direction
     */
    MovementDirection chooseDirection(List<MovementDirection> possibleDirections, MovableEntity entity);
}
