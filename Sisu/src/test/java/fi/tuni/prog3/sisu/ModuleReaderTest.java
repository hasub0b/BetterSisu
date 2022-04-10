
package fi.tuni.prog3.sisu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
    public void testFromSisu() {
        ModuleReader mod = new ModuleReader();
        Module testModule = mod.fromSisu("uta-ok-ykoodi-41176");
        
        assertEquals(testModule.getName(), "Basic Studies in Computer Sciences");
        assertEquals(testModule.getId(), "otm-af70be28-9bf5-49f7-b8fc-41a2bafbf2f2");
        assertEquals(testModule.getGroupId(), "uta-ok-ykoodi-41176");
    }
    
    /*
     * To be implemented.
     */
    @Test
    public void testFromJson() {
        // NOTE: THIS READS INFO FROM A JSON FILE.
        /*
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
        */
    }
    
    @Test
    public void testGetSubModuleNames() {
        String groupId1 = "uta-ok-ykoodi-41176";
        String groupId2 = "uta-tohjelma-1705";
        String geriatryTestId = "otm-7eb812ae-2f0a-4e3a-ac06-bf4695df3fad";
        
        ModuleReader mr = new ModuleReader();
        
        ArrayList<String> courseUnitData = mr.getSubGroupIds(groupId1).get("unit");
        ArrayList<String> moduleData = mr.getSubGroupIds(groupId2).get("module");
        ArrayList<String> geriatryData = mr.getSubGroupIds(geriatryTestId).get("module");
        
        ArrayList<String> expectedUnits = new ArrayList<>();
        expectedUnits.add("tut-cu-g-45620");
        expectedUnits.add("tut-cu-g-45510");
        expectedUnits.add("tut-cu-g-45474");
        expectedUnits.add("otm-5381c680-bc69-4400-a578-ec0a79141cd0");
        expectedUnits.add("otm-4da0e13d-222a-4e14-a239-86b76346ba14");

        ArrayList<String> expectedModules = new ArrayList<>();
        expectedModules.add("otm-3858f1d8-4bf9-4769-b419-3fee1260d7ff");
        expectedModules.add("uta-ok-ykoodi-41176");
        expectedModules.add("uta-ok-ykoodi-41177");
        expectedModules.add("otm-6c36cb36-1507-44ff-baab-a30ac76ca786");
        expectedModules.add("otm-35d5a7e1-71c1-456a-8783-9cf8c34262f5");
        
        ArrayList<String> expectedGeriatry = new ArrayList<>();
        expectedGeriatry.add("otm-eaea17e2-13af-4dda-ba94-a6d0ad393a3e");
        
        assertEquals(expectedUnits, courseUnitData);
        assertEquals(expectedModules, moduleData);
        assertEquals(expectedGeriatry, geriatryData);
    }
    
    @Test
    public void testGatherSubs() {
        String geriatryTestId = "otm-7eb812ae-2f0a-4e3a-ac06-bf4695df3fad";
        ModuleReader mr = new ModuleReader();
        Module testDP = mr.fromSisu(geriatryTestId);
        
        CourseUnit testCU = testDP.getSubModules().get(0).getSubUnits().get(0);
        String testCUName = testCU.getName();
        
        assertEquals(testCUName, "Training in Primary Health Care");
    }

    @Test
    public void testCount() {
        ModuleReader mr = new ModuleReader();
        Module testDP;
        String groupId = "";
        ArrayList<String> ids = mr.getDegreeGroupIds();
        
        for ( int i = 0; i < ids.size(); i++ ) {
            groupId = ids.get(i);
            testDP = mr.fromSisu(groupId);
            System.err.println(i + "\n");
            System.err.println(testDP.toString(""));
            int expectedSubUnits = UrlJsonFetcher.getModule(groupId).split("CourseUnitRule").length-1;
            int expectedSubModules = UrlJsonFetcher.getModule(groupId).split("ModuleRule").length-1;
            assertEquals(testDP.getSubUnits().size(), expectedSubUnits);
            assertEquals(testDP.getSubModules().size(), expectedSubModules);
        }
    }
}
