
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
            testBR = new BufferedReader(new FileReader("src/test/resources/courseUnitExample.json"));
        } catch (FileNotFoundException e) {
            fail("Test file not found (Module)");
        }
        try {
            String line = testBR.readLine();
            while (line != null) {
                line = testBR.readLine();
                jsonString += line;
            }
        } catch (IOException e) {
            fail("File ended unexpectedly");
        }
        
        ModuleReader mod = new ModuleReader();
        Module testModule = mod.fromString(jsonString);
        
        assertEquals(testModule.getName(), "Basic Studies in Computer Sciences");
        assertEquals(testModule.getId(), "otm-af70be28-9bf5-49f7-b8fc-41a2bafbf2f2");
        assertEquals(testModule.getGroupId(), "uta-ok-ykoodi-41176");
    }
    
}
