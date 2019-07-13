package main.java.plugin.sirlich.skills.death;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.DeathSkill;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;

public class SwiftDeath extends DeathSkill {

    public SwiftDeath(RpgPlayer rpgPlayer, int level, String id){
        super(rpgPlayer, level, id);
    }

    @Override
    public void onDeathEffect(PlayerDeathEvent event, ArrayList<RpgPlayer> allies, ArrayList<RpgPlayer> enemies){
        for(RpgPlayer rpgPlayer : allies){

        }

        for(RpgPlayer rpgPlayer : enemies){

        }
    }
}
