package plugin.sirlich.skills.meta;

import org.bukkit.Bukkit;
import plugin.sirlich.SkillScheme;
import plugin.sirlich.core.RpgPlayer;

public class SwordChargeSkill extends CooldownSkill{

    private int schedularID;
    private boolean isCharging;
    private int charges;
    private int maxCharges;
    private boolean isFullyCharged;
    private boolean isOnGroundRequired;

    /* CONFIG TO IMPLEMENT:
    max_charges
    sword_charge_finished_sound
    sword_charge_sound
    sword_charged_release_sound
    sword_fully_charged_release_sound
    sword_charge_refresh_rate
     */

    public SwordChargeSkill(RpgPlayer rpgPlayer, int level, String id, boolean isOnGroundRequired){
        super(rpgPlayer,level,id);
        this.maxCharges = data.getInt("max_charges");
        this.isCharging = false;
        this.isFullyCharged = false;
        this.isOnGroundRequired = isOnGroundRequired;
        this.charges = 0;
    }

    public void onEnablePassthrough(){

    }

    public void onDisablePassthrough(){

    }

    public void onReleaseCharge(int charges, boolean isFullyCharged){

    }

    @Override
    public void onEnable(){
        onEnablePassthrough();
        schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                //The funky or clause here handles the case where the user doesn't have to be on the ground.
                if(getRpgPlayer().getPlayer().isBlocking() && (getRpgPlayer().getPlayer().isOnGround() || !isOnGroundRequired)){
                    if(!isCooldown()){
                        if(charges == maxCharges){
                            if(!isFullyCharged){
                                getRpgPlayer().playSound(data.getSound("sword_charge_finished_sound"));
                                isFullyCharged = true;
                            }
                        } else {
                            onCharge();
                            charges ++;
                            isCharging = true;
                            getRpgPlayer().playSound(data.getSound("sword_charge_sound"));
                        }
                    }
                } else {
                    if(isCharging){
                        refreshCooldown();
                        isCharging = false;
                        if(isFullyCharged){
                            getRpgPlayer().playSound(data.getSound("sword_fully_charged_release_sound"));
                        } else {
                            getRpgPlayer().playSound(data.getSound("sword_charged_release_sound"));
                        }
                        onReleaseCharge(charges, isFullyCharged);
                        isFullyCharged = false;
                        charges = 0;
                    }
                }
            }
        }, 0L, data.getInt("sword_charge_refresh_rate"));
    }

    public void onCharge(){

    }

    @Override
    public void onDisable(){
        onDisablePassthrough();
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }
}
