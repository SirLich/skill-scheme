package main.java.plugin.sirlich.core;

import main.java.plugin.sirlich.SkillScheme;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockUtils implements Listener
{

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(event.getPlayer() != null){
            Player player = (Player) event.getPlayer();
            Block block = event.getBlock();
            System.out.println(block.getData());
        }
    }
    public static void tempPlaceBlock(Material material, Location location, int ticks){
        tempPlaceBlock(material,location,ticks,(byte)0x0);
    }
    public static void tempPlaceBlock(Material material, Location location, int ticks,byte direction){
        final Material oldMaterial = location.getWorld().getBlockAt(location).getType();
        final Location oldLocation = location;
        location.getWorld().getBlockAt(location).setType(material);
        location.getWorld().getBlockAt(location).setData(direction);
        new BukkitRunnable() {
            @Override
            public void run() {
                oldLocation.getWorld().getBlockAt(oldLocation).setType(oldMaterial);
            }

        }.runTaskLater(SkillScheme.getInstance(), ticks);
    }
}
