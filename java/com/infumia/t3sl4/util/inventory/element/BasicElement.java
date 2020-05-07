package com.infumia.t3sl4.util.inventory.element;

import com.infumia.t3sl4.util.inventory.Element;
import com.infumia.t3sl4.util.inventory.Requirement;
import com.infumia.t3sl4.util.inventory.Target;
import com.infumia.t3sl4.util.inventory.requirement.AddedElementReq;
import com.infumia.t3sl4.util.inventory.requirement.ClickedElementReq;
import com.infumia.t3sl4.util.inventory.requirement.DragReq;
import com.infumia.t3sl4.util.inventory.requirement.OrReq;
import com.infumia.t3sl4.util.nbt.api.NBTCompound;
import com.infumia.t3sl4.util.nbt.base.ItemStackNBTOf;
import com.infumia.t3sl4.util.reflection.LoggerOf;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.cactoos.iterable.IterableOf;
import org.cactoos.scalar.And;
import org.jetbrains.annotations.NotNull;

public final class BasicElement implements Element {
   @NotNull
   private static final Logger LOGGER = new LoggerOf(new Class[]{BasicElement.class});
   @NotNull
   private ItemStack icon;
   @NotNull
   private Target[] targets;
   @NotNull
   private Requirement elementReq;

   public BasicElement(@NotNull ItemStack icon, @NotNull String id, boolean encrypted, @NotNull Target... targets) {
      this.icon = encrypted ? this.encrypted(icon, id) : icon;
      this.targets = (Target[])targets.clone();
      this.elementReq = new OrReq(new Requirement[]{new ClickedElementReq(this), (Requirement)(icon.getType() == Material.AIR ? new DragReq() : new AddedElementReq(this))});
   }

   public BasicElement(@NotNull ItemStack icon, @NotNull String id, @NotNull Target... targets) {
      this(icon, id, true, targets);
   }

   public BasicElement(@NotNull ItemStack icon, boolean encrypted, @NotNull Target... targets) {
      this(icon, UUID.randomUUID().toString() + System.currentTimeMillis(), encrypted, targets);
   }

   public BasicElement(@NotNull ItemStack icon, @NotNull Target... targets) {
      this(icon, UUID.randomUUID().toString() + System.currentTimeMillis(), true, targets);
   }

   public BasicElement(@NotNull ItemStack icon, @NotNull String id, boolean encrypted) {
      this(icon, id, encrypted, new Target[0]);
   }

   public BasicElement(@NotNull ItemStack icon, @NotNull String id) {
      this(icon, id, true);
   }

   public BasicElement(@NotNull ItemStack icon, boolean encrypted) {
      this(icon, encrypted, new Target[0]);
   }

   public BasicElement(@NotNull ItemStack icon) {
      this(icon, true);
   }

   @NotNull
   private ItemStack encrypted(@NotNull ItemStack itemStack, @NotNull String id) {
      if (itemStack.getType() == Material.AIR) {
         return itemStack;
      } else {
         ItemStack encryptedItem = itemStack.clone();
         ItemStackNBTOf itemStackNBTOf = new ItemStackNBTOf(encryptedItem);
         NBTCompound itemNBT = itemStackNBTOf.nbt();
         NBTCompound tag = itemNBT.getNBTCompound("tag");
         tag.setString("inventories-id", id);
         itemNBT.set("tag", tag);
         return itemStackNBTOf.apply(itemNBT);
      }
   }

   public void displayOn(@NotNull Inventory inventory, int locX, int locY) {
      inventory.setItem(locX + locY * 9, this.icon.clone());
   }

   public void accept(@NotNull InventoryInteractEvent event) {
      if (this.elementReq.control(event)) {
         try {
            (new And((target) -> {
               target.handle(event);
            }, new IterableOf<>(this.targets))).value();
         } catch (Exception var3) {
            LOGGER.warning("accept(InventoryInteractEvent) -> Somethings went wrong\n" + var3.getMessage());
         }

      }
   }

   public boolean is(@NotNull ItemStack itemStack) {
      return itemStack.isSimilar(this.icon);
   }

   public boolean is(@NotNull Element element) {
      return element instanceof BasicElement && this.is(((BasicElement)element).icon);
   }
}
