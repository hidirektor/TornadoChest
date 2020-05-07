package com.infumia.t3sl4.util.inventory.event;

import com.infumia.t3sl4.util.inventory.ElementEvent;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ElementDragEvent implements ElementEvent {
   @NotNull
   private final InventoryDragEvent baseEvent;
   @NotNull
   private final ElementBasicEvent baseElementEvent;

   public ElementDragEvent(@NotNull InventoryDragEvent baseEvent) {
      this.baseEvent = baseEvent;
      this.baseElementEvent = new ElementBasicEvent(baseEvent);
   }

   @NotNull
   public ItemStack oldCursor() {
      return this.baseEvent.getOldCursor();
   }

   @NotNull
   public Map<Integer, ItemStack> items() {
      return this.baseEvent.getNewItems();
   }

   @NotNull
   public Player player() {
      return this.baseElementEvent.player();
   }

   public void cancel() {
      this.baseElementEvent.cancel();
   }

   public void closeView() {
      this.baseElementEvent.closeView();
   }
}
