package PacManGame.Entities.Ghosts;

import PacManGame.Entities.Animation.Animation;
import PacManGame.Entities.Animation.Gif;
import PacManGame.Entities.Animation.OrientatedGif;
import PacManGame.Entities.DirectionChoosers.IDirectionChooser;
import PacManGame.Entities.MovableEntity;
import PacManGame.Entities.MovementDirection;
import PacManGame.Map.GameMap;

import javax.swing.*;
import java.awt.*;

/**
 * Object representing PacMan enemy "Ghost".
 * Provides methods for Ghost to enter a certain state.
 * STATES:
 * NORMAL -> ghost can kill PacMan if catches him
 * BLUE MODE -> pacMan eats ghost if catches him
 * BLUE MODE ENDING -> same as BLUE MODE, but it exists for showing player that BLUE MODE is coming to end
 */
public class Ghost extends MovableEntity {
    // Constants
    private static final int NORMAL_SPEED = 5;
    private static final int BLUE_MODE_SPEED = 3;
    private final static int DURATION_OF_BLUE_MODE = 5000;
    private final static int DURATION_OF_ENDING_BLUE_MODE = 2000;
    // GIFs and Animation
    private final OrientatedGif orientedGifs;
    private final Gif blueMode;
    private final Gif blueModeEnding;
    private final Animation animation;
    // for cage mechanism, represent if Ghost get out of the cage
    private boolean isInCage = true;
    // timers for setting ghost states
    private GhostStates stateOfGhost = GhostStates.NORMAL;
    private Timer blueModeDuration;
    private Timer blueModeEndingDuration;

    /**
     *
     * @param location point where ghosts will be spawned, it is also starting point, when ghost will be eaten
     * @param size size of sprite, how big will be drawn frames
     * @param spriteRes path to default image, which is stores in resource
     * @param gameMap game map
     * @param orientedGifs gifs for all movement direction, to animated it when ghost moves that direction
     * @param directionChooser chooser of direction
     * @param blueMode gif of ghost, when it is in blue mode
     * @param blueModeEnding gif of ghost, when is in blue mode ending
     */
    public Ghost(Point location, int size, String spriteRes, GameMap gameMap, OrientatedGif orientedGifs, IDirectionChooser directionChooser, Gif blueMode, Gif blueModeEnding) {
        super(location, size, spriteRes, gameMap, NORMAL_SPEED, directionChooser);
        this.startingPoint = new Point(location.x,location.y);
        this.orientedGifs = orientedGifs;
        this.blueMode = blueMode;
        this.blueModeEnding = blueModeEnding;
        // create and starts animation process
        animation = new Animation(orientedGifs.getGif(MovementDirection.RIGHT),size,400);
        animation.startAnimation();
        // sets up timers
        blueModeDuration = new Timer(DURATION_OF_BLUE_MODE,e -> enterBlueModeEnding());
        blueModeDuration.setRepeats(false);
        blueModeEndingDuration = new Timer(DURATION_OF_ENDING_BLUE_MODE,e -> enterNormalMode());
        blueModeEndingDuration.setRepeats(false);
    }

    public void getOutFromCage(){
        isInCage =false;
    }
    public boolean isInCage() {
        return isInCage;
    }

    /**
     * Sets ghost image to show that it is in normal state,sets ghost speed to normal.
     */
    private void enterNormalMode(){
        stateOfGhost = GhostStates.NORMAL;
        animation.changeGif(orientedGifs.getGif(getCurrDirection()));
        movementSpeed = NORMAL_SPEED;
    }
    /**
     * Sets ghost image to show that it is in blue mode ,sets ghost speed to slower one.
     * Start timer for entering later to blue mode ending state.
     */
    public void enterBlueMode(){
        stateOfGhost = GhostStates.BLUE_MODE;
        movementSpeed = BLUE_MODE_SPEED;
        animation.changeGif(blueMode);
        blueModeDuration.restart();
        blueModeEndingDuration.stop();
    }
    /**
     * Sets ghost image to show that it is in blue mode ending.
     * Start timer for entering later to normal state.
     */
    public void enterBlueModeEnding(){
        stateOfGhost = GhostStates.BLUE_MODE_ENDING;
        animation.changeGif(blueModeEnding);
        blueModeEndingDuration.restart();
    }
    public GhostStates getStateOfGhost(){
        return stateOfGhost;
    }

    /**
     * Reset ghost, sets location of ghost to his starting point,
     * sets that he is in cage and sets normal state of ghost.
     */
    @Override
    public void reset() {
        super.reset();
        if (stateOfGhost!= GhostStates.NORMAL){
            enterNormalMode();
            blueModeDuration.stop();
            blueModeEndingDuration.stop();
        }
        isInCage = true;
    }

    /**
     * Methods which is called when ghost change direction, so it can change animation image.
     */
    @Override
    public void notifyDirectionChanges() {
        if (stateOfGhost == GhostStates.NORMAL){
            animation.changeGif(orientedGifs.getGif(getCurrDirection()));
        }
    }

    @Override
    public void draw(Graphics2D g2, int topShift) {
        animation.draw(g2,new Point(location.x, location.y + topShift));
    }
}
