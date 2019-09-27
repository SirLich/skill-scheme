package plugin.sirlich.core;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.sirlich.SkillScheme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class RpgProjectile {

    //RpgProjectileList stuff
    public static HashMap<UUID, RpgProjectile> projectileMap = new HashMap<UUID, RpgProjectile>();
    public static RpgProjectile getProjectile(UUID uuid){
        return projectileMap.get(uuid);
    }
    public static RpgProjectile getProjectile(Arrow arrow){
        return projectileMap.get(arrow.getUniqueId());
    }
    public static RpgProjectile getProjectile(Projectile projectile){
        return projectileMap.get(projectile.getUniqueId());
    }

    private ArrayList<String> tags;
    private HashMap<String, Integer> intData = new HashMap<String, Integer>();
    private RpgPlayer shooter;
    private UUID id;

    public Integer getInt(String key){
        return intData.get(key);
    }

    public void setInt(String key, Integer num){
        intData.put(key, num);
    }

    public boolean hasKey(String key){
        return intData.containsKey(key);
    }



    //Add projectiles
    public static RpgProjectile registerProjectile(Arrow arrow, RpgPlayer shooter){
        return registerProjectile((Projectile) arrow, shooter);
    }
    public static RpgProjectile registerProjectile(Projectile arrow, RpgPlayer shooter){
        ArrayList<String> tags = new ArrayList<String>();
        RpgProjectile rpgArrow = new RpgProjectile(arrow.getUniqueId(),shooter,tags);
        projectileMap.put(arrow.getUniqueId(),rpgArrow);
        return  rpgArrow;
    }

    //Remove projectiles
    public static void deregisterProjectile(Arrow arrow){
        projectileMap.remove(arrow.getUniqueId());
    }
    public static void deregisterProjectile(Projectile projectile){
        projectileMap.remove(projectile.getUniqueId());
    }
    public void deregisterSelf(){
        final UUID id = this.id;
        new BukkitRunnable() {

            @Override
            public void run() {
                projectileMap.remove(id);

            }

        }.runTaskLater(SkillScheme.getInstance(), 5);
    }

    public static void addTag(UUID uuid,String tag){
        RpgProjectile.getProjectile(uuid).tags.add(tag);
    }

    public void addTag(String tag){
        this.tags.add(tag);
    }

    public static boolean hasProjectile(Projectile projectile){
        return projectileMap.containsKey(projectile.getUniqueId());
    }

    public RpgPlayer getShooter(){
        return this.shooter;
    }

    public ArrayList<String> getTags(){
        return tags;
    }

    public UUID getId(){
        return this.id;
    }

    public boolean hasTag(String tag){
        return tags.contains(tag);
    }

    //Handle constructor from both registerProjectile types
    public RpgProjectile(UUID arrowID, RpgPlayer shooter, ArrayList<String> tags){
        this.id = arrowID;
        this.tags = tags;
        this.shooter = shooter;
    }
}
