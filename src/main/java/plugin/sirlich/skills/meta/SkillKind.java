package plugin.sirlich.skills.meta;

public enum SkillKind
{
    SWORD(1),
    AXE(10),
    BOW(19),
    PASSIVE_A(28),
    PASSIVE_B(37),
    PASSIVE_C(46),
    UNDEFINED(0);

    private final int startingPosition;

    SkillKind(int startingPosition){
        this.startingPosition = startingPosition;
    }

    public int getStartingPosition(){
        return startingPosition;
    }
}
