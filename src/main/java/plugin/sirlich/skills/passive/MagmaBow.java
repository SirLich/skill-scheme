package main.java.plugin.sirlich.skills.passive;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.RpgPlayerList;
import main.java.plugin.sirlich.skills.meta.CooldownSkill;
import main.java.plugin.sirlich.skills.meta.Skill;
import main.java.plugin.sirlich.skills.meta.SkillType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class MagmaBow extends Skill {

    //This all looks great!
    private static String id = "MagmaBow";
    private static List<Double> chance = getYaml(id).getDoubleList("values.chance");
    private static List<Integer> fireTicks = getYaml(id).getIntegerList("values.fireTicks");

    //This looks good as well
    public MagmaBow(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"MagmaBow");
    }

    /*
    The issue here is that you ask about a scoreboard tag which you don't set. If you wanted to power
    this skill through scoreboard tags, you would have had to set the scoreboard tag. See onArrowFire in PhantomArrows
    for an example.

    Secondly, we don't actually need to use Scoreboard tags at all (and if we can, we should avoid it).

    Remember: the method onArrowHitEntity you've written bellow will ONLY get called if the player has this skill equiped.
    That means that as SOON as an arrow hits somebody, you know that its a magma arrow. Since this skill effects ALL arrows,
    you don't need any checks (beyond just the check for chance).
     */
    @Override
    public void onArrowHitEntity(ProjectileHitEvent event){
        Entity entity = event.getHitEntity();

        //We can directly cast, since the method we override already does a lot of error checking for us.
        LivingEntity livingEntity = (LivingEntity) entity;

        //Use of random is good here
        Random rand = new Random();

        //simplified if, based on comments above
        if(rand.nextInt() <= chance.get(getLevel())){

            //We are ADDING fire ticks, so you have to get fire ticks first, then add the new ones.
            livingEntity.setFireTicks(livingEntity.getFireTicks() + fireTicks.get(getLevel()));
        }
    }

}
