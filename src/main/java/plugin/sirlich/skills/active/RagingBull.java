package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.RageSkill;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RagingBull extends RageSkill {
    private static String id = "RagingBull";
    private static List<Double> power = getYaml(id).getDoubleList("values.power");


    public RagingBull(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"RagingBull", Material.DIRT);
    }

    @Override
    public ArrayList<String> getDescription(int level){
        ArrayList<String> lorelines = new ArrayList<String>();
        lorelines.add(c.dpurple + "Bullls!");
        return lorelines;
    }

    @Override
    public void onEnrage(){
        getRpgPlayer().editWalkSpeedModifier(2);
    }

    @Override
    public void onRageExpire(){
        getRpgPlayer().editWalkSpeedModifier(-2);
    }

    public void onAxeMeleeAttackOther(EntityDamageByEntityEvent event){
        endRageEarly();
        UUID uuid = event.getEntity().getUniqueId();
        if(RpgPlayer.isPlayer(uuid)){
            RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(uuid);
            rpgPlayer.editWalkSpeedModifier(-2);
            new BukkitRunnable() {

                @Override
                public void run() {
                    System.out.println("Slowness on the target expires");
                }

            }.runTaskLater(SkillScheme.getInstance(), duration.get(getLevel()));
        }
    }

    @Override
    public void onAxeRightClick(PlayerInteractEvent event){
        attemptRage();
    }
}
