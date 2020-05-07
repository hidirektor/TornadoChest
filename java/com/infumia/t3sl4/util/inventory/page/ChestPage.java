package com.infumia.t3sl4.util.inventory.page;

import com.infumia.t3sl4.util.inventory.Page;
import com.infumia.t3sl4.util.inventory.Pane;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public final class ChestPage implements Page {
   @NotNull
   private final String title;
   private final int size;
   @NotNull
   private final List<Pane> panes;
   @NotNull
   private final List<Player> viewers;
   @NotNull
   private Page holder;

   public ChestPage(@NotNull String title, int size, @NotNull Pane... panes) {
      this.title = title;
      this.size = Math.max(size, 9);
      this.panes = new ArrayList(Arrays.asList(panes));
      this.viewers = new ArrayList();
      this.holder = this;
      Arrays.stream(panes).forEach((pane) -> {
         pane.subscribe(this);
      });
   }

   public void add(@NotNull Pane pane, int position) {
      this.panes.add(Math.min(position, this.panes.size()), pane);
      this.update(new Object());
   }

   public void remove(int position) {
      this.panes.remove(position);
      this.update(new Object());
   }

   public void rearrange(int paneIndex, int position) {
      Pane pane = (Pane)this.panes.get(paneIndex);
      this.panes.remove(paneIndex);
      this.panes.add(Math.min(position, this.panes.size()), pane);
      this.update(new Object());
   }

   public void defineHolder(@NotNull Page holder) {
      this.holder = holder;
   }

   public void showTo(@NotNull Player player) {
      Inventory inventory = Bukkit.createInventory(this.holder, this.size, this.title);
      Iterator var3 = this.panes.iterator();

      while(var3.hasNext()) {
         Pane pane = (Pane)var3.next();
         pane.displayOn(inventory);
      }

      player.openInventory(inventory);
      if (!this.viewers.contains(player)) {
         this.viewers.add(player);
      }

   }

   public void handleClose(@NotNull InventoryCloseEvent event) {
      this.viewers.remove((Player)event.getPlayer());
   }

   public void update(@NotNull Object argument) {
      Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugins()[0], () -> {
         this.viewers.forEach((viewer) -> {
            Inventory inventory = viewer.getOpenInventory().getTopInventory();
            inventory.clear();
            this.panes.forEach((pane) -> {
               pane.displayOn(inventory);
            });
         });
      });
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public Inventory getInventory() {
      return Bukkit.createInventory((InventoryHolder)null, 9);
   }

   public void accept(@NotNull InventoryInteractEvent event) {
      (new ArrayList<>(this.panes)).forEach((pane) -> {
         pane.accept(event);
      });
   }
}
