
package fi.tuni.prog3.sisu;

/**
 * Extends module, adds maximum credits functionality.
 *
 * @author leoma
 */
public class DegreeProgramme extends Module {
    int maxCredits;

    public DegreeProgramme(int maxCredits, String name, String id, String groupId) {
        super(name, id, groupId);
        this.maxCredits = maxCredits;
    }
    
    public int getMaxCredits() {
        return maxCredits;
    };
}
