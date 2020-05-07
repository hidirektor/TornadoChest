package com.infumia.t3sl4.util.inventory.page;

import com.infumia.t3sl4.util.inventory.Page;
import com.infumia.t3sl4.util.inventory.Pane;
import com.infumia.t3sl4.util.inventory.Requirement;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.cactoos.list.ListOf;
import org.cactoos.scalar.And;
import org.jetbrains.annotations.NotNull;

public class ControllableDownPage implements Page {
   @NotNull
   private final Page basePage;
   @NotNull
   private final List<Requirement> extraRequirements;

   public ControllableDownPage(@NotNull Page basePage, @NotNull Requirement... extraRequirements) {
      this.basePage = basePage;
      this.extraRequirements = new ListOf(extraRequirements);
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
   }

   public void accept(@NotNull InventoryInteractEvent event) {
      try {
         if (!(new And((requirement) -> {
            return requirement.control(event);
         }, this.extraRequirements)).value()) {
            event.setCancelled(true);
            return;
         }

         this.basePage.accept(event);
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
}
