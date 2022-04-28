package fi.tuni.prog3.sisu;

import java.util.ArrayList;

/**
 * Describes a student and their chosen DegreeProgramme. Has a case-insensitive
 * natural ordering by last name, followed by first name and finally by student
 * id.
 *
 * @author Jyri
 */
public class Student implements Comparable<Student> {

    private String firstName;
    private String lastName;
    private final String studentId;
    private DegreeProgramme programme;
    private String degreeId;

    /**
     * Constructs a Student with given parameters.
     *
     * @param firstName First name
     * @param lastName Last name
     * @param studentId Identification number
     * @param programme DegreeProgramme associated with the student
     */
    public Student(String firstName, String lastName, String studentId, DegreeProgramme programme) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentId = studentId;
        this.programme = programme;
        this.degreeId = "";
    }

    /**
     * Constructs a Student without a chosen programme.
     *
     * @param firstName First name
     * @param lastName Last name
     * @param studentId Identification number
     */
    public Student(String firstName, String lastName, String studentId) {
        this(firstName, lastName, studentId, null);
    }

    /**
     * Initializes an empty Student.
     */
    public Student() {
        this.firstName = new String();
        this.lastName = new String();
        this.studentId = new String();
        this.programme = null;
        this.degreeId = "";
    }

    /**
     * Returns this students first name.
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns this students last name.
     *
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns this students student id.
     *
     * @return student id
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * Returns an ArrayList of the DegreeProgrammes chosen by this student.
     *
     * @return programmes of this student
     */
    public DegreeProgramme getProgramme() {
        return programme;
    }

    /**
     * Sets this students first name to given String.
     *
     * @param firstName first name to change to
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets this students last name to given String.
     *
     * @param lastName last name to change to
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets this students chosen programmes to given.
     *
     * @param programme programme to be associated with this student
     */
    public void setProgramme(DegreeProgramme programme) {
        this.programme = programme;
    }

    /**
     * Formats a student as "Last name, First name (Student Id)"
     *
     * @return String representation of this student
     */
    @Override
    public String toString() {
        return String.format("%s, %s (%s)", lastName, firstName, studentId);
    }

    /**
     * Compares two Students for ordering.
     *
     * @param o other Student to be compared
     * @return negative/zero/positive value if this Student is less than/equal
     * to/greater than the other Student in ordering
     */
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

    /**
     * Compares two Students to find if they represent the same person.
     *
     * @param other Student to compare to
     * @return true if equal, false otherwise
     */
    public boolean equals(Student other) {
        return (lastName.equalsIgnoreCase(other.getLastName())
                && firstName.equalsIgnoreCase(other.getFirstName())
                && studentId.equalsIgnoreCase(other.getStudentId()));
    }

    public void setDegreeId(String groupID) {
        this.degreeId = groupID;
    }

    public String getDegreeId() {
        return degreeId;
    }
    
    public boolean hasProgramme() {
        return (programme != null);
    }
}
