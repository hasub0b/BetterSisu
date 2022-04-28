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


    // boolean to determine if the course is selected on the TreeView
    private boolean selected = false;

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
     * @return String Course ID
     */
    public String getId() {return id;}

    /**
     * Returns GroupID of this course
     *
     * @return String Course GroupID
     */
    public String getCourseUnitGroupId() {return courseUnitGroupId;}

    /**
     * Returns name of this course
     *
     * @return String Course name
     */
    public String getName() {return name;}

    /**
     * Returns code of this course
     *
     * @return String Course code
     */
    public String getCode() {return code;}

    /**
     * Returns minimum credits of this course
     *
     * @return int Minimum credits
     */
    public int getMinCredits() {return minCredits;}

    /**
     * Returns maximum credits of this course
     *
     * @return int Maximum credits
     */
    public int getMaxCredits() {return maxCredits;}

    // addtl info
    /**
     * Returns content of this course
     *
     * @return String Course content
     */
    public String getContent() {return content;}

    /**
     * Returns prerequisite of this course
     *
     * @return String Course prerequisite
     */
    public String getPrerequisite() {return prerequisite;}

    /**
     * Returns outcome of this course
     *
     * @return String Course outcome
     */
    public String getOutcome() {return outcome;}

    /**
     * Returns material of this course
     *
     * @return String Course material
     */
    public String getMaterial() {return material;}

    /**
     * Set course content
     *
     * @param content content of the course
     */
    public void setContent(String content) {this.content = content;}

    /**
     * Set course prerequisite
     *
     * @param prereq prerequisite of the course
     */
    public void setPrerequisite(String prereq) {this.prerequisite = prereq;}

    /**
     * Set course outcome
     *
     * @param outcome outcome of the course
     */
    public void setOutcome(String outcome) {this.outcome = outcome;}

    /**
     * Set course material
     *
     * @param material material of the course
     */
    public void setMaterial(String material) {this.material = material;}

    /**
     * Returns boolean value whether course content has been set or not
     *
     * @return Boolean
     */
    public Boolean hasContent() {return (content != null);}

    /**
     * Returns boolean value whether course prerequisite has been set or not
     *
     * @return Boolean
     */
    public Boolean hasPrerequisite() {return (prerequisite != null);}

    /**
     * Returns boolean value whether course outcome has been set or not
     *
     * @return Boolean
     */
    public Boolean hasOutcome() {return (outcome != null);}

    /**
     * Returns boolean value whether course material has been set or not
     *
     * @return Boolean
     */
    public Boolean hasMaterial() {return (material != null);}

    /**
     * Returns boolean value whether course has been selected or not
     *
     * @return Boolean
     */
    public boolean getSelected(){
        return selected;
    }

    /**
     * Set course to be selected or not
     *
     * @param bool Boolean true: course is selected, false: course is not selected
     */
    public void setSelected(boolean bool){
        selected = bool;
    }

    /**
     * Formats a course as "Course: courseName"
     *
     * @return String representation of this course
     */
    @Override
    public String toString() {
        return getName() + " "+ getMaxCredits() + "cr";
    }
}


