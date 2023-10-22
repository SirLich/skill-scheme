package dev.sirlich.skillscheme.skills.clans.rogue.axe;

import dev.sirlich.skillscheme.skills.meta.TickingSkill;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.triggers.Trigger;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

public class Flash extends TickingSkill
{
    public Flash(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer,level,"Flash");
    }

    private int maxCharges = data.getInt("max_charges");
    private int charges = 0;

    @Override
    public void onAxeRightClick(Trigger event){
        if (charges > 0) {
            charges -= 1;
            getRpgPlayer().playSound(data.getSound("use_charge"));
            getRpgPlayer().tell(data.xliff("use_charge"));
            performFlash();
        }
    }

    private void performFlash(){
        Player p = getPlayer();
        double maxDistance = data.getInt("max_distance");

        RayTraceResult result = p.rayTraceBlocks(maxDistance, FluidCollisionMode.SOURCE_ONLY);

        Location finalLocation;
        if (result != null && result.getHitBlock() != null) {
            finalLocation = result.getHitPosition().toLocation(p.getWorld(), p.getYaw(), p.getPitch());
        }
        else {
            finalLocation = p.getLocation().clone().add(p.getLocation().getDirection().clone().multiply(maxDistance));
        }

        getPlayer().teleport(finalLocation);
    }

    @Override
    public void onTick(){
        if (charges < maxCharges) {
            charges += 1;
            getRpgPlayer().playSound(data.getSound("add_charge"));
            getRpgPlayer().tell(data.xliff("add_charge"));
        }
    }
}
