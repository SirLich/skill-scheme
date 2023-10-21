package dev.sirlich.skillscheme.utilities;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import dev.sirlich.skillscheme.SkillScheme;

import java.io.File;
import java.util.HashMap;

public class Xliff {

    private static HashMap<String, String> xliffDataMap = new HashMap<String, String>();
    private static HashMap<String, Sound> soundDataMap = new HashMap<String, Sound>();

    public static void initializeData(){
        System.out.println("Initializing Xliff data:");
        File file = new File(SkillScheme.getInstance().getDataFolder() + "/global_xliff.yml");
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        System.out.println("Handling xliffs:");
        //Handle xliff
        if(yaml.contains("xliffs")){
            for(String outer_key : yaml.getConfigurationSection("xliffs").getKeys(false)){
                System.out.println(" - Loading: " + outer_key);
                for(String value : yaml.getConfigurationSection("xliffs." + outer_key).getKeys(false)){
                    xliffDataMap.put(outer_key + "." + value, yaml.getString("xliffs." + outer_key + "." + value));
                }
            }
        }

        System.out.println("Handling sounds:");
        //Handle sounds
        if(yaml.contains("sounds")){
            for(String outer_key : yaml.getConfigurationSection("sounds").getKeys(false)){
                System.out.println(" - Loading: " + outer_key);
                for(String value : yaml.getConfigurationSection("sounds." + outer_key).getKeys(false)){
                    soundDataMap.put(outer_key + "." + value, Sound.valueOf(yaml.getString("sounds." + outer_key + "." + value)));
                }
            }
        }
    }

    public static Sound getSound(String code){
        return soundDataMap.get(code);
    }

    //This logic here is a bit yucky, but I wrote it to handle the case where somebody is using the default value.
    public static String getXliff(String code){
        if(xliffDataMap.containsKey(code)){
            return xliffDataMap.get(code);
        } else {
            code = "default." + code;
            if(xliffDataMap.containsKey(code)){
                return xliffDataMap.get(code);
            } else {
                System.out.println("No key found of type: " + code);
                System.out.println("All codes: "  + xliffDataMap.toString());
                return "UNDEFINED KEY: " + code;
            }
        }
    }
}
