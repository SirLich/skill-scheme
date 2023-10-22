package dev.sirlich.skillscheme.skills.clans.any;

import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.Skill;

/**
 * Generic Class which allows you to take less lengthy durations.
 */
public class Constitution extends Skill {
	public Constitution(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"Constitution");
    }

	@Override
	public void onEnable(){
		getRpgPlayer().negativeEffectDurationModifier = data.getDouble("negative_effect_duration_modifier");
	}

	@Override
	public void onDisable() {
		getRpgPlayer().negativeEffectDurationModifier = 1;
	}
}

