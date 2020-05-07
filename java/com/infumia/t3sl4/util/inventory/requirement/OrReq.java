package com.infumia.t3sl4.util.inventory.requirement;

import com.infumia.t3sl4.util.inventory.Requirement;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.cactoos.scalar.Or;
import org.jetbrains.annotations.NotNull;

public final class OrReq implements Requirement {
   @NotNull
   private final Requirement[] reqs;

   public OrReq(@NotNull Requirement... reqs) {
      this.reqs = (Requirement[])reqs.clone();
   }

   public boolean control(@NotNull InventoryInteractEvent event) {
      try {
         return (new Or((req) -> {
            return req.control(event);
         }, this.reqs)).value();
      } catch (Exception var3) {
         return false;
      }
   }
}
