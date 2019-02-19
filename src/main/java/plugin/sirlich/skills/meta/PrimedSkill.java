package main.java.plugin.sirlich.skills.meta;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Sound;

public class PrimedSkill extends CooldownSkill {

    public boolean primed;
    private Sound alreadyPrimedSound;
    private Sound primeSound;
    private String primeText;
    private String alreadyPrimedText;

    public PrimedSkill(RpgPlayer rpgPlayer, int level, String id,String primeText, Sound primeSound, String alreadyPrimedText, Sound alreadyPrimedSound){
        super(rpgPlayer,level,id);
        this.alreadyPrimedText = alreadyPrimedText;
        this.alreadyPrimedSound = alreadyPrimedSound;
        this.primeText = primeText;
        this.primeSound = primeSound;

    }
    public PrimedSkill(RpgPlayer rpgPlayer, int level, String id){
        super(rpgPlayer,level,id);
    }

    public void attemptPrime(){
        if(primed){
            if(alreadyPrimedText != null){
                getRpgPlayer().chat(alreadyPrimedText);
            } else {
                getRpgPlayer().chat(c.red + getName() + c.dgray + "  is already primed.");
            }
            if(alreadyPrimedSound != null){
                getRpgPlayer().playSound(alreadyPrimedSound);
            } else {
                getRpgPlayer().playSound(Sound.BLOCK_ANVIL_DESTROY);
            }
        } else {
            if(isCooldown()){
                return;
            } else {
                primed = true;
                if(primeText != null){
                    getRpgPlayer().chat(primeText);
                } else {
                    getRpgPlayer().chat(c.dgray + "You ready your "+ c.daqua + getRpgPlayer().getPlayer().getInventory().getItemInMainHand().getType().toString() + c.dgray + "...");
                }

                if(primeSound != null){
                    getRpgPlayer().playSound(primeSound);
                } else {
                    getRpgPlayer().playSound(Sound.BLOCK_PISTON_EXTEND);
                }
            }
        }
    }

}
