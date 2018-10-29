package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.ActiveSkill;
import main.java.plugin.sirlich.utilities.BlockUtils;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Prism extends ActiveSkill
{
    private static String id = "Prism";
    private static List<Integer> cooldown = getYaml(id).getIntegerList("values.cooldown");
    private static List<Integer> duration = getYaml(id).getIntegerList("values.duration");

    private boolean deployed = false;

    public Prism(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,cooldown.get(level));
    }

    @Override
    public void onSwap(PlayerSwapHandItemsEvent event){
        if(isCooldown()){return;}
        Player player = getRpgPlayer().getPlayer();
        deployed = true;
        BlockUtils.tempPlaceBlock(Material.GLASS,player.getLocation(),duration.get(getLevel()));
        BlockUtils.tempPlaceBlock(Material.GLASS,player.getLocation().add(new Vector(0,1,0)),duration.get(getLevel()));
        new BukkitRunnable() {

            @Override
            public void run() {
                deployed = false;
            }

        }.runTaskLater(SkillScheme.getInstance(), duration.get(getLevel()));
        refreshCooldown();
    }


    @Override
    public ArrayList<String> getDescription(int level){
        ArrayList<String> lorelines = new ArrayList<String>();
        lorelines.add(c.dgray + "Retreat from the fighting in this quick-deploy");
        lorelines.add(c.dgray + "crystal prism.");
        lorelines.add("");
        lorelines.add(c.dgray + "Press " + c.aqua + "F" + c.dgray + " to activate");
        lorelines.add("");
        lorelines.add(c.dgray + "Cooldown: " + c.green + cooldown.get(level)/20 + c.dgray + " seconds");
        lorelines.add(c.dgray + "Duration: " + c.green + duration.get(level)/20 + c.dgray + " seconds");
        return lorelines;
    }

    @Override
    public void onSuffocationDamageSelf(EntityDamageEvent event){
        if(deployed){
            event.setDamage(0);
        }
    }

}
