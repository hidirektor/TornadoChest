package com.infumia.t3sl4.util.inventory.requirement;

import com.infumia.t3sl4.util.inventory.Requirement;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class NoClickableDownReq implements Requirement {
   public boolean control(@NotNull InventoryInteractEvent event) {
      return event instanceof InventoryClickEvent && !(((InventoryClickEvent)event).getClickedInventory() instanceof PlayerInventory);
   }
}
