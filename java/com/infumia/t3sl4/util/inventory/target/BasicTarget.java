package com.infumia.t3sl4.util.inventory.target;

import com.infumia.t3sl4.util.inventory.Requirement;
import com.infumia.t3sl4.util.inventory.Target;
import com.infumia.t3sl4.util.inventory.event.ElementBasicEvent;
import com.infumia.t3sl4.util.reflection.LoggerOf;
import java.util.logging.Logger;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.cactoos.Proc;
import org.cactoos.scalar.Or;
import org.jetbrains.annotations.NotNull;

public final class BasicTarget implements Target {
   @NotNull
   private static final Logger LOGGER = new LoggerOf(new Class[]{BasicTarget.class});
   @NotNull
   private final Proc<ElementBasicEvent> handler;
   @NotNull
   private final Requirement[] reqs;

   public BasicTarget(@NotNull Proc<ElementBasicEvent> handler, @NotNull Requirement... reqs) {
      this.handler = handler;
      this.reqs = (Requirement[])reqs.clone();
   }

   public void handle(@NotNull InventoryInteractEvent event) {
      try {
         if ((new Or((req) -> {
            return !req.control(event);
         }, this.reqs)).value()) {
            return;
         }

         this.handler.exec(new ElementBasicEvent(event));
      } catch (Exception var3) {
         LOGGER.warning("handle(InventoryInteractEvent) -> Somethings went wrong: " + var3.getMessage());
         var3.printStackTrace();
      }

   }
}
