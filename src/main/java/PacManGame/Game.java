package PacManGame;

import PacManGame.Control.KeyBoard;
import PacManGame.Entities.Animation.Gif;
import PacManGame.Entities.Animation.OrientatedGif;
import PacManGame.Entities.DirectionChoosers.RandomDirectionChooser;
import PacManGame.Entities.Entity;
import PacManGame.Entities.Fruits.Fruit;
import PacManGame.Entities.Fruits.Fruits;
import PacManGame.Entities.Ghosts.Ghost;
import PacManGame.Entities.Ghosts.GhostStates;
import PacManGame.Entities.MovableEntity;
import PacManGame.Entities.MovementDirection;
import PacManGame.Entities.PacMan;
import PacManGame.Map.GameMap;
import PacManGame.Map.GameMapException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The entire PacMan game, provides main game loop, which updates and draws game.
 */
public class Game extends JPanel{
    // game constants
    private final int TILE_SIZE = 32;
    private final int TOP_SHIFT = TILE_SIZE * 2;
    private final int SCREEN_WIDTH = TILE_SIZE * 21;
    private final int SCREEN_HEIGHT = TILE_SIZE * 23 + TOP_SHIFT + TILE_SIZE * 2;
    private final int FPS = 60;

    private final int WAITING_TIME_WHEN_PACMAN_DIE = 1500;
    private final int WAITING_TIME_WHEN_PACMAN_EATEN_GHOST = 300;
    private final int WAITING_TIME_WHEN_PACMAN_CLEAR = 2000;
    private final int WAITING_TIME_STARTING = 1500;
    private final int DELAY_BETWEEN_GHOSTS_ENTER_TO_GAME = 5000;
    private final int FRUITS_POINTS_SHOW_TIME = 800;

    private final int NEEDED_COINS_EAT_TO_SPAWN_FRUIT = 33;

    // constants path to sprites images in resource
    private final String CHERRY = "/Entities/Fruits/Cherry.png";
    private final String STRAWBERRY = "/Entities/Fruits/Strawberry.png";
    private final String ORANGE = "/Entities/Fruits/Orange.png";
    private final String APPLE = "/Entities/Fruits/Apple.png";
    private final String MELON = "/Entities/Fruits/Melon.png";
    private final String GALAXIAN_FLAGSHIP = "/Entities/Fruits/GalaxianFlagship.png";
    private final String BELL = "/Entities/Fruits/Bell.png";
    private final String KEY  = "/Entities/Fruits/Key.png";

    private final String PACMAN_RES= "/Entities/PacMan/PacMan_Left2.png";
    private final String BLINKY_RES= "/Entities/Ghosts/Blinky/Blinky_Right_1.png";

    // game timers
    private Timer gameLoop;
    private Timer pauseTimer;
    private final Timer fruitShowPointsTimer;
    private final Timer keyProviderForGhosts;

    // game attributes
    private final OrientatedGif pacManGifs;
    private final OrientatedGif blinkyGifs;
    private final OrientatedGif inkyGifs;
    private final OrientatedGif pinkyGifs;
    private final OrientatedGif clydeGifs;
    private final Gif blueMode;
    private final Gif blueModeEnding;
    private Point fruitInfoStartingPoint;
    private Point pacManLivesInfoStartingPoint;
    private GameMap gameMap;
    private PacMan pacMan;
    private Fruit fruit;
    private final java.util.List<Ghost> ghosts = new ArrayList<>();
    private final KeyBoard keyBoard = new KeyBoard();
    private int indexOfGhostWhichGetsKey = 1;
    private int gameLevel;
    private boolean showFruitPoints = false;
    private int score;
    private int highScore = 0;
    private boolean bonusLiveAvailable = true;
    private final int scoreNeededForBonusLive = 10000;
    private int successionOfGhostsEating = 0;
    private boolean isGameStarting = true;
    private GameStates gameStates;


