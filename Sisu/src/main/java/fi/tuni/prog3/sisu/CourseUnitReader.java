
package fi.tuni.prog3.sisu;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import java.io.StringReader;

/**
 * Turn CourseUnit JSON-format string into CourseUnit object
 * 
 * @author Leo
 */
public class CourseUnitReader {
    /**
     * Get CourseUnit of groupId from SISU
     * @param groupId groupId of CourseUnit to get
     * @return CourseUnit object of groupId
     */
    public CourseUnit fromSisu(String groupId) {
        Gson gson = new Gson();
        String jsonString = UrlJsonFetcher.getCourseUnit(groupId);
        JsonReader jreader = new JsonReader(new StringReader(jsonString));
        jreader.setLenient(true);
        JsonObject rootElement = gson.fromJson(jreader, JsonObject.class);
        
        // names
        String fi_name = "Nimi ei ole saatavilla.";
        String en_name = "Name unavailable";
        JsonElement namesElement = rootElement.get("name");
        JsonElement en_name_element = namesElement.getAsJsonObject().get("en");
        if ( en_name_element != null ) {
            en_name = en_name_element.getAsString();
        }
        JsonElement fi_name_element = namesElement.getAsJsonObject().get("fi");
        if ( fi_name_element != null ) {
            fi_name = fi_name_element.getAsString();
        }
        
        // code
        String code = "code unavailable";
        JsonElement codeElement = rootElement.get("code");
        if ( !codeElement.isJsonNull() ) {
            code = codeElement.getAsString();
        }
        
        // credits
        JsonObject creditsObject = rootElement.get("credits").getAsJsonObject();
        JsonElement creditElement = null;
        int maxCredits = 0;
        int minCredits = 0;
        if (creditsObject.has("max")) {
            creditElement = creditsObject.getAsJsonObject().get("max");
            if ( !creditElement.isJsonNull() ) {
                maxCredits = creditElement.getAsInt();
            }
        }   
        if (creditsObject.has("min")) {
            creditElement = creditsObject.getAsJsonObject().get("min");
            if ( !creditElement.isJsonNull() ) {
                minCredits = creditElement.getAsInt();
            }
        } else {
            // if a minimum credits limit hasn't been defined, use max
            minCredits = maxCredits;
        }
        
        // if min credits was defined and max wasn't, use min as max
        if ( minCredits > maxCredits) {
            maxCredits = minCredits;
        }
        
        // course id
        JsonElement idElement = rootElement.get("id");
        String id = idElement.getAsString();
        
        if ( !en_name.equals("Name unavailable") ) {
            return new CourseUnit(id,groupId,en_name,code,minCredits,maxCredits);
        } else {
            return new CourseUnit(id,groupId,fi_name,code,minCredits,maxCredits);
        }
    }
}
