package main.java.plugin.sirlich.utilities;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public class BlockUtils implements Listener
{

    private static HashSet<Location> dontBreak = new HashSet<Location>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(event.getPlayer() != null && dontBreak.contains(event.getBlock().getLocation())){
            event.setCancelled(true);
        }

        //PlayerState denies block breaking.
        if(event.getPlayer() != null && !RpgPlayer.getRpgPlayer(event.getPlayer()).getPlayerState().canBreakBlocks()){
            event.setCancelled(true);
        }

        //Deny block breaking during testing (creative mode). This behavior replicates the no-sword thing in Vanilla.
        if(event.getPlayer() != null &&
                event.getPlayer().getGameMode() == GameMode.CREATIVE &&
                event.getPlayer().getInventory().getItemInMainHand() != null &&
                (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.WOOD_AXE||
                event.getPlayer().getInventory().getItemInMainHand().getType() == Material.STONE_AXE ||
                event.getPlayer().getInventory().getItemInMainHand().getType() == Material.IRON_AXE ||
                event.getPlayer().getInventory().getItemInMainHand().getType() == Material.GOLD_AXE ||
                event.getPlayer().getInventory().getItemInMainHand().getType() == Material.DIAMOND_AXE) ||
                event.getPlayer().getInventory().getItemInMainHand().getType() == Material.BOW){
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
        location.getWorld().getBlockAt(location).setType(material);
        location.getWorld().getBlockAt(location).setData(direction);
        new BukkitRunnable() {
            @Override
            public void run() {
                oldLocation.getWorld().getBlockAt(oldLocation).setType(oldMaterial);
                dontBreak.remove(oldLocation);

            }

        }.runTaskLater(SkillScheme.getInstance(), ticks);
    }
}
