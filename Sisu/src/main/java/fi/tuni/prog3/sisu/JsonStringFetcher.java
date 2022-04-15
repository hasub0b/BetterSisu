
package fi.tuni.prog3.sisu;

/**
 * Interface for mapping groupIds to Module and CourseUnit JSON data
 *
 * @author leoma
 */
public interface JsonStringFetcher {
    public String getModule(String groupId);
    public String getCourseUnit(String groupId);
}
