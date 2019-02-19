package main.java.plugin.sirlich.skills.meta;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgArrow;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.core.RpgPlayerList;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.io.File;
import java.util.ArrayList;

public class Skill
{
    private String name;
    private String id;
    private int cost;
    private int maxLevel;
    private ArrayList<String> description = new ArrayList<String>();

    private RpgPlayer rpgPlayer;
    private int level;


    public Skill(RpgPlayer rpgPlayer, int level, String id){
        this.id = id;
        this.rpgPlayer = rpgPlayer;
        this.level = level;
        this.cost = getYaml(id).getInt("values.cost");
        this.maxLevel = getYaml(id).getInt("values.maxLevel");
        this.name = getYaml(id).getString("values.name");
    }


    public void onEnable(){
    }

    public void onDisable(){
    }


    public RpgPlayer getRpgPlayer(){
        return this.rpgPlayer;
    }

    public int getCost()
    {
        return cost;
    }

    public String getName()
    {
        return this.name;
    }
    public String getId()
    {
        return this.id;
    }

    public ArrayList<String> getDescription(int level){
        return this.description;
    }

    public int getLevel()
    {
        return this.level;
    }
    public void setLevel(int level)
    {
        this.level = level;
    }

    public int getMaxLevel()
    {
        return maxLevel;
    }


    public static FileConfiguration getYaml(String id){
        File playerYml = new File(SkillScheme.getInstance().getDataFolder() + "/skills/" + id + ".yml");
        return YamlConfiguration.loadConfiguration(playerYml);
    }

    public void onFallDamageSelf(EntityDamageEvent event){

    }

    public void onExplosionDamageSelf(EntityDamageEvent event){

    }

    public void onSuffocationDamageSelf(EntityDamageEvent event){

    }

    public void onArrowHitEntity(ProjectileHitEvent event){

    }

    public void onArrowHitSelf(EntityDamageByEntityEvent event){

    }

    public void onArrowHitGround(ProjectileHitEvent event){

    }

    public void onBowFirePRE_EVENT(EntityShootBowEvent event){
        RpgArrow.registerArrow((Arrow)event.getProjectile(), RpgPlayerList.getRpgPlayer((Player)event.getEntity()));
        onBowFire(event);
    }

    public void onBowFire(EntityShootBowEvent event){

    }

    public void onSwordMeleeAttackSelf(EntityDamageByEntityEvent event){

    }

    public void onAxeMeleeAttackSelf(EntityDamageByEntityEvent event){

    }

    public void onMeleeAttackSelf(EntityDamageByEntityEvent event){

    }

    public void onSwordMeleeAttackOther(EntityDamageByEntityEvent event){

    }

    public void onAxeMeleeAttackOther(EntityDamageByEntityEvent event){

    }

    public void onBowMeleeAttack(EntityDamageByEntityEvent event){

    }

    public void onBowLeftClick(PlayerInteractEvent event){

    }

    public void onBowRightClickEvent(PlayerInteractEvent event){

    }

    public void onSwordRightClick(PlayerInteractEvent event){

    }

    public void onAxeRightClick(PlayerInteractEvent event){

    }

    public void onSwap(PlayerSwapHandItemsEvent event){

    }
}
