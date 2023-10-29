package dev.sirlich.skillscheme.utilities;

import dev.sirlich.skillscheme.SkillScheme;
import dev.sirlich.skillscheme.core.RpgPlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
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

    private static HashSet<Location> doNotBreakBlocks = new HashSet<Location>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        //Deny block breaking if the block is placed as part of a temporary block placement.
        if(event.getPlayer() != null && doNotBreakBlocks.contains(event.getBlock().getLocation())){
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
        tempPlaceBlock(material,location,ticks, BlockFace.UP);
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

    public static void tempPlaceBlock(Material material, Location location, int ticks, BlockFace direction){
        Block block = location.getWorld().getBlockAt(location);
        BlockData blockData = block.getBlockData();
        Location blockLocation = new Location(location.getWorld(),location.getBlockX(),location.getBlockY(),location.getBlockZ());

        final BlockData oldBlockData = blockData.clone();

        doNotBreakBlocks.add(blockLocation);

        block.setType(material);
        if (blockData instanceof Directional) {
            Directional directionalBlock = (Directional) blockData;
            directionalBlock.setFacing(direction);
            block.setBlockData(directionalBlock);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                blockLocation.getWorld().setBlockData(blockLocation, oldBlockData);
                doNotBreakBlocks.remove(blockLocation);
            }

        }.runTaskLater(SkillScheme.getInstance(), ticks);
    }
}
