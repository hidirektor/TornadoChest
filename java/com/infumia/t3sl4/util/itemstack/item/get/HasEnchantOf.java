package com.infumia.t3sl4.util.itemstack.item.get;

import com.infumia.t3sl4.util.itemstack.ScalarRuntime;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

public final class HasEnchantOf extends ScalarRuntime<Boolean> {
   public HasEnchantOf(@NotNull Scalar<ItemStack> itemStackScalar) {
      super(() -> {
         return ((ItemStack)itemStackScalar.value()).getEnchantments().size() > 0;
      });
   }

   public HasEnchantOf(@NotNull Scalar<ItemStack> itemStackScalar, @NotNull Scalar<Enchantment> enchantmentScalar) {
      super(() -> {
         return ((ItemStack)itemStackScalar.value()).containsEnchantment((Enchantment)enchantmentScalar.value());
      });
   }

   public HasEnchantOf(@NotNull Scalar<ItemStack> itemStackScalar, @NotNull Scalar<Enchantment> enchantmentScalar, int level) {
      super(() -> {
         return ((ItemStack)itemStackScalar.value()).getEnchantmentLevel((Enchantment)enchantmentScalar.value()) >= level;
      });
   }
}
