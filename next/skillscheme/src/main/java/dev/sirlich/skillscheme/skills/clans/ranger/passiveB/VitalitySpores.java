package dev.sirlich.skillscheme.skills.clans.ranger.passiveB;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.TickingSkill;
import dev.sirlich.skillscheme.utilities.Color;

/**
 * Allows you to passively heal, as long as you haven't taken damage for a while.
 */
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
            getRpgPlayer().tell(Color.green + getName() + Color.dgray + " are now active.");
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
            getRpgPlayer().tell(Color.red + getName() + Color.dgray + " is no longer active.");
        }
    }
}
