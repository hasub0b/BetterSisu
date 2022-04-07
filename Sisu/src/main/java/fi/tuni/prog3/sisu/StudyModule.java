
package fi.tuni.prog3.sisu;

import java.util.ArrayList;

/**
 * Extends DegreeProgramme with completion and organizer information
 *
 * @author Leo
 */
public class StudyModule extends DegreeProgramme {
    Boolean complete;
    ArrayList<String> requirements;
    ArrayList<String> organizers;

    public StudyModule(int maxCredits, String name, String id, String groupId, ArrayList<String> organizers) {
        super(maxCredits, name, id, groupId);
        this.organizers = organizers;
        complete = false;
    }
    
    public void completeModule() {
        complete = true;
    }
    
    public void cancelModuleCompletion() {
        complete = false;
    }

    public Boolean isComplete() {
        return complete;
    }

    public ArrayList<String> getRequirements() {
        return requirements;
    }

    public ArrayList<String> getOrganizers() {
        return organizers;
    }
}
