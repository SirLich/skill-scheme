package main.java.plugin.sirlich.core;

import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class Cancelor implements Listener {

    //Cancel XP
    @EventHandler
    public void onExpSpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof ExperienceOrb) {
            event.setCancelled(true); 
        }
    }
}
