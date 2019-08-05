package plugin.sirlich.utilities;

import plugin.sirlich.SkillScheme;
import plugin.sirlich.core.RpgPlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BlockUtils implements Listener
{

    private static HashSet<Location> dontBreak = new HashSet<Location>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        //Deny block breaking if the block is placed as part of a temporary block placement.
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
                WeaponUtils.isWeapon(event.getPlayer().getInventory().getItemInMainHand().getType())){
            event.setCancelled(true);
        }
    }


    public static void tempPlaceBlock(Material material, Location location, int ticks){
        tempPlaceBlock(material,location,ticks,(byte)0x0);
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    Vector blockLoc = new Vector(x,y,z);
                    if(blockLoc.distance(location.toVector()) <= radius){
                        blocks.add(location.getWorld().getBlockAt(x, y, z));
                    }
                }
            }
        }
        return blocks;
    }

    public static void tempPlaceBlock(Material material, Location location, int ticks,byte direction){
        final Material oldMaterial = location.getWorld().getBlockAt(location).getType();
        final Location oldLocation = new Location(location.getWorld(),location.getBlockX(),location.getBlockY(),location.getBlockZ());
        final Byte oldDirection = location.getWorld().getBlockAt(location).getData();
        dontBreak.add(oldLocation);
        location.getWorld().getBlockAt(location).setType(material);
        new BukkitRunnable() {
            @Override
            public void run() {
                oldLocation.getWorld().getBlockAt(oldLocation).setType(oldMaterial);
                dontBreak.remove(oldLocation);
            }

        }.runTaskLater(SkillScheme.getInstance(), ticks);
    }
}
