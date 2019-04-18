package main.java.plugin.sirlich.core;

public enum PlayerState
{
    /*
    This is used for testing the plugin:
    - Skills are automatically applied
     */
    TESTING(true,true),

    /*
    This is for players who are not playing skill-scheme related games at all:
    - No ability to use skills
    - No ability to interact with enchantment tables
     */
    HUB(false,false),

    /*
    This is for players who are currently inside a skill-scheme game:
    - Skills can be used, if useSkill is turned on
    - Skills should be auto-applied when entering this game-mode
     */
    GAME(false,false),

    /*
    This is for players who are waiting in a skillScheme lobby:
    - Can use the skills editor
    - Skills are "saved", and can be applied when the game starts
     */
    LOBBY(true,false),

    /*
    This is a special mode for players who died during a skillScheme game.
    - No access to skills
    - Placed into spectator mode on enter.
     */
    SPECTATOR(false,false);

    private final boolean canUseEditor;
    private final boolean canBreakBlocks;
    PlayerState(boolean canUseEditor, boolean canBreakBlocks){
        this.canUseEditor = canUseEditor;
        this.canBreakBlocks = canBreakBlocks;
    }

    public boolean canUseEditor(){
        return this.canUseEditor;
    }

    public static boolean canUseEditor(PlayerState playerState){
        return playerState.canUseEditor;
    }

    public boolean canBreakBlocks(){
        return this.canBreakBlocks;
    }

    public static boolean canBreakBlocks(PlayerState playerState){
        return playerState.canBreakBlocks;
    }
}
