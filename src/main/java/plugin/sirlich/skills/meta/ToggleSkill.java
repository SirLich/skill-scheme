package plugin.sirlich.skills.meta;

import plugin.sirlich.core.RpgPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ToggleSkill extends Skill{
    /*
    Required config values:

    Optional config values:
     */

    private Material headBlock;
    private ItemStack headSave;
    private boolean active;

    public ToggleSkill(RpgPlayer rpgPlayer, int level, String id, Material headBlock){
        super(rpgPlayer,level,id);
        this.headBlock = headBlock;
    }

    public void activateSkill(){
        this.headSave = getRpgPlayer().getPlayer().getInventory().getHelmet();
        this.getRpgPlayer().getPlayer().getInventory().setHelmet(new ItemStack(headBlock));
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
        this.getRpgPlayer().getPlayer().getInventory().setHelmet(headSave);
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
