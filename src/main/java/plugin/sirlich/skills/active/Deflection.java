package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.CooldownSkill;
import main.java.plugin.sirlich.utilities.c;
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
            RpgPlayer.getRpgPlayer(self).playSound(Sound.BLOCK_ANVIL_PLACE);
            self.setVelocity(new Vector(self.getLocation().getDirection().multiply(-data.getDouble("power")).getX(), 0.4, self.getLocation().getDirection().multiply(-data.getDouble("power")).getZ()));
            refreshCooldown();
        }
    }
}
