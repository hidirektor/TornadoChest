package com.infumia.t3sl4.util.inventory.requirement;

import com.infumia.t3sl4.util.inventory.Requirement;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class DraggedItemReq implements Requirement {
   @NotNull
   private final ItemStack item;

   public DraggedItemReq(@NotNull ItemStack item) {
      this.item = item;
   }

   public boolean control(@NotNull InventoryInteractEvent event) {
      return event instanceof InventoryDragEvent && ((InventoryDragEvent)event).getOldCursor().equals(this.item);
   }
}
