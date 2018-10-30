package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.core.RpgPlayer;
import main.java.plugin.sirlich.skills.meta.ActiveSkill;

public class PrimedSkill extends ActiveSkill
{
    private boolean primed = false;

    public PrimedSkill(RpgPlayer rpgPlayer, int level,String id){
        super(rpgPlayer,level,id);
    }


}
