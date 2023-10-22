package dev.sirlich.skillscheme.core;

import org.bukkit.potion.PotionEffectType;

/**
 * Deprecated!
 */
public class EffectWrapper {

	public EffectWrapper(PotionEffectType effectType, int power, long durationInTicks) {
		this.effectType = effectType;
		this.power = power;
		this.ticks = durationInTicks;

		startTime = System.currentTimeMillis();
	}

	public PotionEffectType effectType;
	public int power;
	public long ticks;
	public long startTime;

	public boolean hasExpired() {
		return getRemainingDuration() <= 0;
	}

	public int getRemainingDuration() {
		return (int) (startTime - System.currentTimeMillis());
	}
}
