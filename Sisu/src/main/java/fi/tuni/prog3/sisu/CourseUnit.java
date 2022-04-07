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
    int credits;

    public CourseUnit(String id, String groupId, String name, String code, int credits) {
        this.id = id;
        this.courseUnitGroupId = groupId;
        this.name = name;
        this.code = code;
        this.credits = credits;
    }

    public String getId() {return id;}

    public String getCourseUnitGroupId() {return courseUnitGroupId;}

    public String getName() {return name;}

    public String getCode() {return code;}

    public int getCredits() {return credits;}

}


