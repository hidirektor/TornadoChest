package com.infumia.t3sl4.util.itemstack.item.meta.get;

import com.infumia.t3sl4.util.itemstack.ScalarRuntime;
import java.util.List;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.cactoos.Scalar;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

public final class FlagOf extends ScalarRuntime<List<ItemFlag>> {
   public FlagOf(@NotNull Scalar<ItemMeta> itemMeta) {
      super(() -> {
         return new ListOf(((ItemMeta)itemMeta.value()).getItemFlags());
      });
   }

   public FlagOf(@NotNull ItemMeta itemMeta) {
      this(() -> {
         return itemMeta;
      });
   }

   public FlagOf(@NotNull ItemStack itemStack) {
      this((Scalar)(new MetaOf(itemStack)));
   }
}
