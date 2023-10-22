package dev.sirlich.skillscheme.skills.clans.rogue.passiveA;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.meta.ClassType;
import dev.sirlich.skillscheme.skills.meta.Skill;

public class Backstab extends Skill {
    public Backstab(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "Backstab");
    }

    private int acceptable_angle;
    private double damage_modifier;
    private double rogue_damage_reduction; //like 0.8 to do 80% normal damage.
    private Sound backstab_sound;

    public void initData(){
        super.initData();
        this.acceptable_angle = data.getInt("acceptable_angle");
        this.damage_modifier = data.getDouble("damage_modifier");
        this.backstab_sound = data.getSound("backstab_sound");
        this.rogue_damage_reduction = data.getDouble("rogue_damage_reduction");
    }


    // TODO: Handle acceptable angle
    public void onSwordMeleeAttackOther(EntityDamageByEntityEvent event){
        Player player = getRpgPlayer().getPlayer();
        Entity damager = event.getDamager();
        //Behind Player
        if (true) {
            event.setDamage(event.getDamage() * damage_modifier);
            getRpgPlayer().playWorldSound(backstab_sound);
            if(damager instanceof Player){
                RpgPlayer rpgDamager = RpgPlayer.getRpgPlayer(damager.getUniqueId());
                if(rpgDamager.getClassType() == ClassType.ROGUE){
                    event.setDamage(event.getDamage() * rogue_damage_reduction);
                }
            }
        }
    }
}
