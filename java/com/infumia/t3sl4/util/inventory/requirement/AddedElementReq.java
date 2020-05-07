package com.infumia.t3sl4.util.inventory.requirement;

import com.infumia.t3sl4.util.inventory.Element;
import com.infumia.t3sl4.util.inventory.Requirement;
import java.util.stream.Stream;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.jetbrains.annotations.NotNull;

public final class AddedElementReq implements Requirement {
   @NotNull
   private final Element element;

   public AddedElementReq(@NotNull Element element) {
      this.element = element;
   }

   public boolean control(@NotNull InventoryInteractEvent event) {
      return (event instanceof InventoryDragEvent && ((InventoryDragEvent)event)
              .getNewItems().values().stream().anyMatch(this.element::is));
   }
}
