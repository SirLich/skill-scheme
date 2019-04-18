package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.CooldownSkill;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.ArrayList;
import java.util.List;

public class StrikeTheSky extends CooldownSkill
{
    private static String id = "StrikeTheSky";
    private List<Double> power = getYaml(id).getDoubleList("values.power");
    private List<Double> range = getYaml(id).getDoubleList("values.range");

    public StrikeTheSky(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,id);
    }

    @Override
    public ArrayList<String> getDescription(int level){
        ArrayList<String> lorelines = new ArrayList<String>();
        lorelines.add(c.dgray + "Create a whirlwind to sucks enemies towards you.");
        return lorelines;
    }


    @Override
    public void onSwap(PlayerSwapHandItemsEvent event){
        if(isCooldown()){return;}
        Double r = range.get(getLevel());
        for(Entity entity : event.getPlayer().getNearbyEntities(r,r,r)){
            entity.setVelocity(getRpgPlayer().getPlayer().getLocation().getDirection().setY(0.6).normalize().multiply(-power.get(getLevel())));
        }
        refreshCooldown();
    }
}
