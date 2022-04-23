package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 *
 * @author Jyri
 */
public class StudentWriterTest {

    private static final String PROGRAMME_ID = "uta-tohjelma-1705";
    private static final ModuleReader MR = new ModuleReader();
    // private static final DegreeProgramme DP = (DegreeProgramme) MR.buildModule(PROGRAMME_ID);
    private static final DegreeProgramme DP = (DegreeProgramme) MR.fromSisu(PROGRAMME_ID);

    @TempDir
    static Path tempDir;

    public StudentWriterTest() {
    }

    /**
     * Test of write method, of class StudentWriter.
     */
    @Test
    public void testWrite() {
        DegreeProgramme dptwo = new DegreeProgramme(2, "dp kaks", "dp kaks id", "dp kaks gid");
        String testStudentId = "h123";
        Student test = new Student("etunimi", "sukunimi", testStudentId, new ArrayList<>(List.of(DP, dptwo)));
        StudentWriter sw = new StudentWriter();
        String tdString = tempDir.toString();
        try {
            System.err.println("Writing to: " + tdString);
            sw.write(tdString, test);
        } catch (IOException ex) {
            fail("Couldn't write file");
        }

        Path expectedFile = tempDir.resolve(testStudentId + ".json");
        assertTrue(expectedFile.toFile().exists());
    }

    @Test
    public void testWriteAll() {
        String testStudentId = "h123";
        Student test = new Student("etunimi", "sukunimi", testStudentId, new ArrayList<>(List.of(DP)));

        String anotherStudentId = "K098";
        Student anotherStudent = new Student("first", "last", anotherStudentId, new ArrayList<>());

        Collection<Student> testStudents = new ArrayList<>(List.of(test, anotherStudent));

        StudentWriter sw = new StudentWriter();
        String tdString = tempDir.toString();
        try {
            System.err.println("Writing to: " + tdString);
            sw.writeAll(tdString, testStudents);
        } catch (IOException ex) {
            fail("Couldn't write file");
        }

        Path expectedFile = tempDir.resolve(testStudentId + ".json");
        assertTrue(expectedFile.toFile().exists());

        Path otherExpectedFile = tempDir.resolve(anotherStudentId + ".json");
        assertTrue(otherExpectedFile.toFile().exists());
    }
}
