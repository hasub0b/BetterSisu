
package fi.tuni.prog3.sisu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for CourseUnit
 *
 * @author hasu
 */

public class CourseUnitTest {

    public CourseUnitTest() {
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
    public void testConstructor() {
        CourseUnit courseUnit = new CourseUnit(
                "id",
                "courseUnitGroupId",
                "name",
                "code",
                5,
                6
        );

        assertEquals("id", courseUnit.getId());
        assertEquals("courseUnitGroupId", courseUnit.getCourseUnitGroupId());
        assertEquals("name", courseUnit.getName());
        assertEquals("code", courseUnit.getCode());
        assertEquals(5, courseUnit.getMinCredits());
        assertEquals(6, courseUnit.getMaxCredits());
    }
    
    @Test
    public void testAdditionalInfo() {
        CourseUnit courseUnit = new CourseUnit(
                "id",
                "courseUnitGroupId",
                "name",
                "code",
                5,
                6
        );
        
        String content = "Contains a heap of homework";
        String prerequisite = "Assumes student is smart";
        String outcome = "Student knows airspeed velocity of unladen swallow";
        String material = "Book.";

        assertTrue(courseUnit.hasContent());
        
        courseUnit.addContent(content);
        courseUnit.addPrerequisite(prerequisite);
        courseUnit.addOutcome(outcome);
        courseUnit.addMaterial(material);
        
        assertTrue(!courseUnit.hasContent());
        assertEquals(courseUnit.getContent(), content);
        assertEquals(courseUnit.getPrerequisite(), prerequisite);
        assertEquals(courseUnit.getOutcome(), outcome);
        assertEquals(courseUnit.getMaterial(), material);
    }
}