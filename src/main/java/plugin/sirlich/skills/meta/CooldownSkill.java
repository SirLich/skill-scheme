package plugin.sirlich.skills.meta;

import org.bukkit.Bukkit;
import plugin.sirlich.SkillScheme;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.utilities.Xliff;
import plugin.sirlich.utilities.c;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;


public class CooldownSkill extends Skill
{
    /*
    Required config values:
    - values.cooldown: int, in ticks (20 ticks per second)


    Optional config values:
     */

    //Current cooldown by level
    private int cooldownValue;
    private int schedularID;
    private boolean cooldown;
    private long lastUsed;
    private final int ACTION_BAR_REFRESH_RATE = 3;
    private final int CHARGE_BAR_LENGTH = 10;

    private Sound cooldownSound;
    private Sound rechargeSound;
    private String cooldownText = c.red + getName() + c.dgray + " can be used again in ";
    private String rechargeText = c.green + getName() + c.dgray + " has been recharged.";

    private double calculateCooldownLeft(){
        double x = (cooldownValue / 20.00) - (System.currentTimeMillis() - lastUsed) / 1000.00;
        return Math.round(x * 10) / 10.0;
    }

    //Returns a number from 1-CHARGE_BAR_LENGTH
    private long calculateChargeBarCooldown(){
        return CHARGE_BAR_LENGTH - Math.abs(Math.round(calculateCooldownLeft() / (cooldownValue / 20.0) * CHARGE_BAR_LENGTH));
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

    public boolean isCooldownNoMedia(){
        return cooldown;
    }

    public void playCooldownMedia(){
        getRpgPlayer().playSound(cooldownSound);
        getRpgPlayer().tell(cooldownText + getChargeColor() + calculateCooldownLeft() + c.dgray + " seconds");
    }

    public void playRechargeMedia(){
        getRpgPlayer().playSound(rechargeSound);
        getRpgPlayer().tell(rechargeText);
    }

    public String getChargeColor(){
        double percent = calculateChargeBarCooldown();
        if(percent > CHARGE_BAR_LENGTH * 0.8){
            return c.green;
        } else if(percent > CHARGE_BAR_LENGTH * 0.6){
            return c.yellow;
        } else if(percent > CHARGE_BAR_LENGTH * 0.4){
            return c.gold;
        } else if(percent > CHARGE_BAR_LENGTH * 0.2){
            return c.red;
        } else {
            return c.dred;
        }
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

    //This should be overridden!
    public boolean showActionBar(){
        System.out.println("WARNING !! THIS ISN'T SETUP CORRECTLY. You must override this value.");
        return false;
    }

    public int getCooldown()
    {
        return cooldownValue;
    }

    @Override
    public void onEnable(){
        schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                if(cooldown && showActionBar()){
                    long chargeMeter = calculateChargeBarCooldown();
                    String m = c.gold + c.bold + getName() + c.yellow + c.bold + ": [";
                    for(int i = 0; i < CHARGE_BAR_LENGTH; i ++){
                        if(i < chargeMeter){
                            m = m + c.green + c.bold + "▇";
                        } else {
                            m = m + c.red + c.bold + "▇";
                        }
                    }

                    m = m + c.yellow + c.bold + "] " + getChargeColor() + calculateCooldownLeft();

                    getRpgPlayer().setActionBar(m);
                }
            }
        }, 0L, ACTION_BAR_REFRESH_RATE);
    }

    public String buildChargeBar(){
        return "";
    }

    @Override
    public void onDisable(){
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }
}
