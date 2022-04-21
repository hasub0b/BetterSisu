package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Jyri
 */
public class StudentWriterTest {

    private static final String PROGRAMME_ID = "uta-tohjelma-1705";
    private static final ModuleReader MR = new ModuleReader();
    private static final DegreeProgramme DP = (DegreeProgramme) MR.fromSisu(PROGRAMME_ID);

    public StudentWriterTest() {
    }

    /**
     * Test of write method, of class StudentWriter.
     */
    @Test
    public void testWrite() {
        DegreeProgramme dptwo = new DegreeProgramme(2, "dp kaks", "dp kaks id", "dp kaks gid");
        Student test = new Student("etunimi", "sukunimi", "h123", new ArrayList<>(List.of(DP, dptwo)));
        StudentWriter sw = new StudentWriter();
        try {
            sw.write(test);
        } catch (IOException ex) {
            fail("Couldn't write file");
        }
    }

}
