package com.infumia.t3sl4.util.inventory;

import com.infumia.t3sl4.util.inventory.listener.InventoryClickListener;
import com.infumia.t3sl4.util.inventory.listener.InventoryCloseListener;
import com.infumia.t3sl4.util.inventory.listener.InventoryDragListener;
import com.infumia.t3sl4.util.inventory.listener.PluginListener;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

public final class Inventories {
   @NotNull
   private static final List<Listener> LISTENERS = new ListOf(new Listener[]{new PluginListener(new Inventories()), new InventoryClickListener(), new InventoryCloseListener(), new InventoryDragListener()});
   @NotNull
   private static final Queue<Plugin> PLUGIN_QUEUE = new ConcurrentLinkedQueue();

   public void prepareFor(@NotNull Plugin plugin) {
      if (PLUGIN_QUEUE.isEmpty()) {
         this.registerListeners(plugin);
      }

      synchronized(this) {
         PLUGIN_QUEUE.add(plugin);
      }
   }

   public void processPluginDisable(@NotNull PluginDisableEvent event) {
      if (!((Plugin)PLUGIN_QUEUE.peek()).equals(event.getPlugin())) {
         synchronized(this) {
            PLUGIN_QUEUE.remove(event.getPlugin());
         }
      } else {
         synchronized(this) {
            PLUGIN_QUEUE.poll();
         }

         Plugin nextPlugin = (Plugin)PLUGIN_QUEUE.peek();
         if (nextPlugin != null && nextPlugin.isEnabled()) {
            this.registerListeners(nextPlugin);
         }
      }
   }

   private void registerListeners(@NotNull Plugin plugin) {
      synchronized(this) {
         LISTENERS.forEach((listener) -> {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
         });
      }
   }
}
