package plugin.sirlich.skills.meta;

import plugin.sirlich.SkillScheme;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.utilities.Xliff;
import plugin.sirlich.utilities.c;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CooldownSkill extends Skill
{
    /*
    Required config values:
    - values.cooldown: int, in ticks (20 ticks per second)


    Optional config values:
     */

    //Current cooldown by level
    private int cooldownValue;

    private boolean cooldown;
    private long lastUsed;

    private Sound cooldownSound;
    private Sound rechargeSound;
    private String cooldownText = c.red + getName() + c.dgray + " can be used again in ";
    private String rechargeText = c.green + getName() + c.dgray + " has been recharged.";

    private double calculateCooldownLeft(){
        double x = (cooldownValue / 20.00) - (System.currentTimeMillis() - lastUsed) / 1000.00;
        System.out.println(x);
        return Math.round(x * 10) / 10.0;
    }

    public CooldownSkill(RpgPlayer rpgPlayer, int level, String id){
        super(rpgPlayer,level,id);
        this.cooldownSound = Xliff.getSound("CooldownSkill.cooldown_sound");
        this.rechargeSound = Xliff.getSound("CooldownSkill.recharge_sound");
        this.cooldownValue = data.getInt("cooldown");
        this.cooldown = false;
    }

    public boolean isCooldown(){
        if(cooldown) {
            playCooldownMedia();
        }
        return cooldown;
    }

    public void playCooldownMedia(){
        getRpgPlayer().playSound(cooldownSound);
        getRpgPlayer().tell(cooldownText + c.green + calculateCooldownLeft() + c.dgray + " seconds");
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
        this.lastUsed = System.currentTimeMillis();
        new BukkitRunnable() {

            @Override
            public void run() {
                cooldown = false;
                if(getRpgPlayer().testSession(getSessionToken())){
                    playRechargeMedia();
                }
            }

        }.runTaskLater(SkillScheme.getInstance(), cooldownValue);
    }

    public int getCooldown()
    {
        return cooldownValue;
    }
}
