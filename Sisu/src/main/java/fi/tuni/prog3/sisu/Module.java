
package fi.tuni.prog3.sisu;

import java.util.ArrayList;

/**
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

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    public ArrayList<Module> getSubModules() {
        return subModules;
    }
    
    public void addSubModule(Module sub) {
        subModules.add(sub);
    }
    
    public void addSubUnit(CourseUnit sub) {
        subUnits.add(sub);
    }
}
