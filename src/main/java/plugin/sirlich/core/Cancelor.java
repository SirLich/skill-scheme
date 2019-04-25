package main.java.plugin.sirlich.core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class Cancelor implements Listener {

    //Cancel XP
    @EventHandler
    public void onExpSpawn(EntityDeathEvent event) {
        event.setDroppedExp(0);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        event.setExpToDrop(0);
    }
}
