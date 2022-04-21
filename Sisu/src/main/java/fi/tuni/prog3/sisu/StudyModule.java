
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
    
    @Override
    public String toString(String indent) {
        String result = String.format("%sMODULE: %s Organizers (%d)", 
                indent, getName(), getOrganizers().size());
        
        if ( !getOrganizers().isEmpty() ) {
            result += String.format(": %s et al.\n", getOrganizers().get(0));
        } else {
            result += "\n";
        }
        
        // Increase indent
        indent += "   ";
        
        if ( !getSubModules().isEmpty() ) {
            result += indent + "SUB-MODULES " + "(" + getSubModules().size() + "):\n";
            for ( Module sub : getSubModules() ) {
                result += sub.toString(indent);
            }
        }
        if ( !getSubUnits().isEmpty() ) {
            result += indent + "SUB-UNITS " + "(" + getSubUnits().size() + "):\n";
            for ( CourseUnit sub : getSubUnits() ) {
                result += indent + "   " + sub.toString() + "\n";
            }
        }
        
        return result;
    }
}
