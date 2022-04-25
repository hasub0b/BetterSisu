
package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * JsonStringFetcher for remote SISU data
 *
 * @author Leo
 */
public class UrlJsonFetcher implements JsonStringFetcher {
    /**
     * Get a list of all degrees from SISU
     * @return JSON format string of all courses in SISU
     */
    public static String getDegreeList() {
        String jsonString = getStringFromUrl("https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000");
        
        return jsonString;
    }
    
    /**
     * Get module JSON data by groupId
     * @param groupId groupId of the module
     * @return JSON format string of module with groupId from SISU
     */
    public String getModule(String groupId) {
        String jsonString = getStringFromUrl("https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId="+groupId+"&universityId=tuni-university-root-id");
        jsonString = jsonString.substring(1,jsonString.length()-1);
        return jsonString;
    }
    
    /**
     * Get course JSON data by groupId
     * @param groupId groupId of the course
     * @return JSON format string of course with groupId from SISU
     */
    public String getCourseUnit(String groupId) {
        String jsonString = getStringFromUrl("https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId="+groupId+"&universityId=tuni-university-root-id");
        jsonString = jsonString.substring(1,jsonString.length()-1);
        return jsonString;
    }
    
    /**
     * Get contents from given URL in string
     * @param url URL to fetch string from
     * @return String of contents from URL
     */
    public static String getStringFromUrl(String url) {
        URL urlObj = null;
        InputStream input;
        String contents;
        
        try {
            urlObj = new URL(url);
            input = urlObj.openStream();
            contents = new String(input.readAllBytes());
            return contents;
        } catch (MalformedURLException e) {
            System.err.println(e);
            return "Invalid URL.";
        } catch (IOException e) {
            System.err.println(e);
            return "Invalud URL.";
        }
    }
}
