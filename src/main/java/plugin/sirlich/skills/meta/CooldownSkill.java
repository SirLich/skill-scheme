package main.java.plugin.sirlich.skills.meta;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CooldownSkill extends Skill
{
    //List of all cooldown values. This is used for Inventory Displays.
    private List<Integer> cooldownValues;

    //Current cooldown by level
    private int cooldownValue;

    private boolean cooldown;

    private Sound cooldownSound = Sound.BLOCK_COMPARATOR_CLICK;
    private Sound rechargeSound = Sound.BLOCK_ENDERCHEST_OPEN;
    private String cooldownText = c.red + getName() + c.dgray + " is still on cooldown.";
    private String rechargeText = c.green + getName() + c.dgray + " has been recharged.";


    public CooldownSkill(RpgPlayer rpgPlayer, int level, String id){
        super(rpgPlayer,level,id);
        this.cooldownValues = getYaml(id).getIntegerList("values.cooldown");
        this.cooldownValue = cooldownValues.get(getLevel());
        this.cooldown = false;
    }

    public boolean isCooldown(){
        if(cooldown){
            playCooldownMedia();
        }
        return cooldown;
    }

    public void playCooldownMedia(){
        getRpgPlayer().playSound(cooldownSound);
        getRpgPlayer().tell(cooldownText);
    }

    public void playRechargeMedia(){
        getRpgPlayer().playSound(rechargeSound);
        getRpgPlayer().tell(rechargeText);
    }

    public void setCooldown(boolean state){
        this.cooldown = state;
    }
    public void refreshCooldown(){
        this.cooldown = true;
        new BukkitRunnable() {

            @Override
            public void run() {
                cooldown = false;
                playRechargeMedia();
            }

        }.runTaskLater(SkillScheme.getInstance(), cooldownValue);
    }

    public Sound getCooldownSound()
    {
        return cooldownSound;
    }

    public void setCooldownSound(Sound cooldownSound)
    {
        this.cooldownSound = cooldownSound;
    }


    public Sound getRechargeSound()
    {
        return rechargeSound;
    }

    public void setRechargeSound(Sound rechargeSound)
    {
        this.rechargeSound = rechargeSound;
    }

    public int getCooldown()
    {
        return cooldownValue;
    }

    public int getCooldown(int level){
        return cooldownValues.get(level);
    }

    public String getCooldownText() {
        return cooldownText;
    }

    public void setCooldownText(String cooldownText) {
        this.cooldownText = cooldownText;
    }

    public String getRechargeText() {
        return rechargeText;
    }

    public void setRechargeText(String rechargeText) {
        this.rechargeText = rechargeText;
    }
}
