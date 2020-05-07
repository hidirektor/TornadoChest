package com.infumia.t3sl4.util.inventory;

import org.bukkit.event.inventory.InventoryInteractEvent;
import org.jetbrains.annotations.NotNull;

public interface Target {
   void handle(@NotNull InventoryInteractEvent var1);
}
