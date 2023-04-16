package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.misc.Recipe;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class Craft implements Listener {

    @EventHandler
    public static void onCraft(CraftItemEvent e){
        Player p = (Player) e.getView().getPlayer();
        if (!ResourcesCheck.isLoaded(p.getName())){
            if (e.getRecipe().getResult().equals(Recipe.recipes.get(0).getResult())){
                e.getRecipe().getResult().setType(Material.AIR);
            }
        }
    }

}
