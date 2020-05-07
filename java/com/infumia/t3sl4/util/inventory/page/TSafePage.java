package com.infumia.t3sl4.util.inventory.page;

import com.infumia.t3sl4.util.inventory.Page;
import com.infumia.t3sl4.util.inventory.Pane;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public final class TSafePage implements Page {
   @NotNull
   private final Page basePage;

   public TSafePage(@NotNull Page basePage) {
      this.basePage = basePage;
   }

   public void add(@NotNull Pane pane, int position) {
      synchronized(this.basePage) {
         this.basePage.add(pane, position);
      }
   }

   public void remove(int position) {
      synchronized(this.basePage) {
         this.basePage.remove(position);
      }
   }

   public void rearrange(int paneIndex, int position) {
      synchronized(this.basePage) {
         this.basePage.rearrange(paneIndex, position);
      }
   }

   public void defineHolder(@NotNull Page holder) {
      this.basePage.defineHolder(holder);
   }

   public void showTo(@NotNull Player player) {
      synchronized(this.basePage) {
         this.basePage.showTo(player);
      }
   }

   public void handleClose(@NotNull InventoryCloseEvent event) {
      synchronized(this.basePage) {
         this.basePage.handleClose(event);
      }
   }

   public void update(@NotNull Object argument) {
      synchronized(this.basePage) {
         this.basePage.update(argument);
      }
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public Inventory getInventory() {
      return this.basePage.getInventory();
   }

   public void accept(@NotNull InventoryInteractEvent event) {
      synchronized(this.basePage) {
         this.basePage.accept(event);
      }
   }
}
