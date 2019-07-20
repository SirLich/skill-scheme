package main.java.plugin.sirlich.skills.meta;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Skill
{
    private String name;
    private String id;
    private int cost;
    private int maxLevel;
    private ArrayList<String> description = new ArrayList<String>();

    private RpgPlayer rpgPlayer;
    private int level;
    public SkillData data;


    public Skill(RpgPlayer rpgPlayer, int level, String id){
        description.add(c.dgray + "This skill does not have a description!");
        this.id = id;
        this.rpgPlayer = rpgPlayer;
        this.level = level;
        this.cost = getYaml(id).getInt("cost");
        this.maxLevel = getYaml(id).getInt("maxLevel");
        this.name = getYaml(id).getString("name");
        this.data = new SkillData(this);
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


    private static ArrayList<String> processDescription(ArrayList<String> description, String id, int level){
        ArrayList<String> newDescription = new ArrayList<String>();
        for(int i = 0; i < description.size(); i ++){
            newDescription.add(processDescriptionLine(description.get(i), id, level));
        }
        return newDescription;
    }


    private static String processDescriptionLine(String line, String id, int level){
        FileConfiguration yml = getYaml(id);
        String regex = "\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()){
            String match = matcher.group();
            String skillCode = match.replace("[","").replace("]","");
            String[] split = skillCode.split(" ");
            String skillName = split[0];
            Double skillValue = SkillData.value(id, skillName, level);
            if(skillValue == -4.04){
                return "! ERROR !";
            }
            if(split.length > 2){
                String operator = split[1];
                Double value = Double.parseDouble(split[2]);
                if(operator.equals("*")){
                    skillValue = skillValue * value;
                } else if(operator.equals("/")){
                    skillValue = skillValue / value;
                }
            }
            line = line.replace(match, c.green + skillValue.toString() + c.dgray);
        }
        return line;
    }

    public ArrayList<String> getDescription(int level){
        if(getYaml(id).contains("description")){
            return processDescription((ArrayList<String>) getYaml(id).getStringList("description"), id, level);
        } else {
            return description;
        }
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

    public void onItemDrop(PlayerDropItemEvent event){

    }

    public void onAxeDrop(PlayerDropItemEvent event){

    }

    public void onBowDrop(PlayerDropItemEvent event){

    }

    public void onSwordDrop(PlayerDropItemEvent event){

    }

    public void onBowFire(EntityShootBowEvent event){

    }

    public void onAxeMiss(PlayerInteractEvent event){

    }

    public void onSwordMiss(PlayerInteractEvent event){

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

    public void onDeath(PlayerDeathEvent event){

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
