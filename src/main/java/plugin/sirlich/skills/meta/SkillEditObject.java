package plugin.sirlich.skills.meta;

import plugin.sirlich.core.RpgPlayer;
import plugin.sirlich.utilities.WeaponUtils;
import plugin.sirlich.utilities.Color;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.HashMap;

public class SkillEditObject
{
    private ClassType classType;
    private RpgPlayer parent;
    private Integer points;

    private HashMap<SkillKind, SimpleSkill> simpleSkillMap = new HashMap<SkillKind, SimpleSkill>();

    public SkillEditObject(ClassType classType, RpgPlayer parent){
        this.classType = classType;
        this.parent = parent;
    }

    /*
    Disgusting method to handle inventory clicks. This could definitely be improved.
     */
    public boolean buttonPush(SkillKind skillKind, SkillType skillType, ClickType clickType){
        //Local data about the current skill
        int maxLevel = skillType.getSkill().getMaxLevel();
        int cost = skillType.getSkill().getCost();

        //Left clicks: Attempt to add a skill level
        if(clickType.equals(ClickType.LEFT)){

            //The player has points remaining
            if(points - cost >= 0 || skillKind == SkillKind.UNDEFINED) {

                //The skill kind we are editing has already been edited
                if (simpleSkillMap.containsKey(skillKind)) {

                    //Skill type is the same, so we can update the level
                    if (simpleSkillMap.get(skillKind).getSkillType() == skillType) {

                        //Skill type is less than max level
                        if (simpleSkillMap.get(skillKind).getLevel() < maxLevel) {
                            points -= cost;
                            simpleSkillMap.get(skillKind).incrementLevel();
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
                    simpleSkillMap.put(skillKind,new SimpleSkill(skillType, 1));
                    points -= cost;
                }
            }

            //No points remaining
            else{
                parent.tell("You don't have any points remaining");
                return false;
            }
        }

        //Right clicks: Attempt to remove a skill level
        else if (clickType.equals(ClickType.RIGHT)){
            //The skill kind we are editing has already been edited
            if(simpleSkillMap.containsKey(skillKind)){

                //Skill type is the same, so we can update the level
                if(simpleSkillMap.get(skillKind).getSkillType() == skillType){

                    //Skill type is less than max level
                    if(simpleSkillMap.get(skillKind).getLevel() > 1){
                        simpleSkillMap.get(skillKind).decrementLevel();
                    }

                    //This skill just got down to 0
                    else {
                        simpleSkillMap.remove(skillKind);
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
    return true;
    }

    public void addSkills(boolean announceLoadout){
        parent.refreshSessionToken();

        //Don't run if skill size is 0
        if(simpleSkillMap.size() == 0){
            return;
        }

        if(announceLoadout){
            this.parent.tell("Your loadout has been applied:");
        }

        ArrayList<SimpleSkill> loadout = new ArrayList<SimpleSkill>();

        //This adds the "default" skills for each class. For example ManaRegen
        for(SimpleSkill simpleSkill : SkillData.getDefaultSkills(classType)){
            loadout.add(simpleSkill);
        }

        //This adds the skills you were just editing.
        for(SimpleSkill simpleSkill : simpleSkillMap.values()){
            //Announce
            if(announceLoadout){
                this.parent.tell(Color.dgray + " - " + Color.gray + simpleSkill.getSkillType().getSkill().getName() + ": " + Color.green + (simpleSkill.getLevel()));
            }

            //Add skills
            loadout.add(simpleSkill);
        }

        //Apply
        parent.setLoadout(classType, loadout);
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
        if (simpleSkillMap.containsKey(skillKind)) {
            return simpleSkillMap.get(skillKind).getLevel();
        }
        else {
            return 0;
        }
    }

    public void clearSkills(){
        simpleSkillMap.clear();
    }

    public void giveLoadout(){
        WeaponUtils.giveLoadout(parent,classType);
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
