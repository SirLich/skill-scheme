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
    private List<Integer> duration;

    //Defaults
    private Material headBlock;
    private boolean raged;
    private Sound currentlyRagingSound;
    private String currentlyRagingText;
    private Sound becomeRagedSound;
    private String becomeRagedText;
    private Sound stoppedRagingSound;
    private String stoppedRagingText;


    public RageSkill(RpgPlayer rpgPlayer, int level, String id, Material headBlock){
        super(rpgPlayer,level,id);
        this.duration = getYaml(id).getIntegerList("values.duration");
        this.headBlock = headBlock;
        this.raged = false;
        this.currentlyRagingSound = null;
        this.currentlyRagingText = null;
        this.becomeRagedSound = null;
        this.becomeRagedText = null;
        this.stoppedRagingSound = null;
        this.stoppedRagingText = null;
    }

    public void attemptRage(){
        if(raged){
            if(currentlyRagingText != null){
                getRpgPlayer().chat(currentlyRagingText);
            } else {
                getRpgPlayer().chat(c.red + getName() + c.dgray + "  is already active.");
            }
            if(currentlyRagingSound != null){
                getRpgPlayer().playSound(currentlyRagingSound);
            } else {
                getRpgPlayer().playSound(Sound.BLOCK_ANVIL_DESTROY);
            }
        } else {
            if(isCooldown()){
                return;
            } else {

                //Set rage
                raged = true;

                //Play media
                if(becomeRagedText != null){
                    getRpgPlayer().chat(becomeRagedText);
                } else {
                    getRpgPlayer().chat(c.dgray + "You activate "+ c.green + this.getName());
                }

                if(becomeRagedSound != null){
                    getRpgPlayer().playSound(becomeRagedSound);
                } else {
                    getRpgPlayer().playSound(Sound.ENTITY_COW_DEATH);
                }

                //Set Helmet
                getRpgPlayer().getPlayer().getInventory().setHelmet(new ItemStack(headBlock));

                //Set raged countdown
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        //Set Raging
                        raged = false;

                        //Set Helmet
                        getRpgPlayer().getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));

                        //Play media
                        if(stoppedRagingText != null){
                            getRpgPlayer().chat(stoppedRagingText);
                        } else {
                            getRpgPlayer().chat(c.green + getName() + c.dgray + " has expired.");
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
            }
        }
    }

    public void setHeadBlock(Material headBlock) {
        this.headBlock = headBlock;
    }

    public void setCurrentlyRagingSound(Sound currentlyRagingSound) {
        this.currentlyRagingSound = currentlyRagingSound;
    }

    public void setCurrentlyRagingText(String currentlyRagingText) {
        this.currentlyRagingText = currentlyRagingText;
    }

    public void setBecomeRagedSound(Sound becomeRagedSound) {
        this.becomeRagedSound = becomeRagedSound;
    }

    public void setBecomeRagedText(String becomeRagedText) {
        this.becomeRagedText = becomeRagedText;
    }

    public void setStoppedRagingSound(Sound stoppedRagingSound) {
        this.stoppedRagingSound = stoppedRagingSound;
    }

    public void setStoppedRagingText(String stoppedRagingText) {
        this.stoppedRagingText = stoppedRagingText;
    }
}
