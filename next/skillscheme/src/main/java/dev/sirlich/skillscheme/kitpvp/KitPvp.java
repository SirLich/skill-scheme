package dev.sirlich.skillscheme.kitpvp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import dev.sirlich.skillscheme.SkillScheme;
import dev.sirlich.skillscheme.core.PlayerJoinHandler;
import dev.sirlich.skillscheme.core.PlayerState;
import dev.sirlich.skillscheme.core.RpgPlayer;

/**
 * Special class, intending to encapsulate the functionality which is KitPvp specific. The idea
 * is this module can be deleted in the future, to re-create SkillScheme as a clans-gamemode or something.
 */
public class KitPvp implements Listener {
	static private int schedularID;

	@EventHandler
	public void PlayerDeathEvent(PlayerDeathEvent event){
		// Clear Drops
		event.getDrops().clear();

		// Set player state back to
		RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(event.getPlayer());
		rpgPlayer.setPlayerState(PlayerState.LOBBY);
	}


	@EventHandler
	public void EntityDamageEvent(EntityDamageEvent e){
		if (e.getEntity() instanceof  Player)
		{
			Player player = (Player) e.getEntity();
			RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);
			if (rpgPlayer.getPlayerState() == PlayerState.LOBBY) {
				e.setCancelled(true);
			}
		}
	}

	static public void init(){
		schedularID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkillScheme.getInstance(), new Runnable() {
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()) {
					RpgPlayer rpgPlayer = RpgPlayer.getRpgPlayer(player);

					final long HEIGHT_LIMIT = 108;

					if (rpgPlayer.getPlayerState() == PlayerState.LOBBY && player.getLocation().getY() < HEIGHT_LIMIT) {
						rpgPlayer.setPlayerState(PlayerState.KITPVP);
						player.setFallDistance(-1000000); // Prevents fall damage, once.
					} 

					if (rpgPlayer.getPlayerState() == PlayerState.KITPVP && player.getLocation().getY() > HEIGHT_LIMIT + 1) {
						rpgPlayer.setPlayerState(PlayerState.LOBBY);
					}
				}
			}
		}, 0L, 5);
	}

	static public void deinit(){
		Bukkit.getServer().getScheduler().cancelTask(schedularID);
	}
}
