
package fi.tuni.prog3.sisu;

import java.util.ArrayList;

/**
 * Represents a Module in SISU which contains several sub-modules and units.
 *
 * @author Leo
 * @author Jyri
 * @author Aleksi
 */
public abstract class Module {
    String name;
    String id;
    String groupId;
    
    ArrayList<Module> subModules;
    ArrayList<CourseUnit> subUnits;

    public Module(String name, String id, String groupId) {
        this.name = name;
        this.id = id;
        this.groupId = groupId;
        this.subModules = new ArrayList<>();
        this.subUnits = new ArrayList<>();
    }

    /**
     * Getter for module name
     * @return Name of module
     */
    public String getName() {return name;}

    /**
     * Getter for module id
     * @return id of module
     */
    public String getId() {return id;}

    /**
     * Getter for groupId of module
     * @return groupId of module
     */
    public String getGroupId() {return groupId;}

    /**
     * Getter for sub-modules
     * @return All sub-modules of this module
     */
    public ArrayList<Module> getSubModules() {return subModules;}
    
    /**
     * Getter for sub-units
     * @return All sub-units of this module
     */
    public ArrayList<CourseUnit> getSubUnits() {return subUnits;}
    
    /**
     * Add a module to this module's sub-modules
     * @param sub Sub-module to add
     */
    public void addSubModule(Module sub) {subModules.add(sub);}
    
    /**
     * Add a CourseUnit to this module's sub-units
     * @param sub Sub-unit to add
     */
    public void addSubUnit(CourseUnit sub) {subUnits.add(sub);}
    
    public String toString(String indent) {
        String result = indent + "MODULE: " + getName() + "\n";
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
