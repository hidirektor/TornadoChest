package com.infumia.t3sl4.util.inventory;

import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Element {
   void displayOn(@NotNull Inventory var1, int var2, int var3);

   void accept(@NotNull InventoryInteractEvent var1);

   boolean is(@NotNull Element var1);

   boolean is(@NotNull ItemStack var1);
}
