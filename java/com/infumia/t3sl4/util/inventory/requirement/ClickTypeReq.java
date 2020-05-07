package com.infumia.t3sl4.util.inventory.requirement;

import com.infumia.t3sl4.util.inventory.Requirement;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.jetbrains.annotations.NotNull;

public final class ClickTypeReq implements Requirement {
   @NotNull
   private final ClickType clickType;

   public ClickTypeReq(@NotNull ClickType clickType) {
      this.clickType = clickType;
   }

   public boolean control(@NotNull InventoryInteractEvent event) {
      return event instanceof InventoryClickEvent && ((InventoryClickEvent)event).getClick() == this.clickType;
   }
}
