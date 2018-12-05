package main.java.plugin.sirlich.core;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.plugin.sirlich.skills.meta.ClassType;
import main.java.plugin.sirlich.skills.meta.SkillType;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import main.java.plugin.sirlich.skills.meta.Skill;
import main.java.plugin.sirlich.skills.meta.SkillEditObject;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Constructor;
import java.util.*;

public class RpgPlayer
{
    private PlayerState playerState;
    private String team;
    private double walkSpeedModifier;
    private String arena;

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
            System.out.println(clazz.getName());
            Constructor<Skill> constructor = clazz.getConstructor(RpgPlayer.class,int.class);
            Skill skill = (Skill) constructor.newInstance(this,level);
            skill.onEnable();
            skillList.put(skillType,skill);
            refreshPassiveModifiers();
        } catch (Exception e){
            System.out.println("WARNING! SOMETHING TERRIBLE HAPPENED IN THE REFLECTION. YAYYY");
        }


    }

    public void wipe(){
        getPlayer().getInventory().clear();
        getPlayer().setHealth(20);
        getPlayer().setExp(0);
        getPlayer().setFoodLevel(20);
    }

    public void teleport(Location location){
        location.setWorld(getPlayer().getLocation().getWorld());

        System.out.println(location.toString());
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
            System.out.println("Removing " + skill.getName());
            skill.onDisable();
        }
        refreshPassiveModifiers();
        skillList.clear();
    }

    public void playSound(Sound sound){
        getPlayer().playSound(getPlayer().getLocation(),sound,1,1);
    }
    public RpgPlayer(Player player){
        this.skillEditObject = new SkillEditObject(ClassType.UNDEFINED, this);
        this.player = player;
        this.team = "Default";
        this.playerState = PlayerState.TESTING;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void chat(String m){
        getPlayer().sendMessage(c.green + m);
    }

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
        wipe();
        if(playerState == PlayerState.HUB){
            clearSkills();
            getPlayer().setGameMode(GameMode.ADVENTURE);
            ItemStack itemStack = new ItemStack(Material.IRON_AXE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(c.red  + "Team Death Match");
            itemStack.setItemMeta(itemMeta);
            NBTItem item = new NBTItem(itemStack);
            item.addCompound("TEAM_DEATH_MATCH_QUEUE");
            itemStack = item.getItem();
            getPlayer().getInventory().setItem(4,itemStack);
        } else if(playerState == PlayerState.SPECTATOR){
            getPlayer().setGameMode(GameMode.SPECTATOR);
            clearSkills();
        } else if(playerState == PlayerState.LOBBY){
            clearSkills();
            getPlayer().setGameMode(GameMode.ADVENTURE);
        } else if(playerState == PlayerState.GAME){
            getPlayer().setGameMode(GameMode.SURVIVAL);
        }
    }

    public static boolean sameTeam(RpgPlayer a, RpgPlayer b){
        return a.getTeam().equals(b.getTeam());
    }

    public String getTeam()
    {
        return team;
    }

    public String getArena()
    {
        return arena;
    }

    public void setArena(String arena)
    {
        this.arena = arena;
    }

    public void setTeam(String team)
    {
        this.team = team;
    }
}
