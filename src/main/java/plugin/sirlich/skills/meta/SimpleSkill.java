package plugin.sirlich.skills.meta;

public class SimpleSkill {

    /*
    This simple class allows skills to be used to without instantiating them.
     */
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
