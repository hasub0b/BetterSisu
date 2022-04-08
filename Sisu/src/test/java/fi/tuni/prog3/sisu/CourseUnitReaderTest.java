
package fi.tuni.prog3.sisu;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Leo
 */
public class CourseUnitReaderTest {
    
    public CourseUnitReaderTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testFromString() {
        BufferedReader testBR = null;
        String jsonString = "";
        try {
            testBR = new BufferedReader(new FileReader("src/test/resources/courseUnitExample.json"));
        } catch (FileNotFoundException e) {
            fail("Test file not found (Course unit)");
        }
        try {
            String line = testBR.readLine();
            while (line != null) {
                System.out.println(line);
                line = testBR.readLine();
                jsonString += line;
            }
        } catch (IOException e) {
            fail("File ended unexpectedly");
        }
        
        CourseUnitReader cur = new CourseUnitReader();
        CourseUnit testCU = cur.fromString(jsonString);
        
        assertEquals(testCU.getName(), "Introduction to Analysis");
        assertEquals(testCU.getCode(), "MATH.MA.110");
        assertEquals(testCU.getCourseUnitGroupId(), "uta-ykoodi-47926");
        assertEquals(testCU.getCredits(), 5);
        assertEquals(testCU.getId(), "otm-94ffcfc5-0db4-4507-b475-63f290639e04");
    }
}
