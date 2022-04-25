package fi.tuni.prog3.sisu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * Simple JWindow displayed at the start so program can be loaded on the background
 *
 * @author Hasu
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
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }
                showScreen();
            }
        });
    }

    public void showScreen() {

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
        Font font = welcomeText.getFont();
        welcomeText.setFont(font.deriveFont(Font.BOLD, 28f));

        // Get vertical position
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        label.add(welcomeText,gbc);


        ImageIcon wait = new ImageIcon("Sisu\\Loading_icon.gif");
        label.add(new JLabel(wait), gbc);


        // Display it
        setVisible(true);
        toFront();

    }

}