package plugin.sirlich.skills.meta;

import plugin.sirlich.SkillScheme;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class SkillData {
    //Non Static stuff
    Skill skill;

    public SkillData(Skill skill){
        this.skill = skill;
    }

    //Data storage for cached skill data
    private static HashMap<String, ArrayList<Double>> skillDataMap = new HashMap<String, ArrayList<Double>>();
    private static HashMap<String, String> xliffDataMap = new HashMap<String, String>();
    private static HashMap<String, Sound> soundDataMap = new HashMap<String, Sound>();
    private static HashMap<ClassType, Integer> pointsMap = new HashMap<ClassType,Integer>();

    //Init method for pulling data from the yaml files
    public static void initializeData(){
        File dir = new File(SkillScheme.getInstance().getDataFolder() + "/skills/");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                handleSkill(child);
            }
        }

        //Load gui point values
        File guiFile = new File(SkillScheme.getInstance().getDataFolder() + "/gui.yml");
        FileConfiguration guiYml = YamlConfiguration.loadConfiguration(guiFile);
        for(String value : guiYml.getConfigurationSection("loadouts").getKeys(false)){
            System.out.println(value);
            pointsMap.put(ClassType.valueOf(value.toUpperCase()), guiYml.getInt("loadouts." + value + ".tokens"));
        }

    }

    //Internal method for handling a single file during init
    private static void handleSkill(File file){
        String fname = file.getName();
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        //Handle SkillData
        if(yaml.contains("values")){
            for(String value : yaml.getConfigurationSection("values").getKeys(false)){
                String key = fname.replace(".yml","") + "_" + value;
                skillDataMap.put(key, (ArrayList) yaml.getDoubleList("values." + value));
            }
        }

        //Handle Xliff data
        if(yaml.contains("xliff")){
            for(String value : yaml.getConfigurationSection("xliff").getKeys(false)){
                String key = fname.replace(".yml","") + "_" + value;
                xliffDataMap.put(key, yaml.getString("xliff." + value));
            }
        }

        //Handle sounds
        if(yaml.contains("sounds")){
            for(String value : yaml.getConfigurationSection("sounds").getKeys(false)){
                String key = fname.replace(".yml","") + "_" + value;
                soundDataMap.put(key, Sound.valueOf(yaml.getString("sounds." + value)));
            }
        }
    }

    //Getter for skill values
    private static Double value(String skill, String data, int index){
        String key = skill + "_" + data;
        try {
            return skillDataMap.get(key).get(index);
        } catch(NullPointerException e){
            System.out.println("NullPointerException in: " + key);
        }
        return -4.04;
    }

    public static String xliff(String skill, String code){
        String key = skill + "_" + code;
        if(xliffDataMap.containsKey(key)){
            return xliffDataMap.get(key);
        } else {
            System.out.println("No key found of type: " + key);
            System.out.println("All codes: "  + xliffDataMap.toString());
            return "UNDEFINED KEY: " + code;
        }
    }

    public static String xliff(Skill skill, String code){
        return xliff(skill.getId(),code);
    }

    public static Sound getSound(String skill, String code){
        String key = skill + "_" + code;
        return soundDataMap.get(key);
    }

    //Non static stuff for the built version
    public static Sound getSound(Skill skill, String code){
        return getSound(skill.getId(),code);
    }


    public Double getDouble(String code, int level){
        return value(skill.getId(), code, level);
    }

    public static Double getDouble(String id, String code, int level){
        return value(id,code,level);
    }
    public Double getDouble(String code){
        return value(skill.getId(),code,skill.getLevel());
    }

    public Integer getInt(String code, int level){
        return value(skill.getId(), code, level).intValue();
    }

    public Integer getInt(String code){
        return value(skill.getId(),code,skill.getLevel()).intValue();
    }

    public String xliff(String code){
        return xliff(skill.getId(),code);
    }

    public Sound getSound(String code){
        return getSound(skill.getId(),code);
    }

    public static Integer getDefaultPointData(ClassType classType){
        System.out.println(pointsMap.toString());
        return pointsMap.get(classType);
    }
}
