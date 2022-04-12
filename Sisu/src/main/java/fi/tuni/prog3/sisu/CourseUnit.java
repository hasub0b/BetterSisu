package fi.tuni.prog3.sisu;

/**
 *
 * Describes a course
 *
 * @author hasu
 */

public class CourseUnit {

    String id;
    String courseUnitGroupId;
    String name;
    String code;
    int minCredits;
    int maxCredits;
    
    // addtl info
    String content;
    String prerequisite;
    String outcome;
    String material;

    public CourseUnit(String id, String groupId, String name, String code, int minCredits, int maxCredits) {
        this.id = id;
        this.courseUnitGroupId = groupId;
        this.name = name;
        this.code = code;
        this.minCredits = minCredits;
        this.maxCredits = maxCredits;
        this.content = null;
        this.prerequisite = null;
        this.outcome = null;
        this.material = null;
    }

    public String getId() {return id;}

    public String getCourseUnitGroupId() {return courseUnitGroupId;}

    public String getName() {return name;}

    public String getCode() {return code;}

    public int getMinCredits() {return minCredits;}
    
    public int getMaxCredits() {return maxCredits;}

    // addtl info
    public String getContent() {return content;}
    public String getPrerequisite() {return prerequisite;}
    public String getOutcome() {return outcome;}
    public String getMaterial() {return material;}
    public void setContent(String content) {this.content = content;}
    public void setPrerequisite(String prereq) {this.prerequisite = prereq;}
    public void setOutcome(String outcome) {this.outcome = outcome;}
    public void setMaterial(String material) {this.material = material;}
    public Boolean hasContent() {return content == null;}
    public Boolean hasPrerequisite() {return prerequisite == null;}
    public Boolean hasOutcome() {return outcome == null;}
    public Boolean hasMaterial() {return material == null;}
    
    @Override
    public String toString() {
        return "COURSE: " + getName();
    }
}


