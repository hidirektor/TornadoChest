package com.infumia.t3sl4.util.inventory;

import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Pane {
   void fill(@NotNull Element var1);

   void fill(@NotNull Element... var1);

   void clear();

   boolean add(@NotNull Element var1);

   @NotNull
   Element[] add(@NotNull Element... var1);

   void insert(@NotNull Element var1, int var2, int var3, boolean var4) throws IllegalArgumentException;

   void replaceAll(@NotNull Element... var1);

   void remove(int var1, int var2) throws IllegalArgumentException;

   void subscribe(@NotNull com.infumia.t3sl4.util.observer.Target<Object> var1);

   boolean contains(@NotNull ItemStack var1);

   void accept(@NotNull InventoryInteractEvent var1);

   void displayOn(@NotNull Inventory var1);
}
