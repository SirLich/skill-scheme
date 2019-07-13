package main.java.plugin.sirlich.utilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WeaponUtils {

    public static boolean isWeapon(ItemStack itemStack){
        return isWeapon(itemStack.getType());
    }

    public static boolean isWeapon(Material material){
        return isMeleeWeapon(material) || material.equals(Material.BOW);
    }

    public static boolean isMeleeWeapon(ItemStack itemStack){
        return isMeleeWeapon(itemStack.getType());
    }
    public static boolean isMeleeWeapon(Material material){
        return isSword(material) || isAxe(material);
    }

    public static boolean isBow(ItemStack itemStack){
        return isBow(itemStack.getType());
    }

    public static boolean isBow(Material material){
        return material == Material.BOW;
    }

    public static boolean isSword(ItemStack itemStack){
        return isSword(itemStack.getType());
    }

    public static boolean isSword(Material material){
        return material.equals(Material.WOOD_SWORD) ||
                material.equals(Material.STONE_SWORD) ||
                material.equals(Material.IRON_SWORD) ||
                material.equals(Material.DIAMOND_SWORD)||
                material.equals(Material.GOLD_SWORD);
    }

    public static boolean isAxe(ItemStack itemStack){
        return isAxe(itemStack.getType());
    }

    public static boolean isAxe(Material material){
        return material.equals(Material.WOOD_AXE) ||
                material.equals(Material.STONE_AXE) ||
                material.equals(Material.IRON_AXE) ||
                material.equals(Material.DIAMOND_AXE) ||
                material.equals(Material.GOLD_AXE);
    }
}
