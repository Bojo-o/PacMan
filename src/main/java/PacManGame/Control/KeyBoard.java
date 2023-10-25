package PacManGame.Control;

import PacManGame.Entities.MovementDirection;
import PacManGame.Entities.PacMan;
import PacManGame.Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Is used for keyboard inputs.
 */
public class KeyBoard implements KeyListener {
    private PacMan pacMan;
    private Game game;

    public void setPacMan(PacMan pacMan){
        this.pacMan = pacMan;
    }
    public void setgame(Game game){
        this.game = game;
    }
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Calls when user pressed some key on keyboard.
     * Sets PacMan next direction of movement,if certain keys are pressed.
     * @param e keyboard event, provides which key was pressed.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // if pacman has not been set yet, it does nothing
        if (pacMan == null){
            return;
        }

        switch (e.getKeyCode()){
            case KeyEvent.VK_W -> pacMan.setNextDirection(MovementDirection.UP);
            case KeyEvent.VK_S -> pacMan.setNextDirection(MovementDirection.DOWN);
            case KeyEvent.VK_A -> pacMan.setNextDirection(MovementDirection.LEFT);
            case KeyEvent.VK_D -> pacMan.setNextDirection(MovementDirection.RIGHT);
            case KeyEvent.VK_ENTER -> game.StartNewGame();
            default -> {}
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
