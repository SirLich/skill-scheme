package main.java.plugin.sirlich.utilities;

import main.java.plugin.sirlich.SkillScheme;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BlockUtils implements Listener
{

    private static HashSet<Location> dontBreak = new HashSet<Location>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        System.out.println("Broke: " + event.getBlock().getLocation().toString());
        if(event.getPlayer() != null && dontBreak.contains(event.getBlock().getLocation())){
            event.setCancelled(true);
        }
    }


    public static void tempPlaceBlock(Material material, Location location, int ticks){
        tempPlaceBlock(material,location,ticks,(byte)0x0);
    }
    public static void tempPlaceBlock(Material material, Location location, int ticks,byte direction){
        final Material oldMaterial = location.getWorld().getBlockAt(location).getType();
        final Location oldLocation = new Location(location.getWorld(),location.getBlockX(),location.getBlockY(),location.getBlockZ());
        dontBreak.add(oldLocation);
        System.out.println("Added: " + oldLocation.toString());
        location.getWorld().getBlockAt(location).setType(material);
        location.getWorld().getBlockAt(location).setData(direction);
        new BukkitRunnable() {
            @Override
            public void run() {
                oldLocation.getWorld().getBlockAt(oldLocation).setType(oldMaterial);
                dontBreak.remove(oldLocation);
                System.out.println("Removed: " + oldLocation.toString());

            }

        }.runTaskLater(SkillScheme.getInstance(), ticks);
    }
}
