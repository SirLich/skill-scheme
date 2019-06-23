package main.java.plugin.sirlich.utilities;

import org.bukkit.Material;

public class WeaponUtils {

    public static boolean isWeapon(Material material){
        return isMeleeWeapon(material) || material.equals(Material.BOW);
    }

    public static boolean isMeleeWeapon(Material material){
        return isSword(material) || isAxe(material);
    }

    public static boolean isSword(Material material){
        return material.equals(Material.WOOD_SWORD) ||
                material.equals(Material.STONE_SWORD) ||
                material.equals(Material.IRON_SWORD) ||
                material.equals(Material.DIAMOND_SWORD)||
                material.equals(Material.GOLD_SWORD);
    }

    public static boolean isAxe(Material material){
        return material.equals(Material.WOOD_AXE) ||
                material.equals(Material.STONE_AXE) ||
                material.equals(Material.IRON_AXE) ||
                material.equals(Material.DIAMOND_AXE) ||
                material.equals(Material.GOLD_AXE);
    }
}
