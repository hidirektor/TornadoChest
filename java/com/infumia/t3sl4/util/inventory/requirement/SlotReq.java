package com.infumia.t3sl4.util.inventory.requirement;

import com.infumia.t3sl4.util.inventory.Requirement;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.jetbrains.annotations.NotNull;

public final class SlotReq implements Requirement {
   private final int slot;

   public SlotReq(int slot) {
      this.slot = slot;
   }

   public boolean control(@NotNull InventoryInteractEvent event) {
      return event instanceof InventoryClickEvent && ((InventoryClickEvent)event).getSlot() == this.slot || event instanceof InventoryDragEvent && ((InventoryDragEvent)event).getInventorySlots().contains(this.slot);
   }
}
