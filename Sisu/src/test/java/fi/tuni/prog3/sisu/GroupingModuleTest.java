package fi.tuni.prog3.sisu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Jyri
 */
public class GroupingModuleTest {

    private GroupingModule testModule;
    private final String expectedName = "A Module";
    private final String expectedID = "Some ID";
    private final String expectedGroupID = "Group ID here";

    public GroupingModuleTest() {
    }

    @Test
    public void testConstructor() {
        testModule = new GroupingModule(expectedName, expectedID, expectedGroupID);

        GroupingModule anotherModule
                = new GroupingModule(
                        "Different name",
                        "Different ID",
                        "Different Group ID"
                );

        assertAll(
                () -> assertNotEquals(testModule.getName(), anotherModule.getName()),
                () -> assertNotEquals(testModule.getId(), anotherModule.getId()),
                () -> assertNotEquals(testModule.getGroupId(), anotherModule.getGroupId())
        );
    }

    @Test
    public void testGetters() {
        testModule = new GroupingModule(expectedName, expectedID, expectedGroupID);

        assertAll(
                () -> assertEquals(expectedName, testModule.getName()),
                () -> assertEquals(expectedID, testModule.getId()),
                () -> assertEquals(expectedGroupID, testModule.getGroupId())
        );
    }
}
