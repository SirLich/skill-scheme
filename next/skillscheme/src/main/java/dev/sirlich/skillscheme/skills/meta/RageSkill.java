package dev.sirlich.skillscheme.skills.meta;

import dev.sirlich.skillscheme.SkillScheme;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.utilities.Color;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * A RageSkill is a skill with a , and a ra
 */
public class RageSkill extends CooldownSkill{

    //Defaults
    private boolean enraged;
    private boolean endRageEarly = false;

    private Sound currentlyEnragedSound = Sound.BLOCK_ANVIL_BREAK;
    private String currentlyEnragedText = Color.red + getName() + Color.dgray + "  is already active.";
    private Sound becomeEnragedSound = Sound.ENTITY_COW_HURT;
    private String becomeEnragedText = Color.dgray + "You activate "+ Color.green + this.getName();

    private Sound stoppedRagingSound = Sound.BLOCK_FIRE_EXTINGUISH;
    private String stoppedRagingText = Color.red + getName() + Color.dgray + " has expired.";


    public RageSkill(RpgPlayer rpgPlayer, int level, String id){
        super(rpgPlayer,level,id);
        this.enraged = false;
    }

    public void onEnrage(){

    }

    public void onRageExpire(){

    }

    public boolean attemptRage(){
        if(isSilenced()){return false;}
        if(enraged){
            getRpgPlayer().tell(currentlyEnragedText);
            getRpgPlayer().playSound(currentlyEnragedSound);
            return false;
        } else {
            if(skillCheck()){
                return false;
            } else {

                //Set rage
                enraged = true;
                onEnrage();

                //Play media
                getRpgPlayer().tell(becomeEnragedText);
                getRpgPlayer().playSound(becomeEnragedSound);

                //Set enraged countdown
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        if(endRageEarly){
                            endRageEarly = false;
                        } else {
                            endRage();
                        }
                    }

                }.runTaskLater(SkillScheme.getInstance(), data.getInt("duration"));
                return true;
            }
        }
    }

    public void endRageEarly(){
        if(isEnraged()){
            this.endRageEarly = true;
            endRage();
        }
    }

    private void endRage(){
        //Set Raging
        enraged = false;
        onRageExpire();

        //Play media
        getRpgPlayer().tell(stoppedRagingText);
        getRpgPlayer().playSound(stoppedRagingSound);

        //Refresh cooldown:
        refreshCooldown();
    }

    public boolean isEnraged() {
        return enraged;
    }

    public void setCurrentlyEnragedSound(Sound currentlyEnragedSound) {
        this.currentlyEnragedSound = currentlyEnragedSound;
    }

    public void setCurrentlyEnragedText(String currentlyEnragedText) {
        this.currentlyEnragedText = currentlyEnragedText;
    }

    public void setBecomeEnragedSound(Sound becomeEnragedSound) {
        this.becomeEnragedSound = becomeEnragedSound;
    }

    public void setBecomeEnragedText(String becomeEnragedText) {
        this.becomeEnragedText = becomeEnragedText;
    }

    public void setStoppedRagingSound(Sound stoppedRagingSound) {
        this.stoppedRagingSound = stoppedRagingSound;
    }

    public void setStoppedRagingText(String stoppedRagingText) {
        this.stoppedRagingText = stoppedRagingText;
    }
}
