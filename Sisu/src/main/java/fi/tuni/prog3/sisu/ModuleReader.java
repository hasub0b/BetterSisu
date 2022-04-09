
package fi.tuni.prog3.sisu;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.TreeMap;

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
        JsonObject rootElement = gsonFromSisu(groupId);
        
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
    
    /**
     * Gathers all sub-modules and sub-units into a TreeMap 
     * where the keys are "module" and "unit" and payloads are ArrayLists of
     * groupIds of each object type respectively.
     * 
     * @param groupId
     * @return 
     */
    public TreeMap<String, ArrayList<String>> getSubGroupIds(String groupId) {
        // Step 1: get JSON array of subs
        JsonObject rootElement = gsonFromSisu(groupId);
        String moduleType = rootElement.get("type").getAsString();
        JsonObject rule = rootElement.get("rule").getAsJsonObject();
        JsonArray rules = new JsonArray();
        
        // Step 2: Look for the actual sub-unit/module array...
        while ( true )  {
            if ( rule.get("type").getAsString().equals("CompositeRule") ) {
                JsonObject subRules = rule.get("rules").getAsJsonArray().get(0).getAsJsonObject();
                if ( !subRules.get("type").getAsString().equals("CourseUnitRule") && 
                        !subRules.get("type").getAsString().equals("ModuleRule") ) {
                    rule = subRules;
                } else {
                    break;
                }
            } else if ( rule.get("type").getAsString().equals("CreditsRule") ) {
                rule = rule.get("rule").getAsJsonObject();
            } else {
                break;
            }
        }
        
        rules = rule.get("rules").getAsJsonArray();
        
        // Step 3: Initialize containers for result
        TreeMap<String, ArrayList<String>> result = new TreeMap<>();
        ArrayList<String> unitGroupIds = new ArrayList<>();
        ArrayList<String> moduleGroupIds = new ArrayList<>();
        
        // Step 4: copy data from rules into containers
        for (var sub : rules) {
            if ( sub.getAsJsonObject().get("type").getAsString().equals("CourseUnitRule") ) {
                unitGroupIds.add(sub.getAsJsonObject().get("courseUnitGroupId").getAsString());
            } else {
                moduleGroupIds.add(sub.getAsJsonObject().get("moduleGroupId").getAsString());
            }
        }
        
        result.put("unit", unitGroupIds);
        result.put("module", moduleGroupIds);
        return result;
    }
    
    /**
     * Gets a GSON object from a SISU module with groupId
     * @param groupId groupId of module to get
     * @return A GSON format object of module data
     */
    private JsonObject gsonFromSisu(String groupId) {
        Gson gson = new Gson();
        String jsonString = UrlJsonFetcher.getModule(groupId);
        JsonReader jreader = new JsonReader(new StringReader(jsonString));
        jreader.setLenient(true);
        return gson.fromJson(jreader, JsonObject.class);
    }
}
