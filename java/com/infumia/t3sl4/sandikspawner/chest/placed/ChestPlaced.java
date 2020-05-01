package com.infumia.t3sl4.sandikspawner.chest.placed;

import com.infumia.t3sl4.sandikspawner.SandikSpawner;
import com.infumia.t3sl4.sandikspawner.SpawnerAPI;
import com.infumia.t3sl4.sandikspawner.chest.type.ChestType;
import com.infumia.t3sl4.util.itemstack.util.Colored;
import com.infumia.t3sl4.util.location.StringOf;
import io.github.portlek.mcyaml.IYaml;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.cactoos.list.ListOf;
import org.cactoos.list.Mapped;
import org.cactoos.scalar.SumOf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class ChestPlaced {
   @NotNull
   private final SpawnerAPI spawnerAPI;
   public static Chest chest2;
   @NotNull
   public final UUID uuid;
   @NotNull
   public final UUID owner;
   @NotNull
   public final Chest chest;
   @NotNull
   public final Sign sign;
   @NotNull
   public final ChestType chestType;
   @NotNull
   private final List<Entity> entities = new ArrayList();
   public int storage;
   public int level;
   @NotNull
   private String esyaDusmeDurumu = "İç";
   private int durum;
   private long canDrop;
   @Nullable
   private BukkitTask task;

   public ChestPlaced(@NotNull SpawnerAPI spawnerAPI, @NotNull UUID uuid, @NotNull UUID owner, @NotNull Chest chest, @NotNull Sign sign, @NotNull ChestType chestType, int storage, int durum, int level) {
      this.spawnerAPI = spawnerAPI;
      this.canDrop = 0L;
      this.uuid = uuid;
      this.chest2 = chest;
      this.owner = owner;
      this.chest = chest;
      this.sign = sign;
      this.chestType = chestType;
      this.storage = storage;
      this.durum = durum;
      this.level = level;
      this.updateEsyaDusmesi();
   }

   private void updateEsyaDusmesi() {
      if (this.durum == 0) {
         this.esyaDusmeDurumu = this.spawnerAPI.getLanguage().chestStatusIn;
      } else if (this.durum == 1) {
         this.esyaDusmeDurumu = this.spawnerAPI.getLanguage().chestStatusOut;
      } else {
         this.esyaDusmeDurumu = this.spawnerAPI.getLanguage().chestStatusStorage;
      }

   }

   public void changeEsyaDusmesi(int i) {
      this.durum = i;
      this.updateEsyaDusmesi();
      this.saveTo(this.spawnerAPI.placedChest);
   }

   public void saveTo(@NotNull IYaml yaml) {
      yaml.set("Spawners." + this.uuid.toString() + ".owner", this.owner.toString());
      yaml.set("Spawners." + this.uuid.toString() + ".chest-location", (new StringOf(this.chest.getLocation())).asKey());
      yaml.set("Spawners." + this.uuid.toString() + ".sign-location", (new StringOf(this.sign.getLocation())).asKey());
      yaml.set("Spawners." + this.uuid.toString() + ".type", this.chestType.getId());
      yaml.set("Spawners." + this.uuid.toString() + ".level", this.level);
      yaml.set("Spawners." + this.uuid.toString() + ".status", this.durum);
      yaml.set("Spawners." + this.uuid.toString() + ".storage", this.storage);
   }

   public void changeEsyaDusmesi() {
      if (this.durum < 2) {
         ++this.durum;
      } else {
         this.durum = 0;
      }

      this.updateEsyaDusmesi();
      this.saveTo(this.spawnerAPI.placedChest);
   }

   public void remove() {
      Bukkit.getScheduler().runTask(SandikSpawner.getInstance(), () -> {
         this.sign.getBlock().setType(Material.AIR);
         this.chest.getBlock().setType(Material.AIR);
      });
   }

   public void levelUp() {
      if (this.level < this.chestType.maxLevel()) {
         ++this.level;
         this.update(false);
         this.saveTo(this.spawnerAPI.placedChest);
      }
   }

   public void update(boolean canDrop) {
      this.updateSign();
      this.task = Bukkit.getScheduler().runTaskTimer(this.spawnerAPI.plugin, () -> {
         if (canDrop && this.canDrop() && Bukkit.getPlayer(this.owner) != null) {
            ItemStack drop = this.chestType.getDrop(this.level);
            if (this.durum == 0) {
               this.chest.getBlockInventory().addItem(new ItemStack[]{drop});
            } else if (this.durum == 1) {
               Location location = this.chest.getLocation();
               if (location.getWorld() != null) {
                  location.getWorld().dropItemNaturally(location, drop);
               }
            } else {
               this.addItemToStorage(drop);
            }
         }
      }, 0L, (long)this.chestType.speed(this.level - 1) * 20L);
   }

   public void updateSign() {
      this.sign.setLine(0, this.replaceSignLines((String)this.spawnerAPI.getConfigs().sign.get(0)));
      this.sign.setLine(1, this.replaceSignLines((String)this.spawnerAPI.getConfigs().sign.get(1)));
      this.sign.setLine(2, this.replaceSignLines((String)this.spawnerAPI.getConfigs().sign.get(2)));
      this.sign.setLine(3, this.replaceSignLines((String)this.spawnerAPI.getConfigs().sign.get(3)));
      this.sign.update();
   }

   private boolean canDrop() {
      this.canDrop += 3L;
      if (this.canDrop >= this.chestType.speed(this.level - 1)) {
         this.canDrop = 0L;
         return true;
      } else {
         return false;
      }
   }

   public void close() {
      if (this.task != null) {
         this.task.cancel();
         this.task = null;
      }

   }


   public void addItemToStorage(@NotNull ItemStack itemStack) {
      this.storage = (new SumOf(new Integer[]{itemStack.getAmount(), this.storage})).intValue();
      this.saveTo(this.spawnerAPI.placedChest);
   }

   @NotNull
   private String replaceSignLines(@NotNull String line) {
      return (String)(new Colored(line.replaceAll("%chest-name%", this.chestType.getName()).replaceAll("%chest-owner%", (String)Optional.ofNullable(Bukkit.getOfflinePlayer(this.owner).getName()).orElse("UNKNOWN")).replaceAll("%chest-level%", String.valueOf(this.level)).replaceAll("%durum%", this.spawnerAPI.getDurumFromInt(this.durum)))).value();
   }

   public void replaceAll(@NotNull ItemStack itemStack) {
      ItemMeta meta = itemStack.getItemMeta();
      if (meta != null) {
         String display = meta.getDisplayName();
         List<String> lore = meta.getLore() == null ? new ListOf(new String[0]) : meta.getLore();
         meta.setDisplayName(this.replaceAll(display));
         meta.setLore(this.replaceAll((List)lore));
         itemStack.setItemMeta(meta);
      }
   }

   @NotNull
   public List<String> replaceAll(@NotNull List<String> replaced) {
      return new Mapped<>(this::replaceAll, replaced);
   }

   @NotNull
   public String replaceAll(@NotNull String replaced) {
      return replaced.replaceAll("%durum%", this.esyaDusmeDurumu);
   }
}
