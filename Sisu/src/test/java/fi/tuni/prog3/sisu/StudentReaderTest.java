package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 *
 * @author Jyri
 */
public class StudentReaderTest {

    private static final String PROGRAMME_ID = "uta-tohjelma-1705";
    private static final ModuleReader MR = new ModuleReader();
    private static final DegreeProgramme DP = (DegreeProgramme) MR.buildModule(PROGRAMME_ID);

    @TempDir
    static Path tempDir;

    public StudentReaderTest() {
    }

    @Test
    public void testBasicRead() {
        String origFirstName = "First";
        String origLastName = "Last";
        String origId = "Something";
        Student original = new Student(origFirstName, origLastName, origId, new ArrayList<>());

        StudentWriter sw = new StudentWriter();
        String tdString = tempDir.toString();
        try {
            System.err.println("Writing to: " + tdString);
            sw.write(tdString, original);
        } catch (IOException ioe) {
            System.err.println(ioe);
            fail("Failed to write student");
        }

        StudentReader sr = new StudentReader();
        Student s = null;
        try {
            System.err.println("Reading from: " + tdString);
            s = sr.read(tdString, origId);
        } catch (IOException ioe) {
            System.err.println(ioe);
            fail("Failed to read student");
        }

        assertNotNull(s);
        assertTrue(s.getFirstName().equalsIgnoreCase(origFirstName));
        assertTrue(s.getLastName().equalsIgnoreCase(origLastName));
        assertTrue(s.getStudentId().equalsIgnoreCase(origId));
    }

    @Test
    public void testReadAll() {
        DegreeProgramme dptwo = new DegreeProgramme(2, "dp kaks", "dp kaks id", "dp kaks gid");
        Student first = new Student("one", "lastName", "1", new ArrayList<>(List.of(DP, dptwo)));
        Student second = new Student("second", "lastName", "2", new ArrayList<>());
        Student third = new Student("third", "lastName", "3", new ArrayList<>());

        ArrayList<Student> testCollection = new ArrayList<>(List.of(first, second, third));

        String tdString = tempDir.toString();

        StudentWriter sw = new StudentWriter();
        try {
            System.err.println("Writing to: " + tdString);
            sw.writeAll(tdString, testCollection);
        } catch (IOException ioe) {
            System.err.println(ioe);
            fail("Failed to write students");
        }

        StudentReader sr = new StudentReader();
        ArrayList<Student> resultCollection = null;
        try {
            System.err.println("Reading from: " + tdString);
            resultCollection = new ArrayList<>(sr.readAll(tdString));
        } catch (IOException ioe) {
            System.err.println(ioe);
            fail("Failed to read students");
        }

        assertNotNull(resultCollection);
        // should be 4 total json files thanks to other test
        assertTrue(resultCollection.size() >= testCollection.size());
        // id's for first, second, third mean they are read before original (see line 33)
        for (int i = 0; i < testCollection.size(); i++) {
            assertTrue(resultCollection.get(i).equals(testCollection.get(i)));
        }
    }
}
