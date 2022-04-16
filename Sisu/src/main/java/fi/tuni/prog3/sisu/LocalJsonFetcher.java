
package fi.tuni.prog3.sisu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author leoma
 */
public class LocalJsonFetcher implements JsonStringFetcher {

    @Override
    public String getModule(String groupId) {
        try {
            return getFromLocation("../json/modules/" + groupId + ".json");
        } catch (FileNotFoundException e) {
            System.err.format("Module file not found: " + groupId);
        } catch (IOException e) {
            System.err.format("error in Module file: " + groupId);
        }
        return null;
    }

    @Override
    public String getCourseUnit(String groupId) {
        try {
            return getFromLocation("../json/courseunits/" + groupId + ".json");
        } catch (FileNotFoundException e) {
            System.err.format("CourseUnit file not found: " + groupId);
        } catch (IOException e) {
            System.err.format("error in CourseUnit file: " + groupId);
        }
        return null;
    }
    
    private String getFromLocation(String fileLocation) throws FileNotFoundException, IOException {
        BufferedReader br;
        String jsonString = "";
        br = new BufferedReader(new FileReader(fileLocation));
        
        // Read lines into jsonString
        String line = br.readLine();
        while (line != null) {
            jsonString += line;
            line = br.readLine();
        }
        
        return jsonString;
    }
}
