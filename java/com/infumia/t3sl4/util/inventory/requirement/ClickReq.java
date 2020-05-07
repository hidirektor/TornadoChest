package com.infumia.t3sl4.util.inventory.requirement;

import com.infumia.t3sl4.util.inventory.Requirement;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.jetbrains.annotations.NotNull;

public final class ClickReq implements Requirement {
   public boolean control(@NotNull InventoryInteractEvent event) {
      return event instanceof InventoryClickEvent;
   }
}
