package PacManGame;

/**
 * Class which connects screen/ window with logical game.
 */
public class PacManGame {
    private final GameScreen screen;
    private final Game game;

    /**
     * Creates game, screen and add game to screen to be rendered.
     */
    public PacManGame(){
        screen = new GameScreen();
        game = new Game();
        screen.add(game);
        screen.pack();
        game.requestFocusInWindow();
    }

    /**
     * Removes from screen game and replaced it for error window, where it renders error message.
     * @param message error message
     */
    private void showError(String message){
        var error = new Error(message);
        screen.remove(game);
        screen.setTitle("Error");
        screen.add(error);
        screen.pack();
    }

    /**
     * Start PacMan game.
     */
    public void run(){
        try {
            game.Run();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }
}
