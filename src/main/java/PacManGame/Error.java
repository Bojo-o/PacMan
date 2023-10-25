package PacManGame;

import javax.swing.*;
import java.awt.*;

/**
 * Represent "error message window".
 * Invokes if game throws some exception to notify user.
 */
public class Error extends JPanel {
    private final String message;

    /**
     * Crete error message object.
     * @param message error message, which will be showed on the screen
     */
    public Error(String message){
        this.message = message;
        this.setPreferredSize(new Dimension(700,200));
        this.setBackground(Color.BLACK);
    }

    /**
     * Draw in the center of screen error message.
     * @param g graphics
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.setFont(new Font(("TimesRoman"),Font.BOLD,16));
        g2.drawString(message, getWidth()/2 - (g2.getFontMetrics().stringWidth(message)/2) ,getHeight()/2);
    }
}
