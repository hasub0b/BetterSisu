
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
            // Fuck it, seems like not all StudyModules even have organizers...
            if ( !rootElement.get("responsibilityInfos").isJsonNull() ) {
                for (var person : rootElement.get("responsibilityInfos").getAsJsonArray()) {
                    if ( !person.getAsJsonObject().get("personId").isJsonNull() ) {
                        organizers.add(person.getAsJsonObject().get("personId").getAsString());
                    }
                }
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
     * Get a list of every available degree's groupIds
     * @return ArrayList of all degree groupIds
     */
    public ArrayList<String> getDegreeGroupIds() {
        Gson gson = new Gson();
        String jsonString = UrlJsonFetcher.getDegreeList();
        JsonReader jreader = new JsonReader(new StringReader(jsonString));
        jreader.setLenient(true);
        JsonObject rootObject = gson.fromJson(jreader, JsonObject.class);
        JsonArray degreeArray = rootObject.get("searchResults").getAsJsonArray();
        
        ArrayList<String> result = new ArrayList<>();
        for ( var deg : degreeArray ) {
            result.add(deg.getAsJsonObject().get("groupId").getAsString());
        }
        return result;
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
        JsonObject rootElement = gsonFromSisu(groupId);
        String moduleType = rootElement.get("type").getAsString();
        JsonObject rule = rootElement.get("rule").getAsJsonObject();
        
        return extractSubGroupIds(rule);
    }
    
    /**
     * Cut through all the layers of rules in the SISU data structure
     * @param givenRule rule to penetrate
     * @return JsonArray of the actual contents of rule
     */
    private JsonArray penetrateRules(JsonObject givenRule) {
        JsonObject rule = givenRule;
        JsonArray rules = new JsonArray();
        
        while ( true )  {
            String ruleType = rule.get("type").getAsString();
            
            if ( ruleType.equals("CompositeRule") ) {
                // "If the rule isn't empty"
                if ( !rule.get("rules").getAsJsonArray().isEmpty() ) {
                    JsonObject subRules = rule.get("rules").getAsJsonArray().get(0).getAsJsonObject();
                    // "If subRules isn't an inner array of subs"
                    if ( !subRules.get("type").getAsString().equals("CourseUnitRule") &&
                            !subRules.get("type").getAsString().equals("ModuleRule") ) {
                        rule = subRules;
                    } else {break;}
                } else {break;}
            } else if ( ruleType.equals("CreditsRule") ) {
                rule = rule.get("rule").getAsJsonObject();
            } else {break;}
        }
        
        // account for empty modules
        if ( rule.get("rules") != null ) {
            rules = rule.get("rules").getAsJsonArray();
        }
        return rules;
    }
    
    /**
     * Extract the sub-modules and sub-units from a rule JSON object. 
     * Works in conjunction with penetrateRules
     * @param rule JSON format rule to extract data from
     * @return Map of types to sub-modules/units
     */
    private TreeMap<String, ArrayList<String>> extractSubGroupIds(JsonObject rule) {
        JsonArray rules = penetrateRules(rule);
 
        TreeMap<String, ArrayList<String>> result = new TreeMap<>();
        ArrayList<String> unitGroupIds = new ArrayList<>();
        ArrayList<String> moduleGroupIds = new ArrayList<>();
        result.put("unit", unitGroupIds);
        result.put("module", moduleGroupIds);
        
        for (var sub : rules) {
            String subType = sub.getAsJsonObject().get("type").getAsString();
            
            if ( subType.equals("CourseUnitRule") ) {
                unitGroupIds.add(sub.getAsJsonObject().get("courseUnitGroupId").getAsString());
                
            } else if (subType.equals("ModuleRule")) {
                moduleGroupIds.add(sub.getAsJsonObject().get("moduleGroupId").getAsString());
                
            } else if ( subType.equals("CreditsRule") || subType.equals("CompositeRule") ) {
                // if this sub's type is a container, call this method recursively
                unitGroupIds.addAll(extractSubGroupIds(sub.getAsJsonObject()).get("unit"));
                moduleGroupIds.addAll(extractSubGroupIds(sub.getAsJsonObject()).get("module"));
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
