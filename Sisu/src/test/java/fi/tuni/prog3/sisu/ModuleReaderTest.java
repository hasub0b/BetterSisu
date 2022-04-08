
package fi.tuni.prog3.sisu;

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
 * Tests for ModuleReader
 *
 * @author Leo
 */
public class ModuleReaderTest {
    
    public ModuleReaderTest() {
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

    /*
     * Tests non-recursive parts of fromString. Recursive part in future test!
     */
    @Test
    public void testFromString() {
        BufferedReader testBR = null;
        String jsonString = "";
        try {
            testBR = new BufferedReader(new FileReader("src/test/resources/ModuleExample.json"));
        } catch (FileNotFoundException e) {
            fail("Test file not found (Module)");
        }
        try {
            String line = testBR.readLine();
            while (line != null) {
                jsonString += line;
                line = testBR.readLine();
            }
        } catch (IOException e) {
            fail("File ended unexpectedly");
        }
        
        // The files start and finish with a square bracket. Gson didn't like this
        jsonString = jsonString.substring(1, jsonString.length()-1);
        
        ModuleReader mod = new ModuleReader();
        Module testModule = mod.fromString(jsonString);
        
        assertEquals(testModule.getName(), "Basic Studies in Computer Sciences");
        assertEquals(testModule.getId(), "otm-af70be28-9bf5-49f7-b8fc-41a2bafbf2f2");
        assertEquals(testModule.getGroupId(), "uta-ok-ykoodi-41176");
    }
    
}