    /**
     * Help method to construct Ghost GIF.
     */
    private void constructGhostGif(OrientatedGif gifs,String ghostName){
        gifs.registerDirection(MovementDirection.UP,new Gif(new String[]{
                "/Entities/Ghosts/"+ghostName +"/"+ghostName+"_Up_1.png",
                "/Entities/Ghosts/"+ghostName +"/"+ghostName+"_Up_2.png",
        }));
        gifs.registerDirection(MovementDirection.DOWN,new Gif(new String[]{
                "/Entities/Ghosts/"+ghostName +"/"+ghostName+"_Down_1.png",
                "/Entities/Ghosts/"+ghostName +"/"+ghostName+"_Down_2.png",
        }));
        gifs.registerDirection(MovementDirection.RIGHT,new Gif(new String[]{
                "/Entities/Ghosts/"+ghostName +"/"+ghostName+"_Right_1.png",
                "/Entities/Ghosts/"+ghostName +"/"+ghostName+"_Right_2.png",
        }));
        gifs.registerDirection(MovementDirection.LEFT,new Gif(new String[]{
                "/Entities/Ghosts/"+ghostName +"/"+ghostName+"_Left_1.png",
                "/Entities/Ghosts/"+ghostName +"/"+ghostName+"_Left_2.png",
        }));
    }

    /**
     * Sets game attributes, creates Sprites and Gifs.
     */
    public Game(){
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyBoard);
        this.setFocusable(true);

        // sets up game loop timer
        gameLoop = new Timer(1000/FPS,(ActionEvent e) -> {
            try {
                Update();
            } catch (GameMapException ex) {
                gameLoop.stop();
                System.out.println(ex.getMessage());
            } catch (URISyntaxException ex) {
                gameLoop.stop();
                System.out.println(ex.getMessage());
            }
            repaint();
        });
        // sets up timer for fruit showing points
        fruitShowPointsTimer = new Timer(FRUITS_POINTS_SHOW_TIME, e -> showFruitPoints = false);
        fruitShowPointsTimer.setRepeats(false);
        // sets up timer for device which provides key from cage door lock to ghosts
        keyProviderForGhosts = new Timer(DELAY_BETWEEN_GHOSTS_ENTER_TO_GAME,e->{
            if (indexOfGhostWhichGetsKey < ghosts.size()){
                ghosts.get(indexOfGhostWhichGetsKey).setKey(gameMap.getDoorKey());
                indexOfGhostWhichGetsKey++;
            }
        });

        pacManGifs = new OrientatedGif();
        blueMode = new Gif(new String[]{"/Entities/Ghosts/Ghost_Blue_1.png","/Entities/Ghosts/Ghost_Blue_2.png"});
        blueModeEnding = new Gif(new String[]{"/Entities/Ghosts/Ghost_Blue_1.png","/Entities/Ghosts/Ghost_Blue_2.png",
                "/Entities/Ghosts/Ghost_White_1.png","/Entities/Ghosts/Ghost_White_2.png"});

