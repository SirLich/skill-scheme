package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.CooldownSkill;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.ArrayList;
import java.util.List;

public class StrikeTheEarth extends CooldownSkill
{
    private static String id = "StrikeTheEarth";
    private  List<Double> power = getYaml(id).getDoubleList("values.power");
    private List<Double> range = getYaml(id).getDoubleList("values.range");

    public StrikeTheEarth(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,id);
    }

    @Override
    public void onSwap(PlayerSwapHandItemsEvent event){
        if(isCooldown()){return;}
        Double r = range.get(getLevel());
        for(Entity entity : event.getPlayer().getNearbyEntities(r,r,r)){
            if(entity instanceof LivingEntity){
                entity.setVelocity(getRpgPlayer().getPlayer().getLocation().toVector().subtract(entity.getLocation().toVector()).multiply(-1).setY(0.6).normalize().multiply(Math.min(power.get(getLevel()) / ((getRpgPlayer().getPlayer().getLocation().distance(entity.getLocation()) + 1)/3),power.get(getLevel())/2)));
            }
        }
        refreshCooldown();
    }
}
