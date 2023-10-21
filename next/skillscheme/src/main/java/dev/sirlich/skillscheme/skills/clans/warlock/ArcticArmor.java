package dev.sirlich.skillscheme.skills.clans.warlock;

import org.bukkit.event.player.PlayerDropItemEvent;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.ManaSkill;
import dev.sirlich.skillscheme.utilities.BlockUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class ArcticArmor extends ManaSkill
{
    private static String id = "ArcticArmor";

    public ArcticArmor(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"ArcticArmor");
    }

    @Override
    public void onTick(){
        for(Block block : BlockUtils.getNearbyBlocks(getRpgPlayer().getPlayer().getLocation(), data.getInt("radius"))){
            if(block.getType() != Material.AIR && block.getType() != Material.ICE && block.getLightLevel() >= 0){
                BlockUtils.tempPlaceBlock(Material.ICE,block.getLocation(),data.getInt("ice_duration"));
            }
        }
    }

    @Override
    public void onSwordDrop(PlayerDropItemEvent entityEvent){
        toggleStatus();
    }
}
