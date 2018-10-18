package main.java.plugin.sirlich.skills.active;

import main.java.plugin.sirlich.skills.meta.ActiveSkill;
import main.java.plugin.sirlich.core.RpgPlayer;

public class ScorchedEarth extends ActiveSkill
{
    public ScorchedEarth(RpgPlayer rpgPlayer, int level){
        super(rpgPlayer, level,-1);
        setName("Scorched Earth");
        setId("ScorchedEarth");
        clearDescription();
        addLoreLine("Strike the earth!");
    }

//    @Override
//    public void handleAction(){
//        getRpgPlayer().chat("You did something!");
//        Player player = getRpgPlayer().getPlayer();
//        int x = (int) player.getLocation().getX();
//        int y = (int) player.getLocation().getY();
//        int z = (int) player.getLocation().getZ();
//        int radius = 5;
//        for(int i = x - radius; i < x + radius; i ++){
//            for(int j = z -radius; j < z + radius; j ++){
//                if(Math.sqrt(Math.abs(i)^2 - Math.abs(i)^2) < radius){
//                    Block block = player.getWorld().getBlockAt(i,y,j);
//                    if(true){
//                        block.setType(Material.FIRE);
//                        player.getWorld().getBlockAt(i,y,j).getRelative(BlockFace.UP).setType(Material.FIRE);
//                    }
//                }
//            }
//        }
//    }

}
