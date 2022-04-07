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
                5
        );

        assertEquals("id", courseUnit.getId());
        assertEquals("courseUnitGroupId", courseUnit.getCourseUnitGroupId());
        assertEquals("name", courseUnit.getName());
        assertEquals("code", courseUnit.getCode());
        assertEquals(5, courseUnit.getCredits());

    }
}