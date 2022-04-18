package fi.tuni.prog3.sisu;

import java.util.ArrayList;

/**
 * Describes a student and their chosen DegreeProgramme(s)
 *
 * @author Jyri
 */
public class Student implements Comparable<Student> {

    private String firstName;
    private String lastName;
    private String studentId;
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

    public Student() {
        this.firstName = new String();
        this.lastName = new String();
        this.studentId = new String();
        this.programmes = new ArrayList<>();
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setProgrammes(ArrayList<DegreeProgramme> programmes) {
        this.programmes = programmes;
    }

    public void addProgramme(DegreeProgramme programme) {
        programmes.add(programme);
    }

    @Override
    public String toString() {
        return String.format("%s, %s (%s)", lastName, firstName, studentId);
    }

    @Override
    public int compareTo(Student o) {
        int result = lastName.compareToIgnoreCase(o.getLastName());
        if (result == 0) {
            result = firstName.compareToIgnoreCase(o.getFirstName());
            if (result == 0) {
                result = studentId.compareToIgnoreCase(o.getStudentId());
            }
        }

        return result;
    }

    public boolean equals(Student other) {
        return (lastName.equalsIgnoreCase(other.getLastName())
                && firstName.equalsIgnoreCase(other.getFirstName())
                && studentId.equalsIgnoreCase(other.getStudentId()));
    }
}
