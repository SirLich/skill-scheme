package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.ActiveSkill;
import org.bukkit.entity.Fireball;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class ClassicFireball extends ActiveSkill
{
    private static String id = "ClassicFireball";
    private static List<Integer> cooldown = getYaml(id).getIntegerList("values.cooldown");
    private static List<Float> yield = getYaml(id).getFloatList("values.yield");

    public ClassicFireball(RpgPlayer rpgPlayer,int level){
        super(rpgPlayer,level,cooldown.get(level));
        setId("ClassicFireball");
        setName("Classic Fireball");
        addLoreLine("Blow up your enimies pew-pew");
    }

    @Override
    public void onAxeRightClick(PlayerInteractEvent entityEvent){
        if(isCooldown()){return;}
        Fireball f = getRpgPlayer().getPlayer().launchProjectile(Fireball.class);
        f.setIsIncendiary(false);
        f.setGlowing(true);
        f.setYield(yield.get(getLevel()));
        refreshCooldown();
    }
}
