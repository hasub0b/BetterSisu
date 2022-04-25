package fi.tuni.prog3.sisu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 *
 * A Login Screen window that is displayed before anything
 * Asks user for a full name and student ID
 * Closes when user has given all needed information
 *
 * @author Hasu
 */
public class LoginScreen {

    boolean loginEntered = false;
    String firstName = "";
    String lastName = "";
    String studentId = "";

    public static void main(String[] args) {
        new LoginScreen();
    }

    public LoginScreen() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }
                showLogin();
            }
        });
    }

    public void showLogin(){

        final JFrame frame = new JFrame("SISU LOGIN");

        int width = 300;
        int height = 200;
        frame.setSize(width, height);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        frame.setLocation(x,y);

        JLabel lblFirstName = new JLabel("First Name:");
        JTextField firstName = new JTextField(20);
        lblFirstName.setLabelFor(firstName);
        JLabel lblLastName = new JLabel("Last Name:");
        JTextField lastName = new JTextField(20);
        lblLastName.setLabelFor(lastName);
        JLabel lblStudentID = new JLabel("Student ID");
        JTextField studentId = new JTextField(20);
        lblStudentID.setLabelFor(studentId);
        JButton enter = new JButton("Continue");
        JLabel information = new JLabel("Enter all details above before continuing");
        information.setLabelFor(enter);

        JLabel message = new JLabel();
        Font font = message.getFont();
        message.setFont(font.deriveFont(Font.BOLD, 12));
        message.setForeground(Color.RED);

        enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(firstName.getText(), "")){
                    message.setText("First name can't be empty!");
                } else if (Objects.equals(lastName.getText(), "")){
                    message.setText("Last name can't be empty!");
                } else if (Objects.equals(studentId.getText(), "")){
                    message.setText("Student ID can't be empty!");
                } else {
                    enterPressed(firstName,lastName,studentId);
                    frame.setVisible(false);
                }

            }
        } );

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(lblFirstName);
        panel.add(firstName);
        panel.add(lblLastName);
        panel.add(lastName);
        panel.add(lblStudentID);
        panel.add(studentId);
        panel.add(enter);
        panel.add(information);
        panel.add(message);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

    }
    public void enterPressed(JTextField first, JTextField last, JTextField id){

        try {
            firstName = first.getText();
            lastName = last.getText();
            studentId = id.getText();

        } catch (Exception e){
            System.err.println("Invalid inputs");
        }

        loginEntered = true;

    }

    public boolean isLoginEntered() {
        return loginEntered;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getStudentID() {
        return studentId;
    }

}
