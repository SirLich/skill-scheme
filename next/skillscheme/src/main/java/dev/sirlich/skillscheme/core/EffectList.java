package dev.sirlich.skillscheme.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import dev.sirlich.skillscheme.SkillScheme;

/**
 * A class which will be created by the RPGPlayer, and represents all handling for *effects* on this player.
 */
public class EffectList {
    // The list of active effects on this player
    HashMap<UUID, EffectWrapper> effects = new HashMap<UUID, EffectWrapper>();

    private RpgPlayer rpgPlayer;

    public EffectList(RpgPlayer rpgPlayer_) {
        rpgPlayer = rpgPlayer_;
    };

    public UUID addEffect(PotionEffectType type, int strength, long durationInTicks) {
        UUID effectID = UUID.randomUUID();
        EffectWrapper effect = new EffectWrapper(type, strength, durationInTicks);
        effects.put(effectID, effect);

        // Set a timer to calculate when the 
        new BukkitRunnable() {
            @Override
            public void run() {
                evaluateActivePotionEffects();
            }

        }.runTaskLater(SkillScheme.getInstance(), durationInTicks);

        evaluateActivePotionEffects();
        return effectID;
    };

    /**
     * Clears all effects
     */
    public void clearEffects(){
        effects.clear();
        rpgPlayer.getPlayer().clearActivePotionEffects();
    }

    /**
     * Searches through all effects, and ensures their effect time is positive.
     */
    public void cleanInvalidEffects(){
        ArrayList<UUID> effectsToRemove = new ArrayList<UUID>();
        
        for (Map.Entry<UUID, EffectWrapper> entry : effects.entrySet()) {
            if (entry.getValue().hasExpired()){
                effectsToRemove.add(entry.getKey());
            }
        }

        for (UUID uuid : effectsToRemove){
            effects.remove(uuid);
        }
    }

    /**
     * Finds the most relevant effect to apply, based on it's power stat. The idea is that as each effect expires, it blends to the next.
     */
    public EffectWrapper getMostPowerfulEffectOfType(PotionEffectType type) {
        EffectWrapper mostPowerfulEffect = null;

        for (EffectWrapper effect : effects.values()) {

            if (effect.effectType == type) {
                if (mostPowerfulEffect == null || effect.power > mostPowerfulEffect.power){
                    mostPowerfulEffect = effect;
                }				
            }
        }	

        return mostPowerfulEffect;
    }


    /**
     * Checks through all current effects, and picks the best set to apply.
     */
    public void evaluateActivePotionEffects() {
        cleanInvalidEffects();

        for (PotionEffectType type : PotionEffectType.values()){
            EffectWrapper effect = getMostPowerfulEffectOfType(type);
            if (effect != null) {
                rpgPlayer.getPlayer().addPotionEffect(new PotionEffect(effect.effectType, effect.getRemainingDuration(), effect.power));
            }
        }
    };
}
