package plugin.sirlich.skills.passive;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.Skill;
import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

public class TripleShot extends Skill {
    public TripleShot(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level, "TripleShot");
    }

    @Override
    public void onBowFire(EntityShootBowEvent event){
        if(event.getForce() == 1.0f){
            getRpgPlayer().playSound(data.sound("triple_shot"));
            getRpgPlayer().tell(data.xliff("triple_shot"));
            Vector velocity = event.getProjectile().getVelocity();
            Arrow arrow1 = getRpgPlayer().shootArrow(velocity);
            Arrow arrow2  = getRpgPlayer().shootArrow(velocity);
            System.out.println(velocity.toString());
            arrow1.setVelocity(velocity.multiply(new Vector(velocity.getX(),velocity.getY() + Math.random()/10,velocity.getZ())));
            arrow2.setVelocity(velocity.multiply(new Vector(velocity.getX(),velocity.getY() + Math.random()/10,velocity.getZ())));
            arrow1.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
            arrow2.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
        }
    }
}
