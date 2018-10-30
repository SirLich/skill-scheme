package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.RpgPlayerList;
import main.java.plugin.sirlich.skills.meta.ActiveSkill;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Deflection extends ActiveSkill
{
    private static String id = "Deflection";
    private static List<Integer> power = getYaml(id).getIntegerList("values.cooldown");

    public Deflection(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"Deflection");
    }

    @Override
    public ArrayList<String> getDescription(int level){
        ArrayList<String> lorelines = new ArrayList<String>();
        lorelines.add(c.dgray + "Retreat from fights at high velocity");
        lorelines.add(c.dgray + "by blocking melee attacks.");
        lorelines.add("");
        lorelines.add(c.aqua + "Sword-Block" + c.dgray + " melee attacks to fire");
        lorelines.add("");
        lorelines.add(c.dgray + "Cooldown: " + c.green + getCooldown()/20 + c.dgray + " seconds");
        lorelines.add(c.dgray + "Power: " + c.green + power.get(level) + c.dgray);
        return lorelines;
    }


    @Override
    public void onMeleeAttackSelf(EntityDamageByEntityEvent event){
        Player self = (Player) event.getEntity();
        Player attacker = (Player) event.getEntity();
        if(self.isBlocking()){
            if(isCooldown()){return;}
            RpgPlayerList.getRpgPlayer(self).playSound(Sound.BLOCK_ANVIL_PLACE);
            self.setVelocity(new Vector(self.getLocation().getDirection().multiply(-power.get(getLevel())).getX(), 0.4, self.getLocation().getDirection().multiply(-power.get(getLevel())).getZ()));
            refreshCooldown();
        }
    }
}
