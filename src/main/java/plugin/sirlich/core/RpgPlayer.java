package main.java.plugin.sirlich.core;

import main.java.plugin.sirlich.skills.meta.ClassType;
import main.java.plugin.sirlich.skills.meta.SkillType;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import main.java.plugin.sirlich.skills.meta.Skill;
import main.java.plugin.sirlich.skills.meta.SkillEditObject;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.util.*;

public class RpgPlayer
{

    /*
    RPGPLAYER LIST STUFF
     */
    public static HashMap<UUID, RpgPlayer> rpgPlayerHashMap = new HashMap<UUID, RpgPlayer>();
    public static HashMap<RpgPlayer, UUID> playerHashMap = new HashMap<RpgPlayer, UUID>();

    public static RpgPlayer getRpgPlayer(Player player) {
        return rpgPlayerHashMap.get(player.getUniqueId());
    }

    public static RpgPlayer getRpgPlayer(String name) {
        return rpgPlayerHashMap.get(Bukkit.getPlayer(name).getUniqueId());
    }

    public static RpgPlayer getRpgPlayer(UUID uuid) {
        return rpgPlayerHashMap.get(uuid);
    }

    public static Collection<RpgPlayer> getRpgPlayers() {
        return rpgPlayerHashMap.values();
    }

    public static Player getPlayer(RpgPlayer rpgPlayer) {
        return Bukkit.getPlayer(playerHashMap.get(rpgPlayer));
    }

    public static boolean isPlayer(Player player) {
        return rpgPlayerHashMap.containsKey(player.getUniqueId());
    }

    public static boolean isPlayer(UUID uuid) {
        return rpgPlayerHashMap.containsKey(uuid);
    }

    public static void addPlayer(Player player) {
        RpgPlayer rpgPlayer = new RpgPlayer(player);
        rpgPlayerHashMap.put(player.getUniqueId(), rpgPlayer);
        playerHashMap.put(rpgPlayer, player.getUniqueId());
    }

    public static void removePlayer(Player player) {
        RpgPlayer rpgPlayer = rpgPlayerHashMap.get(player.getUniqueId());
        rpgPlayer.clearSkills();
        rpgPlayer.setWalkSpeedModifier(0.2);
        playerHashMap.remove(rpgPlayer);
        rpgPlayerHashMap.remove(player.getUniqueId());
    }


    /*
    END RPGPLAYER LIST STUFF
     */

    private boolean modifierActive = false;

    public boolean isModifierActive(){
        return modifierActive;
    }

    public void setModifierActive(boolean a){
        modifierActive = a;
    }

    public int getMana(){
        return Math.round(getPlayer().getExp() * 100);
    }

    public void addMana(int mana){
        float newMana = getPlayer().getExp() + ((float)mana)/100;
        if(newMana < 0){
            newMana = 0;
        } else if(newMana > 1.0f){
            newMana = 1.0f;
        }
        getPlayer().setExp(newMana);
    }

    public RpgPlayer(Player player){
        this.skillEditObject = new SkillEditObject(ClassType.UNDEFINED, this);
        this.player = player;
        this.team = "Default";
        this.playerState = PlayerState.TESTING;
    }


    private PlayerState playerState;
    private String team;
    private double walkSpeedModifier;

    private SkillEditObject skillEditObject;


    public void refreshPassiveModifiers(){
        float walkSpeed = (float) walkSpeedModifier + 0.2f;
        if(walkSpeed <= 0){
            getPlayer().setWalkSpeed(0);
        } else if(walkSpeed >= 1){
            getPlayer().setWalkSpeed(1);
        } else {
            getPlayer().setWalkSpeed(walkSpeed);
        }
    }

    private HashMap<SkillType, Skill> skillList = new HashMap<SkillType, Skill>();

    public HashMap<SkillType, Skill> getSkillList(){
        return skillList;
    }

    public boolean hasSkill(SkillType skillType){
        return skillList.containsKey(skillType);
    }

    public Skill getSkill(SkillType skillType){
        return skillList.get(skillType);
    }

