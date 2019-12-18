package plugin.sirlich.skills.meta;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.utilities.Color;
import org.bukkit.Sound;

public class PrimedSkill extends CooldownSkill {

    public boolean primed;

    private Sound alreadyPrimedSound = Sound.BLOCK_COMPARATOR_CLICK;
    private Sound primeSound = Sound.BLOCK_PISTON_EXTEND;
    private String primeText = Color.green + getName() + Color.dgray + " is now primed.";
    private String alreadyPrimedText = Color.red + getName() + Color.dgray + " is already primed.";


    public PrimedSkill(RpgPlayer rpgPlayer, int level, String id){
        super(rpgPlayer,level,id);
    }

    public void attemptPrime(){
        if(isSilenced()){return;};
        if(primed){
            getRpgPlayer().tell(alreadyPrimedText);
            getRpgPlayer().playSound(alreadyPrimedSound);
        } else {
            if(skillCheck()){
                return;
            } else {
                primed = true;
                getRpgPlayer().tell(primeText);
                getRpgPlayer().playSound(primeSound);
            }
        }
    }

    public Sound getAlreadyPrimedSound() {
        return alreadyPrimedSound;
    }

    public void setAlreadyPrimedSound(Sound alreadyPrimedSound) {
        this.alreadyPrimedSound = alreadyPrimedSound;
    }

    public Sound getPrimeSound() {
        return primeSound;
    }

    public void setPrimeSound(Sound primeSound) {
        this.primeSound = primeSound;
    }

    public String getPrimeText() {
        return primeText;
    }

    public void setPrimeText(String primeText) {
        this.primeText = primeText;
    }

    public String getAlreadyPrimedText() {
        return alreadyPrimedText;
    }

    public void setAlreadyPrimedText(String alreadyPrimedText) {
        this.alreadyPrimedText = alreadyPrimedText;
    }
}
