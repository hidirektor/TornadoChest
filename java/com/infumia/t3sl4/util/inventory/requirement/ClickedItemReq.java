package com.infumia.t3sl4.util.inventory.requirement;

import com.infumia.t3sl4.util.inventory.Requirement;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ClickedItemReq implements Requirement {
   @NotNull
   private final ItemStack item;

   public ClickedItemReq(@NotNull ItemStack item) {
      this.item = item;
   }

   public boolean control(@NotNull InventoryInteractEvent event) {
      if (!(event instanceof InventoryClickEvent)) {
         return false;
      } else {
         InventoryClickEvent inventoryClickEvent = (InventoryClickEvent)event;
         return inventoryClickEvent.getCurrentItem() != null && inventoryClickEvent.getCurrentItem().equals(this.item);
      }
   }
}
