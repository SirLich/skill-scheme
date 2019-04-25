package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.RageSkill;
import main.java.plugin.sirlich.utilities.BlockUtils;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.ArrayList;
import java.util.List;

public class IceRink extends RageSkill
{
    private static String id = "IceRink";
    private static List<Integer> radius = getYaml(id).getIntegerList("values.radius");

    private int schedularID;
    private int REFRESH_RATE = 5;

    public IceRink(RpgPlayer rpgPlayer,int level){
        super(rpgPlayer,level,"IceRink", Material.ICE);
    }

    @Override
    public ArrayList<String> getDescription(int level){
        ArrayList<String> lorelines = new ArrayList<String>();
        lorelines.add(c.gray + c.italic + "Freeze the world!");
        lorelines.add("");
        lorelines.add(c.dgray + "Create an icy circle around you while active.");
        lorelines.add(c.dgray + "Good for crossing water.");
        lorelines.add("");
        lorelines.add(c.dgray + "Radius: " + c.green + radius.get(level) + c.dgray + " blocks");
        return lorelines;
    }

    @Override
    public void onEnrage(){
        schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
            public void run() {
                for(Block block : BlockUtils.getNearbyBlocks(getRpgPlayer().getPlayer().getLocation(), radius.get(getLevel()))){
                    if(block.getType() != Material.AIR && block.getType() != Material.ICE){
                        BlockUtils.tempPlaceBlock(Material.ICE,block.getLocation(),duration.get(getLevel()));
                    }
                }
            }
        }, 0L, REFRESH_RATE);
    }

    @Override
    public void onRageExpire(){
        Bukkit.getServer().getScheduler().cancelTask(schedularID);
    }

    @Override
    public void onSwap(PlayerSwapHandItemsEvent entityEvent){
        attemptRage();
    }
}