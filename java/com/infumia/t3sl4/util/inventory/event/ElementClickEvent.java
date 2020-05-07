package com.infumia.t3sl4.util.inventory.event;

import com.infumia.t3sl4.util.inventory.ElementEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ElementClickEvent implements ElementEvent {
   @NotNull
   private final InventoryClickEvent baseEvent;
   @NotNull
   private final ElementBasicEvent baseElementEvent;

   public ElementClickEvent(@NotNull InventoryClickEvent baseEvent) {
      this.baseEvent = baseEvent;
      this.baseElementEvent = new ElementBasicEvent(baseEvent);
   }

   @NotNull
   public ItemStack currentItem() {
      return this.baseEvent.getCurrentItem() == null ? new ItemStack(Material.AIR) : this.baseEvent.getCurrentItem().clone();
   }

   public int clickedX() {
      return this.baseEvent.getSlot() % 9;
   }

   public int clickedY() {
      return this.baseEvent.getSlot() / 9;
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
