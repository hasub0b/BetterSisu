
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
     * @param groupId Degree groupId
     * @return Module of the given groupId
     */
    public Module fromSisu(String groupId) {
        JsonObject rootElement = gsonFromSisu(groupId);
        
        JsonObject nameObject = rootElement.get("name").getAsJsonObject();
        String name;
        // account for not all courses being in english
        if ( nameObject.has("en") ) {
            name = nameObject.get("en").getAsString();
        } else {
            name = nameObject.get("fi").getAsString();
        }
        
        String id = rootElement.get("id").getAsString();
        String type = rootElement.get("type").getAsString();
        
        if ( type.equals("StudyModule") ) {
            int credits = rootElement.get("targetCredits").getAsJsonObject().get("min").getAsInt();
            ArrayList<String> organizers = new ArrayList<>();
            for (var person : rootElement.get("responsibilityInfos").getAsJsonArray()) {
                organizers.add(person.getAsJsonObject().get("personId").getAsString());
            }
            StudyModule result = new StudyModule(credits, name, id, groupId, organizers);
            gatherSubs(result);
            return result;
        } else if (type.equals("DegreeProgramme")) {
            int credits = rootElement.get("targetCredits").getAsJsonObject().get("min").getAsInt();
            DegreeProgramme result = new DegreeProgramme(credits, name, id, groupId);
            gatherSubs(result);
            return result;
        } else {
            GroupingModule result = new GroupingModule(name, id, groupId);
            gatherSubs(result);
            return result;
        }
    }
    
    /**
     * Gathers all sub-modules and sub-units into a TreeMap 
     * where the keys are "module" and "unit" and payloads are ArrayLists of
     * groupIds of each object type respectively.
     * 
     * @param groupId groupId of module to fetch sub-modules/units of
     * @return Map of types to sub-modules/units
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
    
    /**
     * Gather all rootModule's subs-modules and sub-units into their respective containers
     * @param rootModule Module to gather subs of.
     */
    private void gatherSubs(Module rootModule) {
        // Fetch study-module's sub-modules
        for (String subGroupId : getSubGroupIds(rootModule.getGroupId()).get("module")) {
            rootModule.addSubModule(this.fromSisu(subGroupId));
        }
        // Fetch study-module's sub-units
        for (String subGroupId : getSubGroupIds(rootModule.getGroupId()).get("unit")) {
            CourseUnitReader cur = new CourseUnitReader();
            rootModule.addSubUnit(cur.fromSisu(subGroupId));
        }
    }
}
