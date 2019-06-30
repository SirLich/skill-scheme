package main.java.plugin.sirlich.skills.meta;

import main.java.plugin.sirlich.SkillScheme;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class SkillData {

    private static HashMap<String, ArrayList<Double>> skillDataMap = new HashMap<String, ArrayList<Double>>();

    public static void initializeSkillData(){
        File dir = new File(SkillScheme.getInstance().getDataFolder() + "/skills/");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                handleSkill(child);
            }
        }
    }

    public static void handleSkill(File file){
        String fname = file.getName();
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        if(yaml.contains("values")){
            for(String value : yaml.getConfigurationSection("values").getKeys(false)){
                String key = fname.replace(".yml","") + "_" + value;
                skillDataMap.put(key, (ArrayList) yaml.getDoubleList("values." + value));
            }
        }
    }

    public static Double getSkillValue(String skill, String data, int index){
        String key = skill + "_" + data;
        return skillDataMap.get(key).get(index);
    }
}
