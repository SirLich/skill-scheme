package main.java.plugin.sirlich.skills.meta;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class RageSkill extends CooldownSkill{

    //Default getters
    public List<Integer> duration;

    //Defaults
    private Material headBlock;
    private boolean enraged;
    private boolean endRageEarly = false;

    private Sound currentlyEnragedSound = Sound.BLOCK_ANVIL_DESTROY;
    private String currentlyEnragedText = c.red + getName() + c.dgray + "  is already active.";
    private Sound becomeEnragedSound = Sound.ENTITY_COW_DEATH;
    private String becomeEnragedText = c.dgray + "You activate "+ c.green + this.getName();

    private Sound stoppedRagingSound = Sound.BLOCK_FIRE_EXTINGUISH;
    private String stoppedRagingText = c.red + getName() + c.dgray + " has expired.";


    public RageSkill(RpgPlayer rpgPlayer, int level, String id, Material headBlock){
        super(rpgPlayer,level,id);
        this.duration = getYaml(id).getIntegerList("values.duration");
        this.headBlock = headBlock;
        this.enraged = false;
    }

    public void onEnrage(){

    }

    public void onRageExpire(){

    }

    public boolean attemptRage(){
        if(enraged){
            getRpgPlayer().chat(currentlyEnragedText);
            getRpgPlayer().playSound(currentlyEnragedSound);
            return false;
        } else {
            if(isCooldown()){
                return false;
            } else {

                //Set rage
                enraged = true;
                onEnrage();

                //Play media
                getRpgPlayer().chat(becomeEnragedText);
                getRpgPlayer().playSound(becomeEnragedSound);

                //Set Helmet
                getRpgPlayer().getPlayer().getInventory().setHelmet(new ItemStack(headBlock));

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

                }.runTaskLater(SkillScheme.getInstance(), duration.get(getLevel()));
                return true;
            }
        }
    }

    public void endRageEarly(){
        this.endRageEarly = true;
        endRage();
    }

    private void endRage(){
        //Set Raging
        enraged = false;
        onRageExpire();

        //Set Helmet
        getRpgPlayer().getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));

        //Play media
        getRpgPlayer().chat(stoppedRagingText);
        getRpgPlayer().playSound(stoppedRagingSound);

        //Refresh cooldown:
        refreshCooldown();
    }

    public boolean isEnraged() {
        return enraged;
    }

    public void setHeadBlock(Material headBlock) {
        this.headBlock = headBlock;
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