    public void addSkill(SkillType skillType, int level){
        try{
            Class clazz = skillType.getSkillClass();
            Constructor<Skill> constructor = clazz.getConstructor(RpgPlayer.class,int.class);
            Skill skill = (Skill) constructor.newInstance(this,level);
            skill.onEnable();
            skillList.put(skillType,skill);
            refreshPassiveModifiers();
        } catch (Exception e){
            System.out.println("WARNING! Something terrible has occurred in the reflection.");
        }


    }

    public void addEffect(PotionEffectType potionEffectType, int level, int ticks){
        Collection<PotionEffect> effects = getPlayer().getActivePotionEffects();

        //Search through effects for duplicates
        for(PotionEffect effect : effects){

            //Found a duplicate
            if(effect.getType() == potionEffectType){

            }
        }

    }

    public void removeAllPotionEffects(){
        for (PotionEffect effect : player.getActivePotionEffects()){
            player.removePotionEffect(effect.getType());
        }
    }
    public void wipe(){
        getPlayer().getInventory().clear();
        getPlayer().setHealth(20);
        getPlayer().setExp(0);
        getPlayer().setFoodLevel(20);
        removeAllPotionEffects();
        clearSkills();
    }

    public String getName(){
        return getPlayer().getName();
    }

    public void teleport(Location location){
        location.setWorld(getPlayer().getLocation().getWorld());
        getPlayer().teleport(location);
    }
    public void removeSkill(SkillType skillType){
        if(skillList.containsKey(skillType)){
            skillList.get(skillType).onDisable();
            skillList.remove(skillType);
        }
        refreshPassiveModifiers();
    }

    public void clearSkills(){
        for(Skill skill : skillList.values()){
            skill.onDisable();
        }
        refreshPassiveModifiers();
        skillList.clear();
    }

    public void playSound(Sound sound){
        getPlayer().playSound(getPlayer().getLocation(),sound,1,1);
    }


    public Player getPlayer()
    {
        return player;
    }

    public void tell(String message){
        getPlayer().sendMessage(c.green + message);
    }

    //TODO Eventually add method here with Bucket.broadcastMessage()
    //public void say(String message){}

    private Player player;

    public double getWalkSpeedModifier()
    {
        return walkSpeedModifier;
    }

    public void setWalkSpeedModifier(double walkSpeedModifier)
    {
        this.walkSpeedModifier = walkSpeedModifier;
    }

    public void editWalkSpeedModifier(double change){
        this.walkSpeedModifier += change;
        refreshPassiveModifiers();
    }

    public SkillEditObject getSkillEditObject()
    {
        return skillEditObject;
    }

    public void refreshSkillEditObject(ClassType classType)
    {
        this.getSkillEditObject().clearSkills();
        this.getSkillEditObject().setClassType(classType);
    }

    public PlayerState getPlayerState()
    {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState)
    {
        this.playerState = playerState;

        if(playerState == PlayerState.HUB){
            wipe();
        } else if(playerState == PlayerState.SPECTATOR){
            wipe();
        } else if(playerState == PlayerState.LOBBY){
            wipe();
        } else if(playerState == PlayerState.GAME || playerState == PlayerState.TESTING){
            getRpgPlayer(getPlayer()).getSkillEditObject().addSkills(true);
            getRpgPlayer(getPlayer()).getSkillEditObject().giveLoadout();
        }
    }

    public static boolean isSameTeam(RpgPlayer a, RpgPlayer b){
        return a.getTeam().equals(b.getTeam());
    }

    public String getTeam()
    {
        return team;
    }


    public void setTeam(String team)
    {
        this.team = team;
    }

    public Arrow shootArrow(Vector velocity) {
        return shootArrow(velocity,null);
    }

    public Arrow shootArrow(Vector velocity, String tag){
        Arrow arrow = this.getPlayer().launchProjectile(Arrow.class);
        arrow.setVelocity(velocity);
        RpgProjectile rpgProjectile = RpgProjectile.registerProjectile(arrow,this);
        if(tag != null){
            rpgProjectile.addTag(tag);
        }
        return arrow;
    }

}
