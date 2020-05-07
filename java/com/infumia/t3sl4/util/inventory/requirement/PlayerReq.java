package com.infumia.t3sl4.util.inventory.requirement;

import com.infumia.t3sl4.util.inventory.Requirement;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerReq implements Requirement {
   @NotNull
   private final Player player;

   public PlayerReq(@NotNull Player player) {
      this.player = player;
   }

   public boolean control(@NotNull InventoryInteractEvent event) {
      return event.getWhoClicked().getUniqueId().equals(this.player.getUniqueId());
   }
}
