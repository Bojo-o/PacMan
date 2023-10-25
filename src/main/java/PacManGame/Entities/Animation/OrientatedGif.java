package PacManGame.Entities.Animation;

import PacManGame.Entities.MovementDirection;
import java.util.HashMap;
import java.util.Map;

/**
 * Collections of GIFs for movement direction. For all registered movement
 * direction its stores for its certain GIF.
 */
public class OrientatedGif {

    private final Map<MovementDirection,Gif> gifs = new HashMap<>();

    /**
     * Creates and stores a new pair of movement direction and gif.
     * @param movementDirection movement direction, which will be registered.
     * @param gif the certain gif, which will be registered.
     */
    public void registerDirection(MovementDirection movementDirection, Gif gif){
        gifs.put(movementDirection, gif);
    }

    /**
     * Obtain the certain gif, representing movements direction, from collection.
     * @param movementDirection movement direction, which is key for gif.
     * @return gif or null if provided movement direction is not registered.
     */
    public Gif getGif(MovementDirection movementDirection){
        if (gifs.containsKey(movementDirection)){
            return gifs.get(movementDirection);
        }
        return null;
    }
}
