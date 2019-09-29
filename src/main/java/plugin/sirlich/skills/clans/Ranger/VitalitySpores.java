package plugin.sirlich.skills.clans.Ranger;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.TickingSkill;
import plugin.sirlich.utilities.c;

public class VitalitySpores extends TickingSkill {
    /*
    regeneration_duration: int, ticks
    regeneration_amplifier: int, ticks
    safe_duration: int, ticks

    Sounds:
    now_healing

     */
    private boolean healing = false;

    public VitalitySpores(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "VitalitySpores");
    }

    @Override
    public void onTick(){
        //Player hasn't been damaged in a good long while.
        final int TICKS_TO_MILLIS = 50;
        if(!healing && System.currentTimeMillis() > (data.getDouble("safe_duration") * TICKS_TO_MILLIS) + getRpgPlayer().getLastDamaged()){
            healing = true;
            getRpgPlayer().tell(c.green + getName() + c.dgray + " are now active.");
            getRpgPlayer().playSound(data.getSound("now_healing"), 0.5f);
        }

        if(healing){
            getRpgPlayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, data.getInt("regeneration_duration"),data.getInt("regeneration_amplifier")));
        }
    }

    @Override
    public void onDamageSelf(EntityDamageEvent event){
        if(healing){
            healing = false;
            getRpgPlayer().getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
            getRpgPlayer().tell(c.red + getName() + c.dgray + " is no longer active.");
        }
    }
}
