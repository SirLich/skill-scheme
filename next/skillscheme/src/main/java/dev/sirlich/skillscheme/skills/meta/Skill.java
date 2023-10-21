package dev.sirlich.skillscheme.skills.meta;

// import com.massivecraft.factions.Board;
// import com.massivecraft.factions.FLocation;
// import com.massivecraft.factions.Faction;

import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import dev.sirlich.skillscheme.SkillScheme;
import dev.sirlich.skillscheme.core.RpgPlayer;
import dev.sirlich.skillscheme.skills.triggers.Trigger;
import dev.sirlich.skillscheme.utilities.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Skill
{
    /*
    Required config values:
    - cost: int
    - max_level: int
    - name: string

    Optional config values:
    - description
    - various values, nested under values
    - various sounds, nested under sounds
    - various text-lines, nested under xliff
     */
    private String name;
    private String id;
    private int cost;
    private int maxLevel;
    private UUID sessionToken;
    private ArrayList<String> description = new ArrayList<String>();

    private RpgPlayer rpgPlayer;
    private int level;
    public SkillData data;



    public Skill(RpgPlayer rpgPlayer, int level, String id){
        description.add(Color.dgray + "This skill does not have a description!");
        if(rpgPlayer != null){
            this.sessionToken = rpgPlayer.getSessionToken();
        }
        //TODO These uses of getYml really need to be removed!
        this.id = id;
        this.rpgPlayer = rpgPlayer;
        this.level = level;
        this.cost = getYaml(id).getInt("cost");
        this.maxLevel = getYaml(id).getInt("max_level");
        this.name = getYaml(id).getString("name");
        this.data = new SkillData(this);
        initData();
    }

    /*
    This is an abstraction for anything that might block users ability to use their skills.
    Fundamental checks will be done for some cases, and individual skills/skill types can
    add their own checks.

    This should always be called as:

    if(SkillCheck() or super().SkillCheck()){return}

    Throwing TRUE will mean the skill isn't run!
    */
    public boolean skillCheck(){
        //Check for silenced
        if(getRpgPlayer().isSilenced()){
            getRpgPlayer().tell("You cannot used that skill when silenced");
            return true;
        }

        // TODO: Re-add check for factions
        // //Check for in safe zone
        // try {
        //     FLocation fLoc = new FLocation(getRpgPlayer().getPlayer().getLocation());
        //     Faction faction = Board.getInstance().getFactionAt(fLoc);
        //     if(faction.isSafeZone()){
        //         getRpgPlayer().tell("You cannot used that skill when in safe-zone");
        //         return true;
        //     }
        // } catch(Exception e){
        //     System.out.println("WARNING: Unable to check safezone safegaurd. Are you running the Factions plugin?");
        // }


        //The skill check passed!
        return false;
    }

    //Please call super.initData() if you want to use this method!
    public void initData(){

    }

    public void onEnable(){
    }

    public void onDisable(){

    }

    public RpgPlayer getRpgPlayer(){
        return this.rpgPlayer;
    }

    /**
     * @return The Player that owns this skill.
     */
    public Player getPlayer() {
        return getRpgPlayer().getPlayer();
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

    public UUID getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(UUID sessionToken) {
        this.sessionToken = sessionToken;
    }

    private static ArrayList<String> processDescription(ArrayList<String> description, String id, int level){
        ArrayList<String> newDescription = new ArrayList<String>();
        for(int i = 0; i < description.size(); i ++){
            newDescription.add(processDescriptionLine(description.get(i), id, level));
        }
        return newDescription;
    }

    public boolean isSilenced() {
        boolean silenced = this.getRpgPlayer().isSilenced();
        if(silenced){
            this.rpgPlayer.tellX("Skill.cannot_use_skill_when_silenced");
            this.rpgPlayer.playSoundX("Skill.cannot_use_skill_when_silenced");
        }
        return silenced;
    }

    public boolean isInWater() {
        boolean inWater = this.getRpgPlayer().getPlayer().getLocation().getBlock().isLiquid();
        if(inWater){
            this.rpgPlayer.tellX("Skill.cannot_use_skill_when_in_water");
            this.rpgPlayer.playSoundX("Skill.cannot_use_skill_when_in_water");
        }
        return inWater;
    }

    private static String processDescriptionLine(String line, String id, int level){
        String regex = "\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()){
            String match = matcher.group();
            String skillCode = match.replace("[","").replace("]","");
            String[] split = skillCode.split(" ");
            String skillName = split[0];
            Double skillValue = SkillData.getDouble(id, skillName, level);
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
            line = line.replace(match, Color.green + skillValue.toString() + Color.dgray);
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

    public void onDamageSelf(EntityDamageEvent event){

    }

    public void onFallDamageSelf(EntityDamageEvent event){

    }

    public void onExplosionDamageSelf(EntityDamageEvent event){

    }

    public void onSuffocationDamageSelf(EntityDamageEvent event){

    }

    public void onArrowHitEntity(EntityDamageByEntityEvent event){

    }

    public void onArrowHitSelf(EntityDamageByEntityEvent event){

    }

    /**
     * Called when an arrow hits the ground.
     */
    public void onArrowHitGround(ProjectileHitEvent event){

    }

    public void onItemDrop(PlayerDropItemEvent event){

    }

    public void onItemPickup(PlayerPickupItemEvent event){

    }

    public void onSwap(PlayerSwapHandItemsEvent event){

    }

    public void onItemPickupOther(PlayerPickupItemEvent event){

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

    public void onMeleeAttackOther(EntityDamageByEntityEvent event){

    }

    public void onAxeMeleeAttackOther(EntityDamageByEntityEvent event){

    }

    public void onDeath(PlayerDeathEvent event){

    }

    public void onBowMeleeAttack(EntityDamageByEntityEvent event){

    }

    public void onBowLeftClick(Trigger event){

    }

    public void onLeftClick(Trigger event){

    }

    public void onBowRightClickEvent(Trigger event){

    }

    public void onSwordRightClick(Trigger event){

    }

    public void onAxeRightClick(Trigger event){

    }
}
