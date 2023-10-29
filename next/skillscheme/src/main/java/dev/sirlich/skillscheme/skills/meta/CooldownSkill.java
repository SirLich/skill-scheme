package dev.sirlich.skillscheme.skills.meta;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import dev.sirlich.skillscheme.SkillScheme;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.utilities.Color;
import dev.sirlich.skillscheme.utilities.Xliff;

/**
 * CooldownSkill is a special base-class which gives skills a cooldown.
 */
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
    private String cooldownText = Color.red + getName() + Color.dgray + " can be used again in ";
    private String rechargeText = Color.green + getName() + Color.dgray + " has been recharged.";

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

    @Override
    public boolean skillCheck(){

        //Make sure the player isn't on cooldown
        if(cooldown) {
            playCooldownMedia();
        }

        //Return based on the super check as well as cooldown value
        return cooldown || super.skillCheck();
    }

    public boolean isCooldownNoMedia(){
        return cooldown;
    }

    public void playCooldownMedia(){
        getRpgPlayer().playSound(cooldownSound);
        getRpgPlayer().tell(cooldownText + getChargeColor() + calculateCooldownLeft() + Color.dgray + " seconds");
    }

    public void playRechargeMedia(){
        getRpgPlayer().playSound(rechargeSound);
        getRpgPlayer().tell(rechargeText);
    }

    public String getChargeColor(){
        double percent = calculateChargeBarCooldown();
        if(percent > CHARGE_BAR_LENGTH * 0.8){
            return Color.green;
        } else if(percent > CHARGE_BAR_LENGTH * 0.6){
            return Color.yellow;
        } else if(percent > CHARGE_BAR_LENGTH * 0.4){
            return Color.gold;
        } else if(percent > CHARGE_BAR_LENGTH * 0.2){
            return Color.red;
        } else {
            return Color.dred;
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
        // TODO: Decide on this warning
        // System.out.println("Warning: showActionBar is not bring overridden. In: " + getName());
        return false;
    }

    public int getCooldown()
    {
        return cooldownValue;
    }

    @Override
    public void onEnable(){
        // refreshCooldown();
        //Turn on charge-bar
        schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                if(cooldown && showActionBar()){
                    long chargeMeter = calculateChargeBarCooldown();
                    String m = Color.gold + Color.bold + getName() + Color.yellow + Color.bold + ": [";
                    for(int i = 0; i < CHARGE_BAR_LENGTH; i ++){
                        if(i < chargeMeter){
                            m = m + Color.green + Color.bold + "▇";
                        } else {
                            m = m + Color.red + Color.bold + "▇";
                        }
                    }

                    m = m + Color.yellow + Color.bold + "] " + getChargeColor() + calculateCooldownLeft();

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
