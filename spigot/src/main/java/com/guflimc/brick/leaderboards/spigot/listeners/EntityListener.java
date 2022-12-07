package com.guflimc.brick.leaderboards.spigot.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EntityListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity().hasMetadata("LEADERBOARD")) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().hasMetadata("LEADERBOARD")) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().hasMetadata("LEADERBOARD")) {
            event.setCancelled(true);
        }
    }

    public void onArmorStand(PlayerArmorStandManipulateEvent event) {
        if (event.getRightClicked().hasMetadata("LEADERBOARD")) {
            event.setCancelled(true);
        }
    }

    public void onHanging(HangingBreakEvent event) {
        if (event.getEntity().hasMetadata("LEADERBOARD")) {
            event.setCancelled(true);
        }
    }
}
