package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.CooldownSkill;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.entity.Fireball;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class ClassicFireball extends CooldownSkill
{
    private static String id = "ClassicFireball";
    private static List<Float> yield = getYaml(id).getFloatList("values.yield");

    public ClassicFireball(RpgPlayer rpgPlayer,int level){
        super(rpgPlayer,level,"ClassicFireball");
    }

    @Override
    public ArrayList<String> getDescription(int level){
        ArrayList<String> lorelines = new ArrayList<String>();
        lorelines.add(c.dgray + "Fire and brimstone!");
        lorelines.add(c.dgray + "Shoot fire-balls at unsuspecting foes.");
        lorelines.add("");
        lorelines.add(c.dgray + "Right-Click" + c.aqua + " axe " + c.dgray + "to fire");
        lorelines.add("");
        lorelines.add(c.dgray + "Cooldown: " + c.green + getCooldown()/20 + c.dgray + " seconds");
        lorelines.add(c.dgray + "Yield: " + c.green + yield.get(level) + c.dgray);
        return lorelines;
    }


    @Override
    public void onAxeRightClick(PlayerInteractEvent entityEvent){
        if(isCooldown()){return;}
        Fireball f = getRpgPlayer().getPlayer().launchProjectile(Fireball.class);
        f.setIsIncendiary(false);
        f.setGlowing(true);
        f.setYield(yield.get(getLevel()));
        refreshCooldown();
    }
}
