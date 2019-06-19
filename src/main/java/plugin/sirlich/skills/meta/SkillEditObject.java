package main.java.plugin.sirlich.skills.meta;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.utilities.c;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.HashMap;

public class SkillEditObject
{
    private ClassType classType;
    private RpgPlayer parent;

    private HashMap<SkillKind, SkillType> skillMap = new HashMap<SkillKind, SkillType>();
    private HashMap<SkillKind, Integer> levelMap = new HashMap<SkillKind, Integer>();

    public SkillEditObject(ClassType classType, RpgPlayer parent){
        this.classType = classType;
        this.parent = parent;
    }

    public boolean buttonPush(SkillKind skillKind, SkillType skillType, ClickType clickType){
        int maxLevel = skillType.getSkill().getMaxLevel();

        //Undefined skills
        if(skillKind == SkillKind.UNDEFINED){
            parent.chat("That feature isn't implemented yet. Sorry. ");
        }

        //Defined skills
        else {
            //Left clicks
            if(clickType.equals(ClickType.LEFT)){

                //The skill kind we are editing has already been edited
                if(skillMap.containsKey(skillKind)){

                    //Skill type is the same, so we can update the level
                    if(skillMap.get(skillKind) == skillType){

                        //Skill type is less than max level
                        if(levelMap.get(skillKind) < maxLevel){
                            levelMap.put(skillKind,levelMap.get(skillKind) + 1);
                        }

                        //This skill is already the highest possible
                        else {
                            parent.chat("That skill is already maxed.");
                            return false;
                        }
                    }

                    //Skill type is different
                    else {
                        parent.chat("You can't select more than one skill from each category.");
                        return false;
                    }
                }

                //The skill kind we are editing is fresh!
                else {
                    skillMap.put(skillKind,skillType);
                    levelMap.put(skillKind,1);
                }
            }

            //Right clicks
            else if (clickType.equals(ClickType.RIGHT)){
                //The skill kind we are editing has already been edited
                if(skillMap.containsKey(skillKind)){

                    //Skill type is the same, so we can update the level
                    if(skillMap.get(skillKind) == skillType){

                        //Skill type is less than max level
                        if(levelMap.get(skillKind) > 1){
                            levelMap.put(skillKind,levelMap.get(skillKind) - 1);
                        }

                        //This skill just got down to 0
                        else {
                            skillMap.remove(skillKind);
                            levelMap.remove(skillKind);
                        }
                    }

                    //Skill type is different
                    else {
                        parent.chat("You can't select more than one skill from each category.");
                        return false;
                    }
                }

                //The skill kind we are editing is fresh!
                else {
                    parent.chat("That skill is already at the min level.");
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public void addSkills(){
        this.parent.chat("Your skills have been added:");
        System.out.println(classType.toString());
        if(this.classType == ClassType.WARLOCK){
            parent.addSkill(SkillType.ManaCharger,1);
        }
        for(SkillKind skillKind : skillMap.keySet()){
            SkillType skill = skillMap.get(skillKind);
            int level = levelMap.get(skillKind) - 1;

            //Announce
            this.parent.chat(c.dgray + " - " + c.green + skill.getSkill().getName() + " " + c.gray + level);

            //Add skills
            parent.addSkill(skill,level);
        }
    }

    public ClassType getClassType()
    {
        return classType;
    }
    public void setClassType(ClassType classType)
    {
        this.classType = classType;
    }


    public int getLevel(SkillKind skillKind){
        if(levelMap.containsKey(skillKind)){
            return levelMap.get(skillKind);
        } else {
            return 0;
        }
    }
    public void clearSkills(){
        this.skillMap.clear();
        this.levelMap.clear();
    }

    public void giveLoadout(){
        giveLoadout(parent,classType);
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
            player.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET));
            player.getInventory().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
            player.getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
            player.getInventory().setBoots(new ItemStack(Material.GOLD_BOOTS));
            player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
            player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
        }
        ItemStack stack = new ItemStack(Material.MONSTER_EGG, 64);

        SpawnEggMeta meta = (SpawnEggMeta) stack.getItemMeta();
        meta.setSpawnedType(EntityType.POLAR_BEAR);
        stack.setItemMeta(meta);
        player.getInventory().addItem(stack);
    }
}
