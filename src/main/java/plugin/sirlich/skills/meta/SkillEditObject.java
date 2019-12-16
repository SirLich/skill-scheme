package plugin.sirlich.skills.meta;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.utilities.c;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class SkillEditObject
{
    private ClassType classType;
    private RpgPlayer parent;
    private Integer points;

    private HashMap<SkillKind, SkillType> skillMap = new HashMap<SkillKind, SkillType>();
    private HashMap<SkillKind, Integer> levelMap = new HashMap<SkillKind, Integer>();

    public SkillEditObject(ClassType classType, RpgPlayer parent){
        this.classType = classType;
        this.parent = parent;
    }

    public boolean buttonPush(SkillKind skillKind, SkillType skillType, ClickType clickType){
        int maxLevel = skillType.getSkill().getMaxLevel();
        int cost = skillType.getSkill().getCost();

        //Remove soon
        if(false){

        }
        //Defined skills
        else {
            //Left clicks
            if(clickType.equals(ClickType.LEFT)){

                //The player has points remaining
                if(points - cost >= 0 || skillKind == SkillKind.UNDEFINED) {

                    //The skill kind we are editing has already been edited
                    if (skillMap.containsKey(skillKind)) {

                        //Skill type is the same, so we can update the level
                        if (skillMap.get(skillKind) == skillType || skillKind == SkillKind.UNDEFINED) {

                            //Skill type is less than max level
                            if (levelMap.get(skillKind) < maxLevel) {
                                points -= cost;
                                levelMap.put(skillKind, levelMap.get(skillKind) + 1);
                            }

                            //This skill is already the highest possible
                            else {
                                parent.tell("That skill is already maxed.");
                                return false;
                            }
                        }

                        //Skill type is different
                        else {
                            parent.tell("You can't select more than one skill from each category.");
                            return false;
                        }
                    }

                    //The skill kind we are editing is fresh!
                    else {
                        skillMap.put(skillKind, skillType);
                        levelMap.put(skillKind, 1);
                        points -= cost;
                    }
                }

                //No points remaining
                else{
                    parent.tell("You don't have any points remaining");
                    return false;
                }
            }

            //Right clicks
            else if (clickType.equals(ClickType.RIGHT)){
                //The skill kind we are editing has already been edited
                if(skillMap.containsKey(skillKind)){

                    //Skill type is the same, so we can update the level
                    if(skillMap.get(skillKind) == skillType || skillKind == SkillKind.UNDEFINED){

                        //Skill type is less than max level
                        if(levelMap.get(skillKind) > 1){
                            levelMap.put(skillKind,levelMap.get(skillKind) - 1);
                        }

                        //This skill just got down to 0
                        else {
                            skillMap.remove(skillKind);
                            levelMap.remove(skillKind);
                        }
                        points += cost;
                    }

                    //Skill type is different
                    else {
                        parent.tell("You can't select more than one skill from each category.");
                        return false;
                    }
                }

                //The skill kind we are editing is fresh!
                else {
                    parent.tell("That skill is already at the min level.");
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public void addSkills(boolean announceLoadout){
        parent.refreshSessionToken();

        //Don't run if skill size is 0
        if(skillMap.size() == 0){
            return;
        }

        if(announceLoadout){
            this.parent.tell("Your loadout has been applied:");
        }

        for(SimpleSkill simpleSkill : SkillData.getDefaultSkills(classType)){
            parent.addSkill(simpleSkill.getSkillType(), simpleSkill.getLevel());
        }

        for(SkillKind skillKind : skillMap.keySet()){
            SkillType skill = skillMap.get(skillKind);
            int level = levelMap.get(skillKind) - 1;

            //Announce
            this.parent.tell(c.dgray + " - " + c.gray + skill.getSkill().getName() + ": " + c.green + (level + 1));

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
            player.getInventory().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
            player.getInventory().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
            player.getInventory().setLeggings(new ItemStack(Material.GOLDEN_LEGGINGS));
            player.getInventory().setBoots(new ItemStack(Material.GOLDEN_BOOTS));
            player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
            player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
        }
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
