package plugin.sirlich.utilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.skills.meta.ClassType;

public class WeaponUtils {

    public static boolean isWeapon(ItemStack itemStack){
        return isWeapon(itemStack.getType());
    }

    public static boolean isWeapon(Material material){
        return isMeleeWeapon(material) || material.equals(Material.BOW);
    }

    public static boolean isWearingFullSet(Player player){
        return getClassTypeFromArmor(player) != ClassType.UNDEFINED;
    }

    public static ClassType getClassTypeFromArmor(Player player){
        PlayerInventory inventory = player.getInventory();
        try{
            if(inventory.getChestplate().getType() == Material.DIAMOND_CHESTPLATE &&
            inventory.getLeggings().getType() == Material.DIAMOND_LEGGINGS &&
            inventory.getBoots().getType() == Material.DIAMOND_BOOTS){
                return ClassType.PALADIN;
            }else

            if(inventory.getChestplate().getType() == Material.GOLDEN_CHESTPLATE &&
                    inventory.getLeggings().getType() == Material.GOLDEN_LEGGINGS &&
                    inventory.getBoots().getType() == Material.GOLDEN_BOOTS){
                return ClassType.WARLOCK;
            } else

            if(inventory.getChestplate().getType() == Material.CHAINMAIL_CHESTPLATE &&
                    inventory.getLeggings().getType() == Material.CHAINMAIL_LEGGINGS &&
                    inventory.getBoots().getType() == Material.CHAINMAIL_BOOTS){
                return ClassType.RANGER;
            }else

            if(inventory.getChestplate().getType() == Material.LEATHER_CHESTPLATE &&
                    inventory.getLeggings().getType() == Material.LEATHER_LEGGINGS &&
                    inventory.getBoots().getType() == Material.LEATHER_BOOTS){
                return ClassType.ROGUE;
            }else

            if(inventory.getChestplate().getType() == Material.IRON_CHESTPLATE &&
                    inventory.getLeggings().getType() == Material.IRON_LEGGINGS &&
                    inventory.getBoots().getType() == Material.IRON_BOOTS){
                return ClassType.FIGHTER;
            } else {
                return ClassType.UNDEFINED;
            }
        }


        //Its ok to catch this -its just a null catch for null armor.
        catch(Exception e){
            return ClassType.UNDEFINED;
        }
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
        return material.equals(Material.WOODEN_SWORD) ||
                material.equals(Material.STONE_SWORD) ||
                material.equals(Material.IRON_SWORD) ||
                material.equals(Material.DIAMOND_SWORD)||
                material.equals(Material.GOLDEN_SWORD);
    }

    public static boolean isAxe(ItemStack itemStack){
        return isAxe(itemStack.getType());
    }

    public static boolean isAxe(Material material){
        return material.equals(Material.WOODEN_AXE) ||
                material.equals(Material.STONE_AXE) ||
                material.equals(Material.IRON_AXE) ||
                material.equals(Material.DIAMOND_AXE) ||
                material.equals(Material.GOLDEN_AXE);
    }

    public static void giveLoadout(RpgPlayer rpgPlayer, ClassType classType){
        Player player = rpgPlayer.getPlayer();
        player.getInventory().clear();
        if(classType == ClassType.PALADIN){
            player.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
            player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
            player.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
            player.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
            player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
            player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
        } else if(classType == ClassType.FIGHTER){
            player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
            player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
            player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
            player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
            player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
            player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
        }  else if(classType == ClassType.RANGER){
            player.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
            player.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
            player.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
            player.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
            player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
            player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
            player.getInventory().addItem(new ItemStack(Material.BOW));
            player.getInventory().addItem(new ItemStack(Material.ARROW,64));

        } else if(classType == ClassType.ROGUE){
            player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
            player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
            player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
            player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
            player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
            player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
            player.getInventory().addItem(new ItemStack(Material.BOW));
            player.getInventory().addItem(new ItemStack(Material.ARROW,64));
        } else if(classType == ClassType.WARLOCK){
            player.getInventory().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
            player.getInventory().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
            player.getInventory().setLeggings(new ItemStack(Material.GOLDEN_LEGGINGS));
            player.getInventory().setBoots(new ItemStack(Material.GOLDEN_BOOTS));
            player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
            player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
        }
    }
}
