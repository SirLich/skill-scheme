package plugin.sirlich.skills.oc;

import plugin.sirlich.core.RpgProjectile;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.TickingSkill;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import plugin.sirlich.skills.triggers.Trigger;

import java.util.List;

public class PoisonDarts extends TickingSkill
{
    private static String id = "PoisonDarts";
    private static List<Integer> maxDarts = getYaml(id).getIntegerList("values.maxDarts");
    private static List<Integer> poisonDuration = getYaml(id).getIntegerList("values.poisonDuration");

    private static int charges;

    public PoisonDarts(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level, "PoisonDarts");
        charges = 0;
    }


    @Override
    public void onArrowHitGround(ProjectileHitEvent event){
        RpgProjectile rpgArrow = RpgProjectile.getProjectile(event.getEntity().getUniqueId());
        if(rpgArrow.hasTag("POISON_DART")){
            event.getEntity().remove();
        }
    }


    public void onArrowHitEntity(ProjectileHitEvent event){
        Entity entity = event.getHitEntity();
        RpgProjectile rpgArrow = RpgProjectile.getProjectile((Arrow) event.getEntity());
        if(entity instanceof LivingEntity && rpgArrow.hasTag("POISON_DART")){
            LivingEntity livingEntity = (LivingEntity) entity;
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.POISON,poisonDuration.get(getLevel()),1),true);
        }
    }

    @Override
    public void onBowLeftClick(Trigger event){
        if(charges <= 0){
            getRpgPlayer().tell("You don't enough enough darts.");
        } else {
            Arrow arrow = event.getSelf().launchProjectile(Arrow.class);
            arrow.setVelocity(arrow.getVelocity().multiply(0.5));
            RpgProjectile.registerProjectile(arrow,RpgPlayer.getRpgPlayer(event.getSelf()));
            RpgProjectile rpgArrow = RpgProjectile.getProjectile(arrow.getUniqueId());
            rpgArrow.addTag("POISON_DART");
            charges--;
            getRpgPlayer().tell(ChatColor.GREEN + "Poison Darts: " + ChatColor.GRAY + charges);
        }
    }

    @Override
    public void onTick(){
        if(charges < maxDarts.get(getLevel())){
            charges ++;
            getRpgPlayer().tell(ChatColor.GREEN + "Poison Darts: " + ChatColor.GRAY + charges);
        }
    }
}
