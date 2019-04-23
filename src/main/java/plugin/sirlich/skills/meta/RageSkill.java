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
    private Sound currentlyEnragedSound;
    private String currentlyEnragedText;
    private Sound becomeEnragedSound;
    private String becomeEnragedText;
    private Sound stoppedRagingSound;
    private String stoppedRagingText;


    public RageSkill(RpgPlayer rpgPlayer, int level, String id, Material headBlock){
        super(rpgPlayer,level,id);
        this.duration = getYaml(id).getIntegerList("values.duration");
        this.headBlock = headBlock;
        this.enraged = false;
        this.currentlyEnragedSound = null;
        this.currentlyEnragedText = null;
        this.becomeEnragedSound = null;
        this.becomeEnragedText = null;
        this.stoppedRagingSound = null;
        this.stoppedRagingText = null;
    }

    public void onEnrage(){

    }

    public void onRageExpire(){

    }

    public boolean attemptRage(){
        if(enraged){
            if(currentlyEnragedText != null){
                getRpgPlayer().chat(currentlyEnragedText);
            } else {
                getRpgPlayer().chat(c.red + getName() + c.dgray + "  is already active.");
            }
            if(currentlyEnragedSound != null){
                getRpgPlayer().playSound(currentlyEnragedSound);
            } else {
                getRpgPlayer().playSound(Sound.BLOCK_ANVIL_DESTROY);
            }
            return false;
        } else {
            if(isCooldown()){
                return false;
            } else {

                //Set rage
                enraged = true;
                onEnrage();

                //Play media
                if(becomeEnragedText != null){
                    getRpgPlayer().chat(becomeEnragedText);
                } else {
                    getRpgPlayer().chat(c.dgray + "You activate "+ c.green + this.getName());
                }

                if(becomeEnragedSound != null){
                    getRpgPlayer().playSound(becomeEnragedSound);
                } else {
                    getRpgPlayer().playSound(Sound.ENTITY_COW_DEATH);
                }

                //Set Helmet
                getRpgPlayer().getPlayer().getInventory().setHelmet(new ItemStack(headBlock));

                //Set enraged countdown
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        //Set Raging
                        enraged = false;
                        onRageExpire();

                        //Set Helmet
                        getRpgPlayer().getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));

                        //Play media
                        if(stoppedRagingText != null){
                            getRpgPlayer().chat(stoppedRagingText);
                        } else {
                            getRpgPlayer().chat(c.red + getName() + c.dgray + " has expired.");
                        }

                        if(stoppedRagingSound != null){
                            getRpgPlayer().playSound(stoppedRagingSound);
                        } else {
                            getRpgPlayer().playSound(Sound.BLOCK_FIRE_EXTINGUISH);
                        }

                        //Refresh cooldown:
                        refreshCooldown();
                    }

                }.runTaskLater(SkillScheme.getInstance(), duration.get(getLevel()));
                return true;
            }
        }
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
