
package fi.tuni.prog3.sisu;

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
public class UrlJsonFetcherTest {
    
    public UrlJsonFetcherTest() {
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
    public void testGetDegrees() {
        String testDegrees = UrlJsonFetcher.getDegrees();
        assertTrue(testDegrees.endsWith("\"credits\":{\"min\":120,\"max\":null}}],\"truncated\":false,\"notifications\":null}"));
    }
    
    @Test
    public void testGetModule() {
        String testModule = UrlJsonFetcher.getModule("otm-1d25ee85-df98-4c03-b4ff-6cad7b09618b");
        assertTrue(testModule.endsWith("\"credits\":{\"min\":180,\"max\":null}}}"));
    }
    
    @Test
    public void testGetCourseUnit() {
        
    }
}
