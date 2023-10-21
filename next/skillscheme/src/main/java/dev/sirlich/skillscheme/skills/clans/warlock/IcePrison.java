package dev.sirlich.skillscheme.skills.clans.warlock;

import org.bukkit.event.player.PlayerDropItemEvent;
import dev.sirlich.skillscheme.SkillScheme;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.CooldownSkill;
import dev.sirlich.skillscheme.utilities.BlockUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class IcePrison extends CooldownSkill
{
    private boolean deployed = false;

    public IcePrison(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"IcePrison");
    }

    @Override
    public void onSwordDrop(PlayerDropItemEvent event){
        if(skillCheck()){return;}
        Player player = getRpgPlayer().getPlayer();
        deployed = true;
        BlockUtils.tempPlaceBlock(Material.GLASS,player.getLocation(),data.getInt("duration"));
        BlockUtils.tempPlaceBlock(Material.GLASS,player.getLocation().add(new Vector(0,1,0)),data.getInt("duration"));
        new BukkitRunnable() {

            @Override
            public void run() {
                deployed = false;
            }

        }.runTaskLater(SkillScheme.getInstance(), data.getInt("duration"));
        refreshCooldown();
    }


    @Override
    public void onSuffocationDamageSelf(EntityDamageEvent event){
        if(deployed){
            event.setDamage(0);
        }
    }

}
