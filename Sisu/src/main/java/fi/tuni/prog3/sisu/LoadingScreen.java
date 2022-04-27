package fi.tuni.prog3.sisu;

import java.awt.*;
import javax.swing.*;

/**
 *
 * Simple JWindow displayed at the start after login screen closes so the program can be loaded on the background
 *
 * @author Aleksi Hasu
 */

public class LoadingScreen extends JWindow {

    public static void main(String[] args) {
        new LoadingScreen();
    }

    public LoadingScreen() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // set look and feel
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                showScreen();

            }
        });
    }

    /**
     * Create a new JPanel and display it
     */
    public void showScreen() {

        // Root panel
        JPanel content = (JPanel) getContentPane();
        content.setBackground(Color.WHITE);

        // Set the window's bounds, centering the window
        int width = 700;
        int height = 460;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);

        // Create loading screen
        JLabel label = new JLabel();
        JLabel welcomeText = new JLabel("WELCOME TO SISU!", JLabel.CENTER);
        content.add(label, BorderLayout.CENTER);
        label.setLayout(new GridBagLayout());

        // Set font
        Font font = welcomeText.getFont();
        welcomeText.setFont(font.deriveFont(Font.BOLD, 28f));

        // Get vertical position
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        label.add(welcomeText,gbc);

        // Load the gif
        ImageIcon wait = new ImageIcon("Sisu\\Loading_icon.gif");
        label.add(new JLabel(wait), gbc);

        // Display it
        setVisible(true);
        toFront();

    }

}