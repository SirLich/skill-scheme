package main.java.plugin.sirlich.skills.meta;

import main.java.plugin.sirlich.core.RpgPlayer;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;

public class SkillEditObject
{
    private ClassType classType;
    private RpgPlayer parent;

    public HashMap<SkillKind, SkillType> skillMap = new HashMap<SkillKind, SkillType>();
    public HashMap<SkillKind, Integer> levelMap = new HashMap<SkillKind, Integer>();

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
            else {
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
                    parent.chat("You can't select more than one skill from each category.");
                    return false;
                }
            }
        }
        return true;
    }

    public void addSkills(){
        for(SkillKind skillKind : skillMap.keySet()){
            parent.addSkill(skillMap.get(skillKind), levelMap.get(skillKind));
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
}
