package plugin.sirlich.skills.clans.Ranger;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.CooldownSkill;
import plugin.sirlich.utilities.WeaponUtils;
import plugin.sirlich.utilities.c;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Disengage extends CooldownSkill
{
    public Disengage(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"Disengage");
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

    @Override
    public boolean showActionBar(){
        return WeaponUtils.isSword(getRpgPlayer().getPlayer().getItemInHand());
    }
}
