package com.infumia.t3sl4.util.inventory.pane;

import com.infumia.t3sl4.util.inventory.Element;
import com.infumia.t3sl4.util.inventory.Pane;
import com.infumia.t3sl4.util.observer.Target;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class TSafePane implements Pane {
   @NotNull
   private final Pane basePane;

   public TSafePane(@NotNull Pane basePane) {
      this.basePane = basePane;
   }

   public void fill(@NotNull Element element) {
      synchronized(this.basePane) {
         this.basePane.fill(element);
      }
   }

   public void fill(@NotNull Element... elements) {
      synchronized(this.basePane) {
         this.basePane.fill(elements);
      }
   }

   public void clear() {
      synchronized(this.basePane) {
         this.basePane.clear();
      }
   }

   public boolean add(@NotNull Element element) {
      synchronized(this.basePane) {
         return this.basePane.add(element);
      }
   }

   @NotNull
   public Element[] add(@NotNull Element... elements) {
      synchronized(this.basePane) {
         return this.basePane.add(elements);
      }
   }

   public void insert(@NotNull Element element, int locX, int locY, boolean shift) throws IllegalArgumentException {
      synchronized(this.basePane) {
         this.basePane.insert(element, locX, locY, shift);
      }
   }

   public void replaceAll(@NotNull Element... elements) {
      synchronized(this.basePane) {
         this.basePane.replaceAll(elements);
      }
   }

   public void remove(int locX, int locY) {
      synchronized(this.basePane) {
         this.basePane.remove(locX, locY);
      }
   }

   public void subscribe(@NotNull Target<Object> target) {
      synchronized(this.basePane) {
         this.basePane.subscribe(target);
      }
   }

   public boolean contains(@NotNull ItemStack icon) {
      return this.basePane.contains(icon);
   }

   public void accept(@NotNull InventoryInteractEvent event) {
      this.basePane.accept(event);
   }

   public void displayOn(@NotNull Inventory inventory) {
      this.basePane.displayOn(inventory);
   }
}
