package com.infumia.t3sl4.util.inventory.element;

import com.infumia.t3sl4.util.inventory.Element;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class TSafeElement implements Element {
   @NotNull
   private final Element baseElement;

   public TSafeElement(@NotNull Element baseElement) {
      this.baseElement = baseElement;
   }

   public void displayOn(@NotNull Inventory inventory, int locX, int locY) {
      synchronized(this.baseElement) {
         this.baseElement.displayOn(inventory, locX, locY);
      }
   }

   public void accept(@NotNull InventoryInteractEvent event) {
      this.baseElement.accept(event);
   }

   public boolean is(@NotNull ItemStack icon) {
      return this.baseElement.is(icon);
   }

   public boolean is(@NotNull Element element) {
      return this.baseElement instanceof TSafeElement ? this.baseElement.is(((TSafeElement)element).baseElement) : this.baseElement.is(element);
   }
}
