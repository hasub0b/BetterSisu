
package fi.tuni.prog3.sisu;

/**
 * Extends module, adds maximum credits functionality.
 *
 * @author leoma
 */
public class DegreeProgramme extends Module {
    int maxCredits;

    /**
     * 
     * @param maxCredits maximum amount of credits attainable
     * @param name name of the degree programme
     * @param id id of the degree programme
     * @param groupId groupId of the degree programme
     */
    public DegreeProgramme(int maxCredits, String name, String id, String groupId) {
        super(name, id, groupId);
        this.maxCredits = maxCredits;
    }
    
    public int getMaxCredits() {
        return maxCredits;
    };
}
