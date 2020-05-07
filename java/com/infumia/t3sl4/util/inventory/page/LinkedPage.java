package com.infumia.t3sl4.util.inventory.page;

import com.infumia.t3sl4.util.inventory.Element;
import com.infumia.t3sl4.util.inventory.Page;
import com.infumia.t3sl4.util.inventory.Pane;
import com.infumia.t3sl4.util.inventory.Requirement;
import com.infumia.t3sl4.util.inventory.Target;
import com.infumia.t3sl4.util.inventory.element.BasicElement;
import com.infumia.t3sl4.util.inventory.event.ElementBasicEvent;
import com.infumia.t3sl4.util.inventory.event.ElementDragEvent;
import com.infumia.t3sl4.util.inventory.target.BasicTarget;
import com.infumia.t3sl4.util.inventory.target.ClickTarget;
import com.infumia.t3sl4.util.inventory.target.DragTarget;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class LinkedPage implements Page {
   @NotNull
   private final Map<Integer, Page> pages;
   @NotNull
   private final Element nextElement;
   @NotNull
   private final Element previousElement;
   private int currentPage;

   public LinkedPage(@NotNull Map<Integer, Page> pages, @NotNull Element nextElement, @NotNull Element previousElement, int currentPage) {
      this.pages = pages;
      this.nextElement = nextElement;
      this.previousElement = previousElement;
      this.currentPage = currentPage;
   }

   public LinkedPage(@NotNull Map<Integer, Page> pages, @NotNull ItemStack nextItemStack, @NotNull ItemStack previousItemStack, int currentPage) {
      this.pages = pages;
      this.currentPage = currentPage;
      this.nextElement = new BasicElement(nextItemStack, "next-element", new Target[]{new BasicTarget(ElementBasicEvent::cancel, new Requirement[0]), new DragTarget(ElementDragEvent::cancel, new Requirement[0]), new ClickTarget((event) -> {
         event.cancel();
         if (this.currentPage < pages.size()) {
            this.runTask(() -> {
               --this.currentPage;
               ((Page)pages.get(currentPage)).showTo(event.player());
            });
         }
      }, new Requirement[0])});
      this.previousElement = new BasicElement(previousItemStack, "previous-element", new Target[]{new BasicTarget(ElementBasicEvent::cancel, new Requirement[0]), new DragTarget(ElementDragEvent::cancel, new Requirement[0]), new ClickTarget((event) -> {
         event.cancel();
         if (this.currentPage >= 0) {
            this.runTask(() -> {
               --this.currentPage;
               ((Page)pages.get(currentPage)).showTo(event.player());
            });
         }
      }, new Requirement[0])});
   }

   public LinkedPage(@NotNull Map<Integer, Page> pages, @NotNull Element nextElement, @NotNull Element previousElement) {
      this(pages, (Element)nextElement, (Element)previousElement, 0);
   }

   public LinkedPage(@NotNull Map<Integer, Page> pages, @NotNull ItemStack nextItemStack, @NotNull ItemStack previousItemStack) {
      this(pages, (ItemStack)nextItemStack, (ItemStack)previousItemStack, 0);
   }

   public void add(@NotNull Pane pane, int position) {
      ((Page)this.pages.get(this.currentPage)).add(pane, position);
   }

   public void remove(int position) {
      ((Page)this.pages.get(this.currentPage)).remove(position);
   }

   public void rearrange(int paneIndex, int position) {
      ((Page)this.pages.get(this.currentPage)).rearrange(paneIndex, position);
   }

   public void defineHolder(@NotNull Page holder) {
      ((Page)this.pages.get(this.currentPage)).defineHolder(holder);
   }

   public void showTo(@NotNull Player player) {
      ((Page)this.pages.get(this.currentPage)).showTo(player);
   }

   public void handleClose(@NotNull InventoryCloseEvent event) {
      ((Page)this.pages.get(this.currentPage)).handleClose(event);
   }

   public void accept(@NotNull InventoryInteractEvent event) {
      ((Page)this.pages.get(this.currentPage)).accept(event);
   }

   public void update(@NotNull Object argument) {
      ((Page)this.pages.get(this.currentPage)).update(argument);
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public Inventory getInventory() {
      return ((Page)this.pages.get(this.currentPage)).getInventory();
   }

   private void runTask(@NotNull Runnable runnable) {
      Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugins()[0], runnable);
   }
}
