package com.infumia.t3sl4.util.inventory;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ElementEvent {
   @NotNull
   Player player();

   void cancel();

   void closeView();
}
