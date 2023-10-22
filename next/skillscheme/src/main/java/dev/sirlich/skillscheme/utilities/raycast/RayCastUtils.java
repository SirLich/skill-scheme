package dev.sirlich.skillscheme.utilities.raycast;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

/**
 * TODO: Deprecated
 */
public class RayCastUtils {
    public static Location getNextLocation(Location starting, Vector direction, double distance) {
        Location ending = starting.clone().add(direction.clone().multiply(distance));
        return ending;
    }

    public static RayResult rayCastBlocks(LivingEntity entity, double maxDistance, boolean ignoreLiquids, double precision) {
        return rayCastBlocks(entity, new Vector(0, 0, 0), maxDistance, ignoreLiquids, precision);
    }

    public static RayResult rayCastBlocks(LivingEntity entity, Vector offset, double maxDistance, boolean ignoreLiquids, double precision) {

        Location starting = entity.getEyeLocation().add(offset);
        Vector direction = starting.getDirection();

        Location check = starting.clone();
        Location last = starting.clone();

        double distanceTraveled = 0;
        while (distanceTraveled < maxDistance) {
            last = check.clone();

            check = getNextLocation(check, direction, precision);
            if (check.getBlock().getType() != Material.AIR && !(check.getBlock().isLiquid() && ignoreLiquids)) {
                break;
            }
            distanceTraveled += precision;
        }

        Block block = check.getBlock();
        BlockFace face = block.getFace(last.getBlock());

        return new RayResult(check, block, face);
    }
}
