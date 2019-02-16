package main.java.plugin.sirlich.core;

public enum PlayerState
{
    /*
    This is used for testing the plugin:
    - Skills are automatically applied
     */
    TESTING,

    /*
    This is for players who are not playing skill-scheme related games at all:
    - No ability to use skills
    - No ability to interact with enchantment tables
     */
    HUB,

    /*
    This is for players who are currently inside a skill-scheme game:
    - Skills can be used, if useSkill is turned on
    - Skills should be auto-applied when entering this game-mode
     */
    GAME,

    /*
    This is for players who are waiting in a skillScheme lobby:
    - Can use the skills editor
    - Skills are "saved", and can be applied when the game starts
     */
    LOBBY,

    /*
    This is a special mode for players who died during a skillScheme game.
    - No access to skills
    - Placed into spectator mode on enter. 
     */
    SPECTATOR
}
