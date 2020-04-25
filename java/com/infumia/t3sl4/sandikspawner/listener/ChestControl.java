package com.infumia.t3sl4.sandikspawner.listener;

import com.infumia.t3sl4.sandikspawner.SpawnerAPI;
import com.infumia.t3sl4.sandikspawner.chest.placed.ChestPlaced;
import com.infumia.t3sl4.sandikspawner.chest.type.ChestType;
import com.infumia.t3sl4.sandikspawner.mock.MckChestType;
import io.github.portlek.nbt.base.ItemStackNBTOf;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public class ChestControl implements Listener {

    @NotNull
    private final SpawnerAPI spawnerAPI;

    public ChestControl(@NotNull SpawnerAPI spawnerAPI) {
        this.spawnerAPI = spawnerAPI;
    }

    @EventHandler
    public void normalChestPlace(BlockPlaceEvent event) {
        final ChestType chestType = spawnerAPI.findById(
                new ItemStackNBTOf(event.getItemInHand())
                        .nbt()
                        .getNBTCompound("tag")
                        .getString("chest-spawner-id")
        );
        Block block = event.getBlockPlaced();
        Location chestLocation = ChestPlaced.chest.getLocation();

        if (!(chestType instanceof MckChestType)) {
            return;
        }

        int xi = chestLocation.getBlockX();
        int yi = chestLocation.getBlockY();
        int zi = chestLocation.getBlockZ();
        if (block.getType() == Material.CHEST) {
            if (block.getLocation().getX() == xi+1) {
                event.setCancelled(true);
            } else if (block.getLocation().getX() == xi-1) {
                event.setCancelled(true);
            } else if (block.getLocation().getZ() == zi+1) {
                event.setCancelled(true);
            } else if (block.getLocation().getZ() == zi-1) {
                event.setCancelled(true);
            }
        }
    }

}
