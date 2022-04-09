
package fi.tuni.prog3.sisu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test JSON fetching from URL
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
    public void testConnection() throws IOException {
        URL urlObj = new URL("https://sis-tuni.funidata.fi/");
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        assertEquals(200,connection.getResponseCode());
    }

    @Test
    public void testGetDegrees() {
        String testDegrees = UrlJsonFetcher.getDegreeList();
        assertTrue(testDegrees.endsWith("\"credits\":{\"min\":120,\"max\":null}}],\"truncated\":false,\"notifications\":null}"));
    }
    
    @Test
    public void testGetModule() {
        String testModule = UrlJsonFetcher.getModule("uta-ok-ykoodi-41176");
        assertTrue(testModule.endsWith("\"description\":null,\"allMandatory\":true}}"));
    }
    
    @Test
    public void testGetCourseUnit() {
        String testCourseUnit = UrlJsonFetcher.getCourseUnit("uta-ykoodi-47926");
        assertTrue(testCourseUnit.endsWith("\"inclusionApplicationInstruction\":null}"));
    }
}
