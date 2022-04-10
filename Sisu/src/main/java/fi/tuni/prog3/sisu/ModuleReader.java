
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
     * Get a TreeMap of degree names to groupIds
     * @return TreeMap where keys are degree names and payloads are groupIds
     */
    public TreeMap<String, String> getDegreeGroupIdPairs() {
        TreeMap<String, String> result = new TreeMap<>();
        String name;
        
        for ( String groupId : getDegreeGroupIds() ) {
            JsonObject degreeObject = gsonFromSisu(groupId).get("name").getAsJsonObject();
            
            if ( degreeObject.has("en") ) {
                name = degreeObject.get("en").getAsString();
            } else {
                name = degreeObject.get("fi").getAsString();
            }
            
            result.put(name, groupId);
        }
        
        return result;
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
     * Gathers all sub-module and sub-unit groupIds into a TreeMap 
     * where the keys are "module" and "unit" and payloads are ArrayLists of
     * groupIds of each object type respectively.
     * 
     * @param groupId groupId of module to fetch sub-modules/units of
     * @return Map of types to sub-modules/units
     */
    public TreeMap<String, ArrayList<String>> getSubGroupIds(String groupId) {
        JsonObject rootElement = gsonFromSisu(groupId);
        JsonObject rule = rootElement.get("rule").getAsJsonObject();
        
        return extractSubGroupIds(rule);
    }
    
    /**
     * Cut through layers of rules in the SISU data structure
     * @param givenRule rule to gather sub-rules of
     * @return JsonArray of ModuleRule and CourseUnitRule JSON objects
     */
    private JsonArray getSubRuleArray(JsonObject givenRule) {
        JsonObject rule = givenRule;
        JsonArray rules = new JsonArray();
        
        // If the given rule is already a module or unit, return it in an array
        String ruleType = rule.get("type").getAsString();
        if ( ruleType.equals("CourseUnitRule") || ruleType.equals("ModuleRule") ) {
            rules.add(rule);
            return rules;
        }
        
        // Repeating structure to navigate through rules
        while ( true )  {
            ruleType = rule.get("type").getAsString();
            // Handle CompositeRule
            if ( ruleType.equals("CompositeRule") ) {
                JsonArray subRules = rule.get("rules").getAsJsonArray();
                
                // If this CompositeRule doesn't have subRules, break
                if ( subRules.isEmpty() ) {break;}  
                
                // Check if this CompositeRule has inner composite/credits-rules
                Boolean hasDeeperRules = false;
                for ( var aRule : subRules ) {
                    String aRuleType = aRule.getAsJsonObject().get("type").getAsString();
                    if (aRuleType.equals("CompositeRule") || aRuleType.equals("CreditsRule")) {
                        hasDeeperRules = true;
                    }
                }
                // If this Composite contains only Modules and Units, break
                if ( !hasDeeperRules ) {break;}

                // compile all deeper rules under one composite
                JsonArray subRuleArray = new JsonArray();
                for ( var subRule : subRules ) {
                    subRuleArray.addAll(getSubRuleArray(subRule.getAsJsonObject()));
                }
                // Replace sub-rules with gathered rules
                rule.remove("rules");
                rule.add("rules", subRuleArray);
            // Handle CreditsRule
            } else if ( ruleType.equals("CreditsRule") ) {
                rule = rule.get("rule").getAsJsonObject();
            } else {break;} //Break if current rule isn't composite or credits
        }
        
        // account for empty modules
        if ( rule.get("rules") != null ) {
            rules = rule.get("rules").getAsJsonArray();
        }
        return rules;
    }
    
    /**
     * Extract the sub-modules and sub-units from a rule JSON object. 
     * Works in conjunction with getSubRuleArray
     * @param rule JSON format rule to extract data from
     * @return Map where keys are "module" and "unit" and payloads are arrays of
     * sub-module/sub-unit groupIds respectively
     */
    private TreeMap<String, ArrayList<String>> extractSubGroupIds(JsonObject rule) {
        JsonArray rules = getSubRuleArray(rule);
 
        TreeMap<String, ArrayList<String>> result = new TreeMap<>();
        ArrayList<String> unitGroupIds = new ArrayList<>();
        ArrayList<String> moduleGroupIds = new ArrayList<>();
        
        for (var sub : rules) {
            String subType = sub.getAsJsonObject().get("type").getAsString();
            
            if ( subType.equals("CourseUnitRule") ) {
                unitGroupIds.add(sub.getAsJsonObject().get("courseUnitGroupId").getAsString());
                
            } else if (subType.equals("ModuleRule")) {
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
