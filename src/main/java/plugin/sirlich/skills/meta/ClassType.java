package plugin.sirlich.skills.meta;

public enum ClassType
{
    PALADIN(false),
    FIGHTER(false),
    RANGER(true),
    ROGUE(true),
    WARLOCK(false),
    UNDEFINED(false);

    private final boolean canUseBow;

    ClassType(boolean canUseBow) {
        this.canUseBow = canUseBow;
    }

    public boolean canUseBow(){
        return canUseBow;
    }

}
