package com.infumia.t3sl4.util.inventory.requirement;

import com.infumia.t3sl4.util.inventory.Element;
import com.infumia.t3sl4.util.inventory.Requirement;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.jetbrains.annotations.NotNull;

public final class DraggedElementReq implements Requirement {
   @NotNull
   private final Element element;

   public DraggedElementReq(@NotNull Element element) {
      this.element = element;
   }

   public boolean control(@NotNull InventoryInteractEvent event) {
      if (!(event instanceof InventoryDragEvent)) {
         return false;
      } else {
         InventoryDragEvent inventoryDragEvent = (InventoryDragEvent)event;
         return inventoryDragEvent.getCursor() != null && this.element.is(inventoryDragEvent.getCursor());
      }
   }
}
