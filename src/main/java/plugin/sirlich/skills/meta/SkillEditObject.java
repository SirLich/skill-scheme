package main.java.plugin.sirlich.skills.meta;

import java.util.HashMap;

public class SkillEditObject
{
    private ClassType classType;

    public static HashMap<SkillKind, SkillType> skillMap = new HashMap<SkillKind, SkillType>();
    public static HashMap<SkillKind, Integer> levelMap = new HashMap<SkillKind, Integer>();

    public HashMap<SkillKind, SkillType> getSkillMap(){
        return skillMap;
    }

    public HashMap<SkillKind, Integer> getLevelMap()
    {
        return levelMap;
    }


    public SkillEditObject(ClassType classType){
        this.classType = classType;
        for(SkillKind skillKind : SkillKind.values()){
            skillMap.put(skillKind,SkillType.Basic);
            levelMap.put(skillKind,0);
        }
    }

    public void setSkill(SkillKind skillKind, SkillType skillType){
        skillMap.put(skillKind,skillType);
    }

    public SkillType getSkill(SkillKind skillKind){
        return skillMap.get(skillKind);
    }

    public void setSkillLevel(SkillKind skillKind, int level){
        levelMap.put(skillKind,level);
    }

    public Integer getSkillLevel(SkillKind skillKind){
        return levelMap.get(skillKind);
    }
}
