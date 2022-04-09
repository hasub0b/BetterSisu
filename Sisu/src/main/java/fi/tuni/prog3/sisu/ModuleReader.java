
package fi.tuni.prog3.sisu;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Turn JSON into Module objects
 * 
 * @author Leo
 */
public class ModuleReader {
    /**
     * Get a module from SISU using module groupId
     * @param s Degree groupId
     * @return Module of the given groupId
     */
    public Module fromSisu(String groupId) {
        Gson gson = new Gson();
        String jsonString = UrlJsonFetcher.getModule(groupId);
        JsonReader jreader = new JsonReader(new StringReader(jsonString));
        jreader.setLenient(true);
        JsonObject rootElement = gson.fromJson(jreader, JsonObject.class);
        
        String name = rootElement.get("name").getAsJsonObject().get("en").getAsString();
        String id = rootElement.get("id").getAsString();
        String type = rootElement.get("type").getAsString();
        
        // TODO: Add sub-modules and sub-units once they are available!
        if ( type.equals("StudyModule") ) {
            int credits = rootElement.get("targetCredits").getAsJsonObject().get("min").getAsInt();
            ArrayList<String> organizers = new ArrayList<>();
            for (var person : rootElement.get("responsibilityInfos").getAsJsonArray()) {
                organizers.add(person.getAsJsonObject().get("personId").getAsString());
            }
            return new StudyModule(credits, name, id, groupId, organizers);
        } else if (type.equals("DegreeProgramme")) {
            int credits = rootElement.get("targetCredits").getAsJsonObject().get("min").getAsInt();
            return new DegreeProgramme(credits, name, id, groupId);
        } else {
            return new GroupingModule(name, id, groupId);
        }
    }
}
