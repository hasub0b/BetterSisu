package fi.tuni.prog3.sisu;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Jyri
 */
public class StudentTest {

    private Student testStudent;
    private static final String FIRST_NAME = "Firstname";
    private static final String LAST_NAME = "Lastname";
    private static final String STUDENT_ID = "K12345";
    private static final DegreeProgramme FIRST_PROGRAMME
            = new DegreeProgramme(5, "ABC", "anID", "aGID");
    private static final DegreeProgramme SECOND_PROGRAMME
            = new DegreeProgramme(3, "DEF", "anotherID", "anotherGID");

    public StudentTest() {
    }

    @BeforeEach
    public void setUp() {
        ArrayList<DegreeProgramme> programmes = new ArrayList<>(List.of(FIRST_PROGRAMME, SECOND_PROGRAMME));
        testStudent = new Student(FIRST_NAME, LAST_NAME, STUDENT_ID, programmes);
    }

    @Test
    public void testGetFirstName() {
        assertEquals(testStudent.getFirstName(), FIRST_NAME);
    }

    @Test
    public void testGetLastName() {
        assertEquals(testStudent.getLastName(), LAST_NAME);
    }

    @Test
    public void testGetStudentId() {
        assertEquals(testStudent.getStudentId(), STUDENT_ID);
    }

    @Test
    public void testGetProgrammes() {
        ArrayList<DegreeProgramme> result = testStudent.getProgrammes();
        assertTrue(result.size() == 2);
        assertAll("first DegreeProgramme equalities",
                () -> assertEquals(result.get(0).getName(), FIRST_PROGRAMME.getName()),
                () -> assertEquals(result.get(0).getId(), FIRST_PROGRAMME.getId()),
                () -> assertEquals(result.get(0).getGroupId(), FIRST_PROGRAMME.getGroupId())
        );
        assertAll("second DegreeProgramme equalities",
                () -> assertEquals(result.get(1).getName(), SECOND_PROGRAMME.getName()),
                () -> assertEquals(result.get(1).getId(), SECOND_PROGRAMME.getId()),
                () -> assertEquals(result.get(1).getGroupId(), SECOND_PROGRAMME.getGroupId())
        );
    }

    @Test
    public void testSetFirstName() {
        String expected = "Some other name";
        testStudent.setFirstName(expected);
        assertEquals(testStudent.getFirstName(), expected);
    }

    @Test
    public void testSetLastName() {
        String expected = "Some other name";
        testStudent.setLastName(expected);
        assertEquals(testStudent.getLastName(), expected);
    }

    @Test
    public void testSetStudentId() {
        String expected = "Another ID";
        testStudent.setStudentId(expected);
        assertEquals(testStudent.getStudentId(), expected);
    }

    @Test
    public void testSetProgrammes() {
        ArrayList<DegreeProgramme> newList = new ArrayList<>(List.of(SECOND_PROGRAMME));
        testStudent.setProgrammes(newList);
        ArrayList<DegreeProgramme> afterSet = testStudent.getProgrammes();
        assertTrue(afterSet.size() == 1);
        assertAll(
                () -> assertEquals(afterSet.get(0).getName(), SECOND_PROGRAMME.getName()),
                () -> assertEquals(afterSet.get(0).getId(), SECOND_PROGRAMME.getId()),
                () -> assertEquals(afterSet.get(0).getGroupId(), SECOND_PROGRAMME.getGroupId())
        );
    }

    @Test
    public void testAddProgramme() {
        DegreeProgramme third = new DegreeProgramme(1, "asd", "fgh", "jkl");
        testStudent.addProgramme(third);
        assertTrue(testStudent.getProgrammes().size() == 3);
    }

    @Test
    public void testToString() {
        String expected = String.format("%s, %s (%s)", LAST_NAME, FIRST_NAME, STUDENT_ID);
        assertEquals(testStudent.toString(), expected);
    }

    @Test
    public void testCompareTo() {
        Student first = new Student("a Firstname", "a Lastname", "a StudentID");
        Student second = new Student("a Firstname", "b Lastname", "a StudentID");
        Student third = new Student("b Firstname", "b Lastname", "a StudentID");
        Student fourth = new Student("b Firstname", "b Lastname", "b StudentID");

        assertAll(
                () -> {
                    assertAll("primary",
                            () -> assertTrue(first.compareTo(second) < 0),
                            () -> assertTrue(first.compareTo(third) < 0),
                            () -> assertTrue(first.compareTo(fourth) < 0)
                    );
                },
                () -> {
                    assertAll("secondary",
                            () -> assertTrue(second.compareTo(third) < 0),
                            () -> assertTrue(second.compareTo(fourth) < 0)
                    );
                },
                () -> {
                    assertAll("tertiary",
                            () -> assertTrue(third.compareTo(fourth) < 0)
                    );
                }
        );
    }

}
