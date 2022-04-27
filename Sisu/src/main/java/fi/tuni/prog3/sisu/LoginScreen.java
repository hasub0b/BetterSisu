package fi.tuni.prog3.sisu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

/**
 *
 * A Login Screen window that is displayed before anything
 * Asks user for a full name and student ID
 * Closes when user has given all needed information
 *
 * @author Aleski Hasu
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
                try {
                    showLogin();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void showLogin() throws IOException {

        final JFrame frame = new JFrame("SISU LOGIN");
        frame.setResizable(false);

        // frame size and location on the screen
        int width = 300;
        int height = 200;
        frame.setSize(width, height);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        frame.setLocation(x,y);

        // create necessary components
        //JLabel lblStudents = new JLabel("Saved Students: ");
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
        enter.setToolTipText("Continue to Sisu");
        JLabel information = new JLabel("Enter all details above before continuing");
        information.setLabelFor(enter);

        JLabel message = new JLabel();
        Font font = message.getFont();
        message.setFont(font.deriveFont(Font.BOLD, 12));
        message.setForeground(Color.RED);

        StudentReader sr = new StudentReader();
        // Handle button action
        enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(firstName.getText(), "")){
                    message.setText("First name can't be empty!");
                } else if (Objects.equals(lastName.getText(), "")){
                    message.setText("Last name can't be empty!");
                } else if (Objects.equals(studentId.getText(), "")){
                    message.setText("Student ID can't be empty!");
                }else {
                    try {
                        if(sr.exists(studentId.getText())){
                            Student foundStudent = sr.read(studentId.getText());
                            if (Objects.equals(lastName.getText(), foundStudent.getLastName()) && Objects.equals(firstName.getText(), foundStudent.getFirstName())){
                                enterPressed(firstName,lastName,studentId);
                                frame.setVisible(false);
                            }else {
                                message.setText("Matching ID with different name found!");
                            }

                        } else {
                            enterPressed(firstName,lastName,studentId);
                            frame.setVisible(false);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        } );

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // populate saved Students for test purposes
        /*
        new StudentWriter().write(new Student("firstName", "lastName", "studentId"));
        new StudentWriter().write(new Student("James", "Bond", "007"));
        new StudentWriter().write(new Student("Matti", "Meikalainen", "000-001"));
         */


        Collection<Student> savedStudents = new StudentReader().readAll();
        JComboBox<Student> students = new JComboBox<Student>();
        students.setPrototypeDisplayValue(new Student("                     ","                    ","                    "));

        students.setToolTipText("Choose an existing student");
        // if there's no saved student used we use different JComboBox so the ActionListener won't be set
        if (savedStudents.isEmpty()){
            JComboBox<String> studentsString = new JComboBox<String>();
            studentsString.addItem("NO SAVED STUDENTS FOUND");
            panel.add(studentsString);

        } else {
            for (Student stud : savedStudents){
                students.addItem(stud);
            }

            panel.add(students);

        }

        // handle the event when the user is selects an item from the drop-down list.
        students.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                JComboBox<Student> combo = (JComboBox<Student>) event.getSource();
                Student selectedStudent = (Student) combo.getSelectedItem();

                firstName.setText(selectedStudent.getFirstName());
                lastName.setText(selectedStudent.getLastName());
                studentId.setText(selectedStudent.getStudentId());
            }
        });

        // add components to the panel
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
    // handle the "continue"-button action, when user has given valid inputs to all fields
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
