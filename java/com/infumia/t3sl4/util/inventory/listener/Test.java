package com.infumia.t3sl4.util.inventory.listener;

import java.util.HashMap;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

public class Test implements Listener {
   @NotNull
   private final Plugin plugin;
   private HashMap<Player, Boolean> activemap = new HashMap();
   private HashMap<Player, Long> activecooldown = new HashMap();
   private HashMap<Player, Boolean> ultimap = new HashMap();
   private HashMap<Player, Long> ulticooldown = new HashMap();
   private HashMap<Player, ItemStack> helmet = new HashMap();
   private HashMap<Player, ItemStack> chestplate = new HashMap();
   private HashMap<Player, ItemStack> leggings = new HashMap();
   private HashMap<Player, ItemStack> boots = new HashMap();
   private static final String RANKSUIKASTCI = "ranksuikastci";

   public Test(@NotNull Plugin plugin) {
      this.plugin = plugin;
   }

   @EventHandler
   public void activeSkills(PlayerInteractEvent e) {
      Player player = e.getPlayer();
      Action action = e.getAction();
      if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR || player.hasPermission("ranksuikastci") && e.getItem() != null) {
         Material item = ((ItemStack)Objects.requireNonNull(e.getItem())).getType();
         if (item == Material.POTION) {
            if (e.getItem().getData() != null) {
               this.instaPotion(player, e.getItem());
            }
         } else if (item == Material.DIAMOND_SWORD || item == Material.GOLDEN_SWORD || item == Material.IRON_SWORD || item == Material.STONE_SWORD || item == Material.WOODEN_SWORD) {
            if (player.isSneaking()) {
               if (this.ulticooldown.get(player) == null) {
                  this.ultimap.put(player, true);
                  this.ulticooldown.put(player, System.currentTimeMillis());
                  Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                     Long var10000 = (Long)this.ulticooldown.remove(player);
                  }, 240L);
                  Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                     if (this.ultimap.get(player) != null) {
                        this.ultimap.remove(player);
                     }

                  }, 120L);
               } else {
                  this.getCooldownUlti(player);
               }
            } else if (this.activecooldown.get(player) == null) {
               this.activecooldown.put(player, System.currentTimeMillis());
               this.activemap.put(player, true);
               this.saveArmor(player);
               this.clearArmor(player);
               Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                  this.activecooldown.remove(player);
                  player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 0, false, false), true);
                  ((AttributeInstance)Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED))).setBaseValue(0.30000001192092896D);
               }, 400L);
               Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                  if (this.activemap.get(player) != null) {
                     this.activemap.remove(player);
                     this.armorCheck(player.getLocation(), player);
                     this.clearData(player);
                  }

                  if (((AttributeInstance)Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED))).getBaseValue() != 0.20000000298023224D) {
                     ((AttributeInstance)Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED))).setBaseValue(0.20000000298023224D);
                  }

               }, 200L);
            } else {
               this.getCooldownActive(player);
            }
         }
      }

   }

   @EventHandler
   public void onDamage(EntityDamageByEntityEvent e) {
      if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
         Player player = (Player)e.getEntity();
         Player damager = (Player)e.getDamager();
         if (player.hasPermission("ranksuikastci") && e.getDamager() instanceof Player && this.activemap.get(player) != null) {
            this.activemap.remove(player);
            this.armorCheck(player.getLocation(), player);
         }

         if (damager.hasPermission("ranksuikastci") && this.ultimap.get(player) != null) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 5, 0), true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3, 0), true);
            this.ultimap.remove(player);
         }

      }
   }

   private void instaPotion(Player player, ItemStack item) {
      PotionMeta potionMeta = (PotionMeta)item.getItemMeta();

      assert potionMeta != null;

      PotionData potionData = potionMeta.getBasePotionData();
      PotionType potionType = potionData.getType();
      PotionEffectType potionEffectType = potionType.getEffectType();

      assert potionEffectType != null;

      if (!potionEffectType.equals(PotionEffectType.INVISIBILITY) && !potionEffectType.equals(PotionEffectType.FIRE_RESISTANCE) && !potionEffectType.equals(PotionEffectType.WATER_BREATHING) && !potionEffectType.equals(PotionEffectType.NIGHT_VISION)) {
         if (!potionEffectType.equals(PotionEffectType.HEAL) && !potionEffectType.equals(PotionEffectType.HARM)) {
            if (!potionEffectType.equals(PotionEffectType.INCREASE_DAMAGE) && !potionEffectType.equals(PotionEffectType.SPEED) && !potionEffectType.equals(PotionEffectType.JUMP)) {
               if (!potionEffectType.equals(PotionEffectType.REGENERATION) && !potionEffectType.equals(PotionEffectType.POISON)) {
                  if (!potionEffectType.equals(PotionEffectType.SLOW_FALLING) && !potionEffectType.equals(PotionEffectType.WEAKNESS)) {
                     if (potionEffectType.equals(PotionEffectType.LUCK)) {
                        player.addPotionEffect(new PotionEffect(potionEffectType, 6000, 0));
                     } else if (potionType == PotionType.SLOWNESS) {
                        if (potionData.isExtended()) {
                           player.addPotionEffect(new PotionEffect(potionEffectType, 4800, 0));
                        } else if (potionData.isUpgraded()) {
                           player.addPotionEffect(new PotionEffect(potionEffectType, 400, 3));
                        } else {
                           player.addPotionEffect(new PotionEffect(potionEffectType, 1800, 0));
                        }
                     } else if (potionType == PotionType.TURTLE_MASTER) {
                        if (potionData.isExtended()) {
                           player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 800, 2));
                           player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 800, 3));
                        } else if (potionData.isUpgraded()) {
                           player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 3));
                           player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 400, 5));
                        } else {
                           player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 2));
                           player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 400, 3));
                        }
                     }
                  } else if (potionData.isExtended()) {
                     player.addPotionEffect(new PotionEffect(potionEffectType, 4800, 0));
                  } else {
                     player.addPotionEffect(new PotionEffect(potionEffectType, 1800, 0));
                  }
               } else if (potionData.isExtended()) {
                  player.addPotionEffect(new PotionEffect(potionEffectType, 1800, 0));
               } else if (potionData.isUpgraded()) {
                  player.addPotionEffect(new PotionEffect(potionEffectType, 440, 1));
               } else {
                  player.addPotionEffect(new PotionEffect(potionEffectType, 900, 0));
               }
            } else if (potionData.isExtended()) {
               player.addPotionEffect(new PotionEffect(potionEffectType, 9600, 0));
            } else if (potionData.isUpgraded()) {
               player.addPotionEffect(new PotionEffect(potionEffectType, 1800, 1));
            } else {
               player.addPotionEffect(new PotionEffect(potionEffectType, 3600, 0));
            }
         } else if (potionData.isUpgraded()) {
            player.addPotionEffect(new PotionEffect(potionEffectType, 10, 1));
         } else {
            player.addPotionEffect(new PotionEffect(potionEffectType, 10, 0));
         }
      } else if (potionData.isExtended()) {
         player.addPotionEffect(new PotionEffect(potionEffectType, 9600, 0), true);
      } else {
         player.addPotionEffect(new PotionEffect(potionEffectType, 3600, 0), true);
      }

      player.getInventory().setItemInMainHand(new ItemStack(Material.GLASS_BOTTLE));
      ((World)Objects.requireNonNull(Bukkit.getWorld(player.getWorld().getName()))).playSound(player.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0F, 1.0F);
   }

   private void armorCheck(Location loc, Player player) {
      ItemStack air = new ItemStack(Material.AIR);
      if (!Objects.equals(player.getInventory().getHelmet(), air)) {
         ((World)Objects.requireNonNull(Bukkit.getServer().getWorld(((World)Objects.requireNonNull(loc.getWorld())).getName()))).dropItemNaturally(loc, player.getInventory().getHelmet());
      }

      player.getInventory().setHelmet((ItemStack)this.helmet.get(player));
      if (!Objects.equals(player.getInventory().getChestplate(), air)) {
         ((World)Objects.requireNonNull(Bukkit.getServer().getWorld(((World)Objects.requireNonNull(loc.getWorld())).getName()))).dropItemNaturally(loc, player.getInventory().getChestplate());
      }

      player.getInventory().setChestplate((ItemStack)this.chestplate.get(player));
      if (!Objects.equals(player.getInventory().getLeggings(), air)) {
         ((World)Objects.requireNonNull(Bukkit.getServer().getWorld(((World)Objects.requireNonNull(loc.getWorld())).getName()))).dropItemNaturally(loc, player.getInventory().getLeggings());
      }

      player.getInventory().setLeggings((ItemStack)this.leggings.get(player));
      if (!Objects.equals(player.getInventory().getBoots(), air)) {
         ((World)Objects.requireNonNull(Bukkit.getServer().getWorld(((World)Objects.requireNonNull(loc.getWorld())).getName()))).dropItemNaturally(loc, player.getInventory().getBoots());
      }

      player.getInventory().setBoots((ItemStack)this.boots.get(player));
      this.clearData(player);
   }

   private void saveArmor(Player player) {
      this.helmet.put(player, player.getInventory().getHelmet());
      this.chestplate.put(player, player.getInventory().getChestplate());
      this.leggings.put(player, player.getInventory().getLeggings());
      this.boots.put(player, player.getInventory().getBoots());
   }

   private void clearArmor(Player player) {
      ItemStack air = new ItemStack(Material.AIR);
      player.getInventory().setHelmet(air);
      player.getInventory().setChestplate(air);
      player.getInventory().setLeggings(air);
      player.getInventory().setBoots(air);
   }

   private void clearData(Player player) {
      this.helmet.remove(player);
      this.chestplate.remove(player);
      this.leggings.remove(player);
      this.boots.remove(player);
   }

   private void getCooldownActive(Player p) {
      p.sendMessage(String.valueOf((Long)this.activecooldown.get(p) / 1000L + 20L - System.currentTimeMillis() / 1000L));
   }

   private void getCooldownUlti(Player p) {
      p.sendMessage(String.valueOf((Long)this.ulticooldown.get(p) / 1000L + 10L - System.currentTimeMillis() / 1000L));
   }
}
