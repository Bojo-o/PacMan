package PacManGame.Entities.Animation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Object representing animation of some GIF.
 * It provides mechanism for changing which sprites will be showed,
 * so it creates illusion of animation.
 */
public class Animation {
    private Gif gif;
    // represents index, which sprite from gif is showed
    private int phaseIndex = 0;
    // represents size of frame, which will be drawn
    private final int sizeOfImage;
    // used for symmetry animation
    private boolean isSymmetry = false;
    private boolean isPhaseInc = true;
    // timer, which changes sprites
    private final Timer animationLoop;

    /**
     *
     * @param gif gif, representing animation
     * @param sizeOfImage represents size of image
     * @param animationTime time in Ms, which represents time of duration of one iteration of gif.
     */
    public Animation(Gif gif,int sizeOfImage,int animationTime){
        this.gif = gif;
        this.sizeOfImage = sizeOfImage;
        animationLoop = new Timer(animationTime / gif.getSizeOfFrame(),(ActionEvent e) -> updatePhase());
    }

    /**
     * Changes GIF, if the new provided GIF has same size as old one, it leaves phase of which frame is showed.
     * @param newGif new GIF, which will be replaced for old.
     */
    public void changeGif(Gif newGif){
        if (gif.getSizeOfFrame() != newGif.getSizeOfFrame()){
            phaseIndex = 0;
            isPhaseInc = true;
        }
        gif = newGif;
    }

    /**
     * Start animation, so sprites starts to changing.
     */
    public void startAnimation(){
        animationLoop.start();
    }

    public void setSymmetry(boolean value){
        isSymmetry = value;
    }

    /**
     * Draw the certain frame, which is representing phase of animation.
     * @param g2 2D graphics, it will be drawn into it.
     * @param location point of coordinates, where frame will be drawn.
     */
    public void draw(Graphics2D g2, Point location) {
        g2.drawImage(gif.getFrame(phaseIndex), location.x, location.y, sizeOfImage,sizeOfImage,null);
    }
    /**
     * Update phase of animation.
     */
    private void updatePhase(){
        if (gif.getSizeOfFrame() == 1){
            return;
        }

        if (isSymmetry){
            if (isPhaseInc){
                phaseIndex++;
                if (phaseIndex == gif.getSizeOfFrame()){
                    phaseIndex -=2;
                    isPhaseInc = false;
                }
            }else{
                phaseIndex--;
                if (phaseIndex == -1){
                    phaseIndex+=2;
                    isPhaseInc=true;
                }
            }
        }else{
           phaseIndex++;
           if (phaseIndex == gif.getSizeOfFrame()){
               phaseIndex = 0;
           }
        }
    }
}
