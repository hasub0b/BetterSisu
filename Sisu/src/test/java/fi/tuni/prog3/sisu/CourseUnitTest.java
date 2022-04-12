package fi.tuni.prog3.sisu;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * Test for CourseUnit
 *
 * @author hasu
 */

class CourseUnitTest {

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

        courseUnit.addContent(content);
        courseUnit.addPrerequisite(prerequisite);
        courseUnit.addOutcome(outcome);
        courseUnit.addMaterial(material);
        
        assertEquals(courseUnit.getContent(), content);
        assertEquals(courseUnit.getPrerequisite(), prerequisite);
        assertEquals(courseUnit.getOutcome(), outcome);
        assertEquals(courseUnit.getMaterial(), material);
    }
}