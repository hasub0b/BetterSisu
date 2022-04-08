
package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Handle getting JSON strings from URL strings
 *
 * @author Leo
 */
public class UrlJsonFetcher {
    /**
     * Get a list of all degrees from SISU
     * @return JSON format string of all courses in SISU
     */
    public static String getDegreeList() {
        return getStringFromUrl("https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000");
    }
    
    /**
     * Get module JSON data by groupId
     * @return JSON format string of module with groupId from SISU
     */
    public static String getModule(String groupId) {
        return getStringFromUrl("https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId="+groupId+"&universityId=tuni-university-root-id");
    }
    
    /**
     * Get course JSON data by groupId
     * @return JSON format string of course with groupId from SISU
     */
    public static String getCourseUnit(String groupId) {
        return getStringFromUrl("https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId="+groupId+"&universityId=tuni-university-root-id");
    }
    
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
