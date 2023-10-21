package dev.sirlich.skillscheme.skills.meta;

public enum SkillKind
{
    SWORD(1),
    AXE(10),
    BOW(19),
    ACTIVE(28),
    PASSIVE_A(37),
    PASSIVE_B(46),
    UNDEFINED(0);

    private final int startingPosition;

    SkillKind(int startingPosition){
        this.startingPosition = startingPosition;
    }

    public int getStartingPosition(){
        return startingPosition;
    }
}
