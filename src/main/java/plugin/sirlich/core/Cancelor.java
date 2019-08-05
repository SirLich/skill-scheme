package plugin.sirlich.core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class Cancelor implements Listener {

    //Cancel mob XP
    @EventHandler
    public void onExpSpawn(EntityDeathEvent event) {
        event.setDroppedExp(0);
    }

    //Cancel block XP
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        event.setExpToDrop(0);
    }
}
