package fi.tuni.prog3.sisu;

import java.util.ArrayList;

/**
 * Describes a student and their chosen DegreeProgramme(s)
 *
 * @author Jyri
 */
public class Student {

    private final String firstName;
    private final String lastName;
    private final String studentId;
    private ArrayList<DegreeProgramme> programmes;

    public Student(String firstName, String lastName, String studentId, ArrayList<DegreeProgramme> programmes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentId = studentId;
        this.programmes = programmes;
    }

    public Student(String firstName, String lastName, String studentId) {
        this(firstName, lastName, studentId, new ArrayList<DegreeProgramme>());
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStudentId() {
        return studentId;
    }

    public ArrayList<DegreeProgramme> getProgrammes() {
        return programmes;
    }

    @Override
    public String toString() {
        return String.format("%s, %s (%s)", lastName, firstName, studentId);
    }
}
