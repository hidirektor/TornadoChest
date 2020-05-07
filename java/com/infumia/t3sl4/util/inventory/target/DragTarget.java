package com.infumia.t3sl4.util.inventory.target;

import com.infumia.t3sl4.util.inventory.Requirement;
import com.infumia.t3sl4.util.inventory.Target;
import com.infumia.t3sl4.util.inventory.event.ElementDragEvent;
import com.infumia.t3sl4.util.reflection.LoggerOf;
import java.util.logging.Logger;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.cactoos.Proc;
import org.cactoos.scalar.And;
import org.jetbrains.annotations.NotNull;

public final class DragTarget implements Target {
   @NotNull
   private static final Logger LOGGER = new LoggerOf(new Class[]{ClickTarget.class});
   @NotNull
   private final Proc<ElementDragEvent> handler;
   @NotNull
   private final Requirement[] reqs;

   public DragTarget(@NotNull Proc<ElementDragEvent> handler, @NotNull Requirement... reqs) {
      this.handler = handler;
      this.reqs = (Requirement[])reqs.clone();
   }

   public void handle(@NotNull InventoryInteractEvent event) {
      if (event instanceof InventoryDragEvent) {
         try {
            if ((new And((req) -> {
               return req.control(event);
            }, this.reqs)).value()) {
               this.handler.exec(new ElementDragEvent((InventoryDragEvent)event));
            }
         } catch (Exception var3) {
            LOGGER.warning("handle(InventoryInteractEvent) -> Somethings went wrong: " + var3.getMessage());
            var3.printStackTrace();
         }

      }
   }
}
