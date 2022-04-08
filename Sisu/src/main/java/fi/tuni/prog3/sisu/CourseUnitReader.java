
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
    public CourseUnit fromString(String s) {
        Gson gson = new Gson();
        JsonReader jreader = new JsonReader(new StringReader(s));
        jreader.setLenient(true);
        JsonObject rootElement = gson.fromJson(jreader, JsonObject.class);
        
        // names
        JsonElement namesElement = rootElement.get("name");
        JsonElement en_name_element = namesElement.getAsJsonObject().get("en");
        String en_name = en_name_element.getAsString();
        JsonElement fi_name_element = namesElement.getAsJsonObject().get("fi");
        String fi_name = fi_name_element.getAsString();
        
        // code
        JsonElement codeElement = rootElement.get("code");
        String code = codeElement.getAsString();
        
        // groupId
        JsonElement groupIdElement = rootElement.get("groupId");
        String groupId = groupIdElement.getAsString();
        
        // max credits
        JsonElement creditsElement = rootElement.get("credits");
        JsonElement maxCreditElement = creditsElement.getAsJsonObject().get("max");
        int credits = maxCreditElement.getAsInt();
        
        // course id
        JsonElement idElement = rootElement.get("id");
        String id = idElement.getAsString();
        
        return new CourseUnit(id,groupId,en_name,code,credits);
    }
}
