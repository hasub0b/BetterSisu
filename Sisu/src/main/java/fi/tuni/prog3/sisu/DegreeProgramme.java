
package fi.tuni.prog3.sisu;

/**
 * Extends module, adds maximum credits functionality.
 *
 * @author leoma
 */
public class DegreeProgramme extends Module {
    int maxCredits;



    int currentCredits;

    public DegreeProgramme(int maxCredits, String name, String id, String groupId) {
        super(name, id, groupId);
        this.maxCredits = maxCredits;
        this.currentCredits = 0;
    }

    
    public int getMaxCredits() {
        return maxCredits;
    };

    public int getCurrentCredits() { return currentCredits; }

    public void addCredits(int credits){
        currentCredits += credits;
    }

    public void removeCredits(int credits){
        currentCredits -= credits;

        // Shouldn't happen but in a case something went wrong
        if (currentCredits < 0){
            currentCredits = 0;
        }
    }

    @Override
    public String toString(){
        return getName() + " " + getCurrentCredits() +" / " + getMaxCredits() + "cr";
    }
}
