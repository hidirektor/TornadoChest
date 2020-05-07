package com.infumia.t3sl4.util.inventory.requirement;

import com.infumia.t3sl4.util.inventory.Requirement;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.jetbrains.annotations.NotNull;

public final class DragTypeReq implements Requirement {
   @NotNull
   private final DragType dragType;

   public DragTypeReq(@NotNull DragType dragType) {
      this.dragType = dragType;
   }

   public boolean control(@NotNull InventoryInteractEvent event) {
      return event instanceof InventoryDragEvent && ((InventoryDragEvent)event).getType() == this.dragType;
   }
}
