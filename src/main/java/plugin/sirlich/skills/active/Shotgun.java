package plugin.sirlich.skills.active;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.CooldownSkill;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class Shotgun extends CooldownSkill {
    public Shotgun(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"Shotgun");
    }

    @Override
    public void onBowFire(EntityShootBowEvent event){
        Vector velocity = event.getProjectile().getVelocity();
        ArmorStand stand = (ArmorStand) event.getProjectile().getWorld().spawnEntity(event.getProjectile().getLocation(),EntityType.ARMOR_STAND);
        stand.setItemInHand(new ItemStack(Material.STONE_SWORD));
        stand.setInvulnerable(true);
        stand.setVisible(false);
        stand.setCollidable(false);
        stand.setRightArmPose(new EulerAngle(0,velocity.getY(),0));
        //Entity entity = event.getProjectile().getWorld().dropItem(event.getProjectile().getLocation(),new ItemStack(Material.STONE_SWORD));
        event.getProjectile().addPassenger(stand);
    }
}
