package fi.tuni.prog3.sisu;

/**
 *
 * Describes a course
 *
 * @author hasu
 */

public class CourseUnit {

    private String id;
    private String courseUnitGroupId;
    private String name;
    private String code;
    private int minCredits;
    private int maxCredits;
    
    // addtl info
    private String content;
    private String prerequisite;
    private String outcome;
    private String material;

    /**
     * Constructs a CourseUnit
     *
     * @param id Course ID
     * @param groupId Course GroupID
     * @param name Course name
     * @param code Course code
     * @param minCredits Minimum credits
     * @param maxCredits Maximum credits
     */
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

    /**
     * Returns ID of this course
     *
     * @return Course ID
     */
    public String getId() {return id;}

    /**
     * Returns GroupID of this course
     *
     * @return Course GroupID
     */
    public String getCourseUnitGroupId() {return courseUnitGroupId;}

    /**
     * Returns GroupID of this course
     *
     * @return Course GroupID
     */
    public String getName() {return name;}

    /**
     * Returns name of this course
     *
     * @return Course name
     */
    public String getCode() {return code;}

    /**
     * Returns code of this course
     *
     * @return Course code
     */
    public int getMinCredits() {return minCredits;}

    /**
     * Returns minimum credits of this course
     *
     * @return Minimum credits
     */
    public int getMaxCredits() {return maxCredits;}

    /**
     * Returns content of this course
     *
     * @return Course content
     */
    public String getContent() {return content;}

    /**
     * Returns prerequisite of this course
     *
     * @return Course prerequisite
     */
    public String getPrerequisite() {return prerequisite;}

    /**
     * Returns outcome of this course
     *
     * @return Course outcome
     */
    public String getOutcome() {return outcome;}

    /**
     * Returns material of this course
     *
     * @return Course material
     */
    public String getMaterial() {return material;}
    public void setContent(String content) {this.content = content;}
    public void setPrerequisite(String prereq) {this.prerequisite = prereq;}
    public void setOutcome(String outcome) {this.outcome = outcome;}
    public void setMaterial(String material) {this.material = material;}
    public Boolean hasContent() {return (content != null);}
    public Boolean hasPrerequisite() {return (prerequisite != null);}
    public Boolean hasOutcome() {return (outcome != null);}
    public Boolean hasMaterial() {return (material != null);}


    /**
     * Formats a course as "Course: courseName"
     *
     * @return String representation of this course
     */
    @Override
    public String toString() {
        return "COURSE: " + getName();
    }
}


