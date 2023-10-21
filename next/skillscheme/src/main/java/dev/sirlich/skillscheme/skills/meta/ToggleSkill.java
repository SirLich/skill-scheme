package dev.sirlich.skillscheme.skills.meta;

import dev.sirlich.skillscheme.core.RpgPlayer;
import org.bukkit.inventory.ItemStack;

/**
 * Special skill which can be "toggled" on.
 */
public class ToggleSkill extends Skill{
    /*
    Required config values:

    Optional config values:
     */

    private ItemStack headSave;
    private boolean active;

    public ToggleSkill(RpgPlayer rpgPlayer, int level, String id){
        super(rpgPlayer,level,id);
    }

    public void activateSkill(){
        this.active = true;
        getRpgPlayer().setModifierActive(true);
    }

    public void toggleSkill(){
        if(this.active){
            deactivateSkill();
        } else {
            activateSkill();
        }
    }

    public void deactivateSkill(){
        this.active = false;
        getRpgPlayer().setModifierActive(false);
    }

    public boolean isActive() {
        return active;
    }

    public void toggleStatus(){
        if(this.isActive()){
            deactivateSkill();
        } else {
            activateSkill();
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