        pacManGifs.registerDirection(MovementDirection.UP,new Gif(new String[]{
                "/Entities/PacMan/PacMan_Up1.png",
                "/Entities/PacMan/PacMan_Up2.png",
                "/Entities/PacMan/PacMan_Up3.png",
                "/Entities/PacMan/PacMan4.png"
        }));
        pacManGifs.registerDirection(MovementDirection.DOWN,new Gif(new String[]{
                "/Entities/PacMan/PacMan_Down1.png",
                "/Entities/PacMan/PacMan_Down2.png",
                "/Entities/PacMan/PacMan_Down3.png",
                "/Entities/PacMan/PacMan4.png"
        }));
        pacManGifs.registerDirection(MovementDirection.RIGHT,new Gif(new String[]{
                "/Entities/PacMan/PacMan_Right1.png",
                "/Entities/PacMan/PacMan_Right2.png",
                "/Entities/PacMan/PacMan_Right3.png",
                "/Entities/PacMan/PacMan4.png"
        }));
        pacManGifs.registerDirection(MovementDirection.LEFT,new Gif(new String[]{
                "/Entities/PacMan/PacMan_Left1.png",
                "/Entities/PacMan/PacMan_Left2.png",
                "/Entities/PacMan/PacMan_Left3.png",
                "/Entities/PacMan/PacMan4.png"
        }));
        // creates GIFS
        blinkyGifs = new OrientatedGif();
        constructGhostGif(blinkyGifs,"Blinky");
        inkyGifs = new OrientatedGif();
        constructGhostGif(inkyGifs,"Inky");
        pinkyGifs = new OrientatedGif();
        constructGhostGif(pinkyGifs,"Pinky");
        clydeGifs = new OrientatedGif();
        constructGhostGif(clydeGifs,"Clyde");
    }

    /**
     * If game ended, it starts new one.
     */
    public void StartNewGame(){
        if (gameStates == GameStates.GAME_OVER){
            try {
                Run();
            } catch (GameMapException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * Init game.
     * Creates and loaded game map, creates PacMan,fruit and ghosts, set level to be 1,
     * loads high score from data file.
     * Also register the first fruit -> Cherry
     * @throws GameMapException if game could not be loaded
     */
    private void InitGame() throws GameMapException, URISyntaxException {
        this.gameStates=GameStates.RUNNING;
        // creates and load game map
        gameMap = new GameMap(TILE_SIZE,this);
        gameMap.loadGameMap(new File(Game.class.getResource("/GameMaps/classicPacManMap.txt").toURI()));

        // creates pacman
        pacMan = new PacMan(gameMap.GetPacManStartingLocation(), TILE_SIZE,PACMAN_RES,gameMap, pacManGifs);
        keyBoard.setPacMan(pacMan);
        keyBoard.setgame(this);
        // create ghosts
        // as default sprite ,its used the same sprite because ghosts have animation
        ghosts.clear();
        ghosts.add(new Ghost(gameMap.getBlinkyStartingPoint(), TILE_SIZE,BLINKY_RES,gameMap, blinkyGifs,new RandomDirectionChooser(),blueMode,blueModeEnding));
        ghosts.add(new Ghost(gameMap.getInkyStartingPoint(), TILE_SIZE,BLINKY_RES,gameMap, inkyGifs,new RandomDirectionChooser(),blueMode,blueModeEnding));
        ghosts.add(new Ghost(gameMap.getPinkyStartingPoint(), TILE_SIZE,BLINKY_RES,gameMap, pinkyGifs,new RandomDirectionChooser(),blueMode,blueModeEnding));
        ghosts.add(new Ghost(gameMap.getClydeStartingPoint(), TILE_SIZE,BLINKY_RES,gameMap, clydeGifs,new RandomDirectionChooser(),blueMode,blueModeEnding));
        // sets other attributes
        gameLevel = 1;
        score = 0;
        // creates fruit and default add to it Cherry fruit
        fruit = new Fruit(gameMap.getFruitPoint(), TILE_SIZE,CHERRY);
        fruit.registerNewFruit(Fruits.CHERRY,CHERRY,100);
        // sets value how many coins must be eaten by PacMan, that fruit will spawn
        gameMap.setNeededCoinForFruitSpawn(NEEDED_COINS_EAT_TO_SPAWN_FRUIT);
        // sets point of where will be drawn lives and fruits info
        fruitInfoStartingPoint = new Point(gameMap.getWidth() - TILE_SIZE * 2,gameMap.getHeight() + TOP_SHIFT + 16);
        pacManLivesInfoStartingPoint = new Point(TILE_SIZE,gameMap.getHeight() + TOP_SHIFT + 16);
        // loads high score
        // if it can not be loaded it sets high score to zero
        // and print message about it

        if (!readHighScore(new File(Game.class.getResource("/HighScore.txt").toURI()))){
            highScore = 0;
            System.out.println("Can not load HighScore!");
        }
    }

    /**
     * Add points to the score.
     * @param points value of point, which will be added
     */
    public void incScore(int points){
        score+=points;
    }

    /**
     * Calls when PacMan has eaten Power-Pellet, it notifies ghost ,that they must go to Blue mode
     */
    public void powerPelletWasEaten(){
        successionOfGhostsEating = 0;
        ghosts.forEach(Ghost::enterBlueMode);
    }
    public GameMap getGameMap() {
        return gameMap;
    }
    public PacMan getPacMan(){
        return pacMan;
    }

    /**
     * Pauses game for certain time.
     * When timer runs up it sets to game be in running state again.
     * @param timeInMs duration of pause
     * @param gameStates  type of pause , for painting and updating purposes
     */
    private void pauseGame(int timeInMs, GameStates gameStates){
        this.gameStates = gameStates;
        pauseTimer = new Timer(timeInMs,(ActionEvent e) -> this.gameStates = GameStates.RUNNING);
        pauseTimer.setRepeats(false);
        pauseTimer.start();
    }

    /**
     * Init game(create game map, loads it and construct pacman and ghost entity), also
     * stars game loop.
     * @throws GameMapException if  game map could not be loaded
     */
    public void Run() throws GameMapException, URISyntaxException {
        InitGame();
        gameLoop.start();
    }

    /**
     * Invokes when Pacman die by ghost.
     * Pauses game, takes live from pacman and resets all game objects.
     */
    private void pacManDie(){
        pauseGame(WAITING_TIME_WHEN_PACMAN_DIE, GameStates.PAUSE);
        pacMan.loseLive();
        resetGameObjects();
    }

    /**
     * Invokes when Pacman eats ghost.
     * Starts pause, add points for that to score,
     * reset eaten ghost, also gives it key from cage, because ghost
     * will be again in cage.
     * @param ghost ghosts, which was eaten
     */
    private void pacManEatsGhost(Ghost ghost){
        pauseGame(WAITING_TIME_WHEN_PACMAN_EATEN_GHOST, GameStates.PAUSE_FOR_EATING);
        ghost.reset();
        ghost.setKey(gameMap.getDoorKey());
        score +=  (int)(200 * Math.pow(2,successionOfGhostsEating));
        successionOfGhostsEating+=1;
    }

    /**
     * Calls when all coins have been collected.
     * Loads a new next game map and invokes game resets,
     * increase level attribute and process if to the next level will be added
     * next fruit.
     * @throws GameMapException if next game map could not be loaded
     */
    private void clearLevel() throws GameMapException, URISyntaxException {
        pauseGame(WAITING_TIME_WHEN_PACMAN_CLEAR, GameStates.PAUSE);
        // load new game map

        // in original Pacman game there is just one map and all levels are same
        // so its load again the same map -> all points for ghosts, fruits etc. are same
        gameMap.loadGameMap(new File(Game.class.getResource("/GameMaps/classicPacManMap.txt").toURI()));
        resetGameObjects();
        gameLevel++;
        // add fruit to the next level if it's possible
        switch (gameLevel){
            case 2 -> fruit.registerNewFruit(Fruits.STRAWBERRY,STRAWBERRY,300);
            case 3 -> fruit.registerNewFruit(Fruits.ORANGE,ORANGE,500);
            case 5 -> fruit.registerNewFruit(Fruits.APPLE,APPLE,700);
            case 7 -> fruit.registerNewFruit(Fruits.MELON,MELON,1000);
            case 9 -> fruit.registerNewFruit(Fruits.GALAXIAN_FLAGSHIP,GALAXIAN_FLAGSHIP,2000);
            case 11 -> fruit.registerNewFruit(Fruits.BELL,BELL,3000);
            case 13 -> fruit.registerNewFruit(Fruits.KEY,KEY,5000);
            default -> {}
        }
    }

    /**
     * Sets up pacMan and ghosts to their starting point, set fruit to not be visible
     * and sets other attributes which represent that pacman die or cleared level.
     */
    private void resetGameObjects(){
        pacMan.reset();
        fruit.setVisibility(false);
        ghosts.forEach(ghost -> {
            ghost.reset();
            ghost.setKey(null);
        });
        isGameStarting=true;
        indexOfGhostWhichGetsKey=1;
        gameMap.setNeededCoinForFruitSpawn(NEEDED_COINS_EAT_TO_SPAWN_FRUIT);
    }
    /**
     * Reads high score value from file, in which that value has been stored.
     * @param highScoreData file containing high score data
     * @return true if it successfully obtains value from file
     */
    private boolean readHighScore(File highScoreData){
        try( var fileReader = new Scanner((highScoreData))){
            if (fileReader.hasNextLine()){
                this.highScore = Integer.parseInt(fileReader.nextLine());
                fileReader.close();
                return true;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Can not find file with HighScore data");
            return false;
        } catch (Exception e){
            System.out.println("HighScore data file is corrupted");
            return false;
        }
        return false;
    }

    /**
     * Saves high score value to file, which stores high score value.
     * @return true if its successful
     */
    private boolean saveHighScore(){
        try {
            var file = new FileWriter("res/HighScore.txt",false);
            file.write(String.valueOf(highScore));
            file.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Indicates if fruit has spawned.
     * @return if fruit is visible
     */
    public boolean isFruitSpawn(){
        return fruit.isVisible();
    }

    /**
     * Invokes when PacMan die and does not have next live.
     * Sets gameState to be Game Over, saves HighScore and stops
     * game loop.
     */
    private void endGame(){
        gameStates = GameStates.GAME_OVER;
        if (!saveHighScore()){
            System.out.println("Can not save High Score!");
        }
        gameLoop.stop();
    }

    /**
     * Updates game, moves pacman and ghosts, check all stuff for game to work right.
     * More details are in method as comments.
     */
    private void Update() throws GameMapException, URISyntaxException {
        // if game is pause it's not updating game
        if (gameStates != GameStates.RUNNING){return;}

        // end game if PacMan does not have live
        if (!pacMan.hasLives()){
            endGame();
            return;
        }

        // if game starts (pacman die, or cleared level)
        // sets starting game pause, during which "Get ready!" is show
        if (isGameStarting){
            pauseGame(WAITING_TIME_STARTING, GameStates.PAUSE_FOR_STARTING);
            isGameStarting=false;
            return;
        }

        // timer, which gives key from cage door lock
        // if timer is not running its start it
        // if already all ghosts got key it stops timer
        if (!keyProviderForGhosts.isRunning()){
            keyProviderForGhosts.restart();
        }else {
            if (indexOfGhostWhichGetsKey == ghosts.size()){
                keyProviderForGhosts.stop();
            }
        }
        // rewrite HighCore, if it was beaten
        if (score > highScore){
            highScore = score;
        }

        // check if PacMan obtain points for getting bonus live
        if (score >= scoreNeededForBonusLive && bonusLiveAvailable){
            pacMan.getLive();
            bonusLiveAvailable = false;
        }
        // check if pacMan eats all coins on the map
        // if that's true its invokes clear process to set up a new map
        if (gameMap.getRemainingCoinsCount() == 0){
            clearLevel();
            return;
        }
        // ask map if PacMan eats enough coins to spawn fruit game object
        // if true -> set fruit to be visible, which means it will be drawn, and it can be collected
        if (gameMap.wasEatenAlreadyEnoughCoinsForFruitToBeSpawn()){
            gameMap.setNeededCoinForFruitSpawn(NEEDED_COINS_EAT_TO_SPAWN_FRUIT);
            fruit.setVisibility(true);
        }
        // call process to collect coins
        pacMan.coinCollisions();
        // moves PacMan and ghosts
        pacMan.Move();
        ghosts.forEach(MovableEntity::Move);
        // fruit process
        // if fruit is spawn and pacman is in distance to collect it
        // its set fruit to be not visible, add point to score and start timer to
        // show point on the game
        if (fruit.isVisible()){
            if (Entity.getDistance(pacMan,fruit) < TILE_SIZE - (TILE_SIZE /3)){
                fruit.setVisibility(false);
                score +=fruit.getPoints();
                showFruitPoints = true;
                fruitShowPointsTimer.start();
            }
        }
        // process of ghosts collision with PacMan or fruit
        ghosts.forEach(ghost -> {
            // if ghost get out from cage, ghosts lost key from it , so algorithm of moving can not choose to go again
            // to the cage
            if (!ghost.isInCage()){
                ghost.setKey(null);
            }
            // if ghost distance from fruit is so small, that ghost can collect it
            // its sets fruit to be not spawn, which means sets visibility to false
            if (Entity.getDistance(ghost,fruit) < TILE_SIZE - (TILE_SIZE /3)){
                fruit.setVisibility(false);
            }
            // process ghosts collision with PacMan
            if (Entity.getDistance(ghost,pacMan) < TILE_SIZE - (TILE_SIZE /4)){
                // if ghost is in Normal state, in which it can kill pacman its invoke method to process that
                // if ghost is in Blue mode, its calls' method, which represent process of pacman eating ghost
               if (ghost.getStateOfGhost() == GhostStates.NORMAL){
                   pacManDie();
               }else{
                   pacManEatsGhost(ghost);
               }
            }
        });
    }

    /**
     * Draws bold text on the screen.
     * @param g2 2D graphics, into which text will be drawn
     * @param textColor color of text
     * @param textSize size of text
     * @param location point location where text will be drawn, point represent middle of text
     * @param text string text to be drawn
     */
    private void drawBoldText(Graphics2D g2,Color textColor,int textSize,Point location,String text){
        g2.setColor(textColor);
        g2.setFont(new Font(("TimesRoman"),Font.BOLD,textSize));
        g2.drawString(text, location.x - (g2.getFontMetrics().stringWidth(text)/2) ,location.y);
    }

    /**
     * Draws GUI, which is Score and HighScore labels.
     * @param g2 2D graphics, into which GUI will be drawn
     */
    private void drawGUI(Graphics2D g2){
        drawBoldText(g2,Color.WHITE,24,new Point(32,24),"1UP");
        drawBoldText(g2,Color.WHITE,24,new Point(36,48),String.valueOf(score));
        drawBoldText(g2,Color.WHITE,24,new Point(getWidth()/2,24),"HIGH SCORE");
        drawBoldText(g2,Color.WHITE,24,new Point(getWidth()/2,48),String.valueOf(highScore));
    }

    /**
     * Draws text message in the middle of screen.
     * @param g2 2D graphics, into which text will be drawn
     * @param text string message
     * @param color color of text
     */
    private void drawTextInfoToCenterOfScreen(Graphics2D g2, String text, Color color){
        drawBoldText(g2,color,32,new Point(getWidth()/2,getHeight()/2),text);
    }

    /**
     * Calls when Pacman has eaten fruit, to draws obtained point for that.
     * @param g2  graphics, into which points will be drawn
     */
    private void drawPointsForFruitEating(Graphics2D g2){
        var point = new Point(fruit.getLocation().x + (TILE_SIZE/2),fruit.getLocation().y+TOP_SHIFT);
        drawBoldText(g2,Color.MAGENTA,32,point,(String.valueOf(fruit.getPoints())));
    }
    /**
     * Calls when Pacman has eaten ghost, to draws obtained point for that.
     * @param g2  graphics, into which points will be drawn
     */
    private void drawPointsForGhostEating(Graphics2D g2){
        var point = pacMan.getLocation();
        drawBoldText(g2,Color.CYAN,24,new Point(point.x,point.y + TOP_SHIFT),
                String.valueOf((int)(200 * Math.pow(2,successionOfGhostsEating - 1))));
    }

    /**
     * Draws game frame.
     * @param g graphics, into which all game will be drawn
     */
    @Override
    protected void paintComponent(Graphics g) {
        // convert graphic to 2D graphics for better usage
        Graphics2D g2 = (Graphics2D)g;
        // if game is paused -> return , so last frame during game running state is showed
        if (gameStates == GameStates.PAUSE) {
            return;
        }
        // if game is paused for ghost eating -> return , so last frame during game running state is showed , but only
        // add to frame and draw points obtaining for ghost eating
        if (gameStates == GameStates.PAUSE_FOR_EATING){
            drawPointsForGhostEating(g2);
            return;
        }
        // clear screen, so it can paint to "empty canvas"
        super.paintComponent(g);
        // check if game map is created and loaded
        if (gameMap == null) return;
        if (!gameMap.isLoaded()) return;
        // draws game map
        gameMap.Draw(g2, TOP_SHIFT);
        // draw scores
        drawGUI(g2);
        // if game is over it only draws map,scores and info about that
        if (gameStates == GameStates.GAME_OVER){
            drawTextInfoToCenterOfScreen(g2,"GAME OVER",Color.red);
            drawBoldText(g2,Color.WHITE,28,new Point(getWidth()/2,(getHeight()/4) * 3),"Press enter for new game");
            return;
        }
        // draws fruit if its visible
        fruit.draw(g2, TOP_SHIFT);
        // draws pacman
        pacMan.draw(g2, TOP_SHIFT);
        // draw all ghosts
        ghosts.forEach(ghost -> ghost.draw(g2, TOP_SHIFT));
        // draws pacman lives and fruits info on the bottom of screen
        fruit.drawFruitInfo(g2,fruitInfoStartingPoint);
        pacMan.drawLivesInfo(g2,pacManLivesInfoStartingPoint);
        // if pacman eats fruit it sets attribute to be true,starts timer, and it draws points for it
        // if timer runs up it sets that attribute to false
        if (showFruitPoints){
            drawPointsForFruitEating(g2);
        }
        // and lastly show text label if game is paused for it starts
        if (gameStates == GameStates.PAUSE_FOR_STARTING){
            drawTextInfoToCenterOfScreen(g2,"GET READY!",Color.YELLOW);
        }
        g2.dispose();
    }
}
