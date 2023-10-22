package dev.sirlich.skillscheme.skills.meta;

/**
This simple class allows skills to be used to without instantiating them.
*/
public class SimpleSkill {
    private SkillType skillType;
    private Integer level;

    public SimpleSkill(SkillType skillType, Integer level){
        this.skillType = skillType;
        this.level = level;
    }

    public SkillType getSkillType() {
        return skillType;
    }

    public void setSkillType(SkillType skillType) {
        this.skillType = skillType;
    }

    public Integer getLevel() {
        return level;
    }

    public void incrementLevel() {
        level ++;
    }

    public void decrementLevel(){
        level --;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
