package com.infumia.t3sl4.util.inventory.event;

import com.infumia.t3sl4.util.inventory.ElementEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.jetbrains.annotations.NotNull;

public final class ElementBasicEvent implements ElementEvent {
   @NotNull
   private final InventoryInteractEvent baseEvent;

   public ElementBasicEvent(@NotNull InventoryInteractEvent baseEvent) {
      this.baseEvent = baseEvent;
   }

   @NotNull
   public Player player() {
      return (Player)this.baseEvent.getWhoClicked();
   }

   public void cancel() {
      this.baseEvent.setCancelled(true);
   }

   public void closeView() {
      Bukkit.getScheduler().runTask(this.baseEvent.getHandlers().getRegisteredListeners()[0].getPlugin(), () -> {
         this.baseEvent.getWhoClicked().closeInventory();
      });
   }
}
