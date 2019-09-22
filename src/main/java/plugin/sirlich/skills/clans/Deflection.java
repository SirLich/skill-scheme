package plugin.sirlich.skills.clans;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.CooldownSkill;
import plugin.sirlich.utilities.c;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Deflection extends CooldownSkill
{
    public Deflection(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"Deflection");
    }

    @Override
    public void onMeleeAttackSelf(EntityDamageByEntityEvent event){
        Player self = (Player) event.getEntity();
        if(self.isBlocking()){
            if(isCooldown()){return;}
            RpgPlayer.getRpgPlayer(self).playSound(Sound.BAT_HURT);
            self.setVelocity(new Vector(self.getLocation().getDirection().multiply(-data.getDouble("power")).getX(), 0.4, self.getLocation().getDirection().multiply(-data.getDouble("power")).getZ()));
            refreshCooldown();
        }
    }
}
