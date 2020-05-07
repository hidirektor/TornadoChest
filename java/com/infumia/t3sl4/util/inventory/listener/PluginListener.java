package com.infumia.t3sl4.util.inventory.listener;

import com.infumia.t3sl4.util.inventory.Inventories;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.jetbrains.annotations.NotNull;

public final class PluginListener implements Listener {
   @NotNull
   private final Inventories inventories;

   public PluginListener(@NotNull Inventories inventories) {
      this.inventories = inventories;
   }

   @EventHandler
   public void listener(PluginDisableEvent event) {
      this.inventories.processPluginDisable(event);
   }
}
