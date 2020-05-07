package com.infumia.t3sl4.util.inventory.requirement;

import com.infumia.t3sl4.util.inventory.Element;
import com.infumia.t3sl4.util.inventory.Requirement;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.jetbrains.annotations.NotNull;

public final class ClickedElementReq implements Requirement {
   @NotNull
   private final Element element;

   public ClickedElementReq(@NotNull Element element) {
      this.element = element;
   }

   public boolean control(@NotNull InventoryInteractEvent event) {
      if (!(event instanceof InventoryClickEvent)) {
         return false;
      } else {
         InventoryClickEvent inventoryClickEvent = (InventoryClickEvent)event;
         return inventoryClickEvent.getCurrentItem() != null && this.element.is(inventoryClickEvent.getCurrentItem());
      }
   }
}
