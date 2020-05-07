package com.infumia.t3sl4.util.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public interface Page extends InventoryHolder, com.infumia.t3sl4.util.observer.Target<Object> {
   void add(@NotNull Pane var1, int var2);

   void remove(int var1);

   void rearrange(int var1, int var2);

   void defineHolder(@NotNull Page var1);

   void showTo(@NotNull Player var1);

   void handleClose(@NotNull InventoryCloseEvent var1);

   void accept(@NotNull InventoryInteractEvent var1);
}
