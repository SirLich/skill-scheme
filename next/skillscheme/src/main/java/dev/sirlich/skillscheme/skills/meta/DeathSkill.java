package dev.sirlich.skillscheme.skills.meta;

import dev.sirlich.skillscheme.core.RpgPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.List;

public class DeathSkill extends Skill{

    private List<Integer> range;

    public DeathSkill(RpgPlayer rpgPlayer, int level, String id){
        super(rpgPlayer,level,id);
        this.range = getYaml(id).getIntegerList("range");
    }

    @Override
    public void onDeath(PlayerDeathEvent event){
        RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(event.getEntity().getUniqueId());
        int r = range.get(getLevel());
        ArrayList<RpgPlayer> allies = new ArrayList<RpgPlayer>();
        ArrayList<RpgPlayer> enemies = new ArrayList<RpgPlayer>();
        for(Entity entity : event.getEntity().getNearbyEntities(r,r,r)){
            if(entity instanceof Player && RpgPlayer.isRpgPlayer((Player) entity)){
                RpgPlayer effectedRpgPlayer = RpgPlayer.getRpgPlayer(event.getEntity());
                if(RpgPlayer.isSameTeam(rpgPlayer,effectedRpgPlayer)){
                    allies.add(effectedRpgPlayer);
                } else {
                    enemies.add(effectedRpgPlayer);
                }
            }
        }
        onDeathEffect(event,allies,enemies);
    }

    public void onDeathEffect(PlayerDeathEvent event, ArrayList<RpgPlayer> allies, ArrayList<RpgPlayer> enemies){

    }

}
