package com.infumia.t3sl4.util.inventory.element;

import com.infumia.t3sl4.util.inventory.Element;
import com.infumia.t3sl4.util.reflection.LoggerOf;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.cactoos.iterable.IterableOf;
import org.cactoos.scalar.And;
import org.cactoos.scalar.FirstOf;
import org.cactoos.scalar.Or;
import org.jetbrains.annotations.NotNull;

public final class LiveElement implements Element {
   @NotNull
   private static final Logger LOGGER = new LoggerOf(new Class[]{LiveElement.class});
   @NotNull
   private final Plugin plugin;
   private final int period;
   @NotNull
   private final Element[] frames;

   public LiveElement(@NotNull Plugin plugin, int period, @NotNull Element... frames) {
      this.plugin = plugin;
      this.period = period;
      this.frames = (Element[])frames.clone();
   }

   private Element nullElement() {
      return new BasicElement(new ItemStack(Material.PAPER), "nullElement");
   }

   private Element findFrame(@NotNull ItemStack icon) {
      try {
         return (Element)(new FirstOf<>((element) -> {
            return element.is(icon);
         }, new IterableOf(this.frames), this::nullElement)).value();
      } catch (Exception var3) {
         return this.nullElement();
      }
   }

   private boolean contains(@NotNull Element element) {
      try {
         return (new Or((frame) -> {
            return frame.is(element);
         }, this.frames)).value();
      } catch (Exception var3) {
         return false;
      }
   }

   public void accept(@NotNull InventoryInteractEvent event) {
      try {
         (new And((frame) -> {
            frame.accept(event);
         }, this.frames)).value();
      } catch (Exception var3) {
         LOGGER.warning("accept(InventoryInteractEvent) -> Somethings went wrong\n" + var3.getMessage());
      }

   }

   public void displayOn(@NotNull final Inventory inventory, final int locX, final int locY) {
      this.frames[0].displayOn(inventory, locX, locY);
      (new BukkitRunnable() {
         private int iterator;

         public void run() {
            if (inventory.getViewers().isEmpty()) {
               this.cancel();
            } else {
               this.nextFrame().displayOn(inventory, locX, locY);
            }

         }

         private Element nextFrame() {
            this.iterator = this.iterator + 1 < LiveElement.this.frames.length ? this.iterator + 1 : 0;
            return LiveElement.this.frames[this.iterator];
         }
      }).runTaskTimer(this.plugin, 1L, (long)this.period);
   }

   public boolean is(@NotNull ItemStack icon) {
      return this.findFrame(icon).is(icon);
   }

   public boolean is(@NotNull Element element) {
      return this.contains(element);
   }
}
