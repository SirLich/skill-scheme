package main.java.plugin.sirlich.skills.meta;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CooldownSkill extends Skill
{
    private int cooldownTimer;
    private boolean cooldown;
    private int cooldownValue;

    private List<Integer> cooldownValues;
    private Sound cooldownSound;
    private Sound rechargeSound;


    public CooldownSkill(RpgPlayer rpgPlayer, int level, String id){
        super(rpgPlayer,level,id);
        this.cooldownValues = getYaml(id).getIntegerList("values.cooldown");
        this.cooldownTimer = cooldownValues.get(getLevel());
        this.cooldownValue = cooldownTimer;
        this.cooldown = false;
        setCooldownSound(Sound.BLOCK_COMPARATOR_CLICK);
        setRechargeSound(Sound.BLOCK_ENDERCHEST_OPEN);
    }

    public boolean isCooldown(){
        if(cooldown){
            playCooldownMedia();
        }
        return cooldown;
    }

    public void playCooldownMedia(){
        if(rechargeSound != null){
            getRpgPlayer().playSound(cooldownSound);
        }
        getRpgPlayer().chat(c.red + getName() + c.dgray + " is still on cooldown.");
    }

    public void playRechargeMedia(){
        if(rechargeSound != null){
            getRpgPlayer().playSound(rechargeSound);
        }
        getRpgPlayer().chat(c.green + getName() + c.dgray + " has been recharged.");
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

        }.runTaskLater(SkillScheme.getInstance(), cooldownTimer);
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
}
