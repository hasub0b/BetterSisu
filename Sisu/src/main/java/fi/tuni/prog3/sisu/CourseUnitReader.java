
package fi.tuni.prog3.sisu;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import java.util.TreeMap;

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
        String enName = getEnAttribute(rootElement, "name");
        
        // code
        String code = "code unavailable";
        JsonElement codeElement = rootElement.get("code");
        if ( !codeElement.isJsonNull() ) {
            code = codeElement.getAsString();
        }
        
        // credits
        int minCredits = getMinMaxCredits(rootElement).get("min");
        int maxCredits = getMinMaxCredits(rootElement).get("max");
        
        // course id
        JsonElement idElement = rootElement.get("id");
        String id = idElement.getAsString();
        
        // initialize result
        CourseUnit result = new CourseUnit(id,groupId,enName,code,minCredits,maxCredits);

        // addtl info
        result.setContent(getEnAttribute(rootElement, "content"));
        result.setPrerequisite(getEnAttribute(rootElement, "prerequisites"));
        result.setOutcome(getEnAttribute(rootElement, "outcomes"));
        result.setMaterial(getEnAttribute(rootElement, "learningMaterial"));
        
        return result;
    }
    
    /**
     * Get an English attribute from JsonObject.
     * If English attribute is unavailable, return Finnish attribute.
     * If no attribute is available, return null.
     * @param rootElement Object to get element from
     * @param attName Attribute name
     * @return Attribute value if it exists, else null
     */
    private String getEnAttribute(JsonObject rootElement, String attName) {
        String result = null;
        
        if ( rootElement.has(attName) ) {
            if (!rootElement.get(attName).isJsonNull()) {
                JsonObject contentLangObj = rootElement.get(attName).getAsJsonObject();
                if ( contentLangObj.has("en") ) {
                    result = contentLangObj.get("en").getAsString();
                } else {
                    result = contentLangObj.get("fi").getAsString();
                }
            }
        }
        
        return result;
    }
    
    private TreeMap<String, Integer> getMinMaxCredits(JsonObject rootElement) {
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
        
        TreeMap<String, Integer> result = new TreeMap<>();
        result.put("min", minCredits);
        result.put("max", maxCredits);
        
        return result;
    }
}