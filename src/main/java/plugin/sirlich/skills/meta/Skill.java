package main.java.plugin.sirlich.skills.meta;

import main.java.plugin.sirlich.SkillScheme;
import main.java.plugin.sirlich.core.RpgPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;

public class Skill
{
    private String name = "Undefined Skill";
    private String id = "undefined_skill";
    private int cost;
    private int maxLevel;
    private ArrayList<String> description = new ArrayList<String>();
    {
        this.description.add("Some lore line");
    }

    private RpgPlayer rpgPlayer;
    private int level;


    public Skill(RpgPlayer rpgPlayer, Integer level){
        this.rpgPlayer = rpgPlayer;
        this.level = level;
        this.cost = 1;
        this.maxLevel = 2;
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
    public void setCost(int cost)
    {
        this.cost = cost;
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

    public void clearDescription(){
        this.description.clear();
    }
    public void addLoreLine(String m){
        this.description.add(ChatColor.DARK_GRAY + m);
    }


    public void setName(String name_)
    {
        name = name_;
    }
    public void setId(String id_)
    {
        this.id = id_;
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

    public void setMaxLevel(int maxLevel)
    {
        this.maxLevel = maxLevel;
    }

    public static FileConfiguration getYaml(String id){
        File playerYml = new File(SkillScheme.getInstance().getDataFolder() + "/skills/" + id + ".yml");
        return YamlConfiguration.loadConfiguration(playerYml);
    }
}
