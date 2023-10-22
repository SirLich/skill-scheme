package dev.sirlich.skillscheme.utilities.raycast;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * TODO: Deprecated
 */
public class RayResult {
	public Location endLocation;
	public Block block;
	public BlockFace blockFace;

	public RayResult(Location location, Block block, BlockFace blockFace) {
		this.endLocation = location;
		this.block = block;
		this.blockFace = blockFace;
	}
}
