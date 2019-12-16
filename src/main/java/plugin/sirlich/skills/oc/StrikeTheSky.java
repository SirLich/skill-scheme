package plugin.sirlich.skills.oc;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.CooldownSkill;
import plugin.sirlich.utilities.c;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
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
            if(entity instanceof Monster || entity instanceof Player){
                entity.setVelocity(getRpgPlayer().getPlayer().getLocation().toVector().subtract(entity.getLocation().toVector()).setY(0.4).normalize().multiply(power.get(getLevel())));
            }
        }
        refreshCooldown();
    }
}
