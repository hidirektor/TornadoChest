package com.infumia.t3sl4.util.inventory.requirement;

import com.infumia.t3sl4.util.inventory.Requirement;
import java.util.stream.Stream;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class AddedItemReq implements Requirement {
   @NotNull
   private final ItemStack item;

   public AddedItemReq(@NotNull ItemStack item) {
      this.item = item;
   }

   public boolean control(@NotNull InventoryInteractEvent event) {
      boolean var2;
      if (event instanceof InventoryDragEvent) {
         Stream var10000 = ((InventoryDragEvent)event).getNewItems().values().stream();
         ItemStack var10001 = this.item;
         var10001.getClass();
         if (var10000.anyMatch(var10001::equals)) {
            var2 = true;
            return var2;
         }
      }

      var2 = false;
      return var2;
   }
}
