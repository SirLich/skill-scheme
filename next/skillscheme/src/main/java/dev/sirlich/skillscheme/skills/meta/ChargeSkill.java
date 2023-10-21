package dev.sirlich.skillscheme.skills.meta;

import org.bukkit.Bukkit;
import dev.sirlich.skillscheme.SkillScheme;
import dev.sirlich.skillscheme.core.RpgPlayer;

public class ChargeSkill extends CooldownSkill{

    private int schedularID;
    private boolean isCharging;
    private int charges;
    private int maxCharges;
    private boolean isFullyCharged;
    private boolean useCooldown;
    private boolean useChargePitch;

    /*
    Required config values:
    - max_charges: int
    - charge_refresh_rate: int, in ticks. How many ticks before a charge is added (up to max_charges).

    Sounds:
    - charge_finish_sound: sound
    - charge_tick_sound: sound
    - charge_release_sound: sound
    - full_charge_release_sound: sound

    Optional config values:
    */

    public ChargeSkill(RpgPlayer rpgPlayer, int level, String id, boolean useCooldown, boolean useChargePitch){
        super(rpgPlayer,level,id);
        this.maxCharges = data.getInt("max_charges");
        this.isCharging = false;
        this.isFullyCharged = false;
        this.charges = 0;
        this.useCooldown = useCooldown;
        this.useChargePitch = useChargePitch;
    }

    public void onReleaseCharge(int charges, boolean isFullyCharged){

    }

    public boolean isFullyCharged(){
        return isFullyCharged;
    }

    public int getCharges(){
        return charges;
    }

    //Overide this!
    public boolean isCharging(){
        System.out.println("Skill: " + this.getId() + " is configured incorrectly. Please overide isCharging");
        return false;
    }

    //please use a super() call if you want to use this method.
    @Override
    public void onEnable(){
        super.onEnable();
        schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                //Handle ticks when you are charging
                if(isCharging()){
                    if(!useCooldown || !isCooldownNoMedia()){
                        if(charges == maxCharges){
                            if(!isFullyCharged){
                                getRpgPlayer().playSound(data.getSound("charge_finish_sound"));
                                isFullyCharged = true;
                            }
                        } else {
                            onCharge();
                            charges ++;
                            isCharging = true;
                            float pitch = 1.0f;
                            if(useChargePitch){
                                pitch = 1 +  (float) charges/data.getInt("max_charges");
                                System.out.println(pitch);
                            }
                            getRpgPlayer().playSound(data.getSound("charge_tick_sound"), 1, pitch);
                        }
                    }
                }

                //Handle the case where you WERE charging and release a charge.
                else {
                    if(isCharging){
                        if(useCooldown){
                            refreshCooldown();
                        }
                        isCharging = false;
                        if(isFullyCharged){
                            getRpgPlayer().playSound(data.getSound("full_charge_release_sound"));
                        } else {
                            getRpgPlayer().playSound(data.getSound("charge_release_sound"));
                        }
                        onReleaseCharge(charges, isFullyCharged);
                        isFullyCharged = false;
                        charges = 0;
                    }
                }
            }
        }, 0L, data.getInt("charge_refresh_rate"));
    }

    public void onCharge(){

    }

    //Please use a super() method if you want to use this method.
    @Override
    public void onDisable(){
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }
}
