package com.infumia.t3sl4.util.inventory.page;

import com.infumia.t3sl4.util.inventory.Page;
import com.infumia.t3sl4.util.inventory.Pane;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.cactoos.Proc;
import org.jetbrains.annotations.NotNull;

public final class CloseInformerPage implements Page {
   @NotNull
   private final Page basePage;
   @NotNull
   private final Proc<Player> proc;

   public CloseInformerPage(@NotNull Page basePage, @NotNull Proc<Player> proc) {
      this.basePage = basePage;
      this.proc = proc;
      this.defineHolder(this);
   }

   public void add(@NotNull Pane pane, int position) {
      this.basePage.add(pane, position);
   }

   public void remove(int position) {
      this.basePage.remove(position);
   }

   public void rearrange(int paneIndex, int position) {
      this.basePage.rearrange(paneIndex, position);
   }

   public void defineHolder(@NotNull Page holder) {
      this.basePage.defineHolder(holder);
   }

   public void showTo(@NotNull Player player) {
      this.basePage.showTo(player);
   }

   public void handleClose(@NotNull InventoryCloseEvent event) {
      this.basePage.handleClose(event);

      try {
         this.proc.exec((Player)event.getPlayer());
      } catch (Exception var3) {
      }

   }

   public void update(@NotNull Object argument) {
      this.basePage.update(argument);
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public Inventory getInventory() {
      return this.basePage.getInventory();
   }

   public void accept(@NotNull InventoryInteractEvent event) {
      this.basePage.accept(event);
   }
}
