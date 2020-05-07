package com.infumia.t3sl4.util.inventory.pane;

import com.infumia.t3sl4.util.inventory.Element;
import com.infumia.t3sl4.util.inventory.Pane;
import com.infumia.t3sl4.util.observer.Target;
import java.util.Arrays;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public final class LivePane implements Pane {
   @NotNull
   private final Plugin plugin;
   private final int period;
   @NotNull
   private final Pane[] frames;

   public LivePane(@NotNull Plugin plugin, int period, @NotNull Pane... frames) {
      this.plugin = plugin;
      this.period = period;
      this.frames = (Pane[])frames.clone();
   }

   public void fill(@NotNull Element element) {
      Pane[] var2 = this.frames;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Pane frame = var2[var4];
         frame.fill(element);
      }

   }

   public void fill(int frame, @NotNull Element element) {
      this.frames[frame].fill(element);
   }

   public void fill(@NotNull Element... elements) {
      Pane[] var2 = this.frames;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Pane frame = var2[var4];
         frame.fill(elements);
      }

   }

   public void fill(int frame, @NotNull Element... elements) {
      this.frames[frame].fill(elements);
   }

   public void clear() {
      Pane[] var1 = this.frames;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Pane frame = var1[var3];
         frame.clear();
      }

   }

   public boolean add(@NotNull Element element) {
      Pane[] var2 = this.frames;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Pane frame = var2[var4];
         if (frame.add(element)) {
            return true;
         }
      }

      return false;
   }

   @NotNull
   public Element[] add(@NotNull Element... elements) {
      Element[] leftOvers = elements;
      Pane[] var3 = this.frames;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Pane frame = var3[var5];
         leftOvers = frame.add(leftOvers);
         if (leftOvers.length == 0) {
            break;
         }
      }

      return leftOvers;
   }

   /** @deprecated */
   @Deprecated
   public void insert(@NotNull Element element, int locX, int locY, boolean shift) {
   }

   public void insert(int frame, @NotNull Element element, int locX, int locY, boolean shift) throws IllegalArgumentException {
      this.frames[frame].insert(element, locX, locY, shift);
   }

   public void replaceAll(@NotNull Element... elements) {
      Pane[] var2 = this.frames;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Pane frame = var2[var4];
         frame.replaceAll(elements);
      }

   }

   public void replaceAll(int frame, Element... elements) {
      this.frames[frame].replaceAll(elements);
   }

   /** @deprecated */
   @Deprecated
   public void remove(int locX, int locY) throws IllegalArgumentException {
   }

   public void remove(int frame, int locX, int locY) throws IllegalArgumentException {
      this.frames[frame].remove(locX, locY);
   }

   public void subscribe(@NotNull Target<Object> target) {
      Arrays.stream(this.frames).forEach((frame) -> {
         frame.subscribe(target);
      });
   }

   public boolean contains(@NotNull ItemStack icon) {
      Pane[] var2 = this.frames;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Pane frame = var2[var4];
         if (frame.contains(icon)) {
            return true;
         }
      }

      return false;
   }

   public void accept(@NotNull InventoryInteractEvent event) {
      Pane[] var2 = this.frames;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Pane frame = var2[var4];
         frame.accept(event);
      }

   }

   public void displayOn(@NotNull final Inventory inventory) {
      this.frames[0].displayOn(inventory);
      (new BukkitRunnable() {
         private int iterator;

         public void run() {
            if (inventory.getViewers().isEmpty()) {
               this.cancel();
            } else {
               this.nextFrame().displayOn(inventory);
            }

         }

         private Pane nextFrame() {
            this.iterator = this.iterator + 1 < LivePane.this.frames.length ? this.iterator + 1 : 0;
            return LivePane.this.frames[this.iterator];
         }
      }).runTaskTimer(this.plugin, 1L, (long)this.period);
   }
}
