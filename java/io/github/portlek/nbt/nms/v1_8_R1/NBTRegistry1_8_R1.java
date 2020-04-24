package io.github.portlek.nbt.nms.v1_8_R1;

import io.github.portlek.nbt.api.NBTCompound;
import io.github.portlek.nbt.api.NBTList;
import io.github.portlek.nbt.api.NBTRegistry;
import io.github.portlek.nbt.nms.v1_8_R1.compound.NBTCompoundOf;
import io.github.portlek.nbt.nms.v1_8_R1.list.NBTTagListOf;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.cactoos.list.Mapped;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NBTRegistry1_8_R1 implements NBTRegistry {

    @NotNull
    @Override
    public ItemStack toItemStack(@NotNull NBTCompound nbtCompound) {
        return CraftItemStack.asBukkitCopy(
            net.minecraft.server.v1_8_R1.ItemStack.createStack(
                getTag(
                    nbtCompound.toString()
                )
            )
        );
    }

    @NotNull
    @Override
    public NBTCompound toCompound(@NotNull ItemStack itemStack) {
        return new NBTCompoundOf(
            CraftItemStack.asNMSCopy(itemStack).save(new NBTTagCompound())
        );
    }

    @NotNull
    @Override
    public NBTCompound toCompound(@NotNull String nbtString) {
        return new NBTCompoundOf(
            getTag(nbtString)
        );
    }

    @NotNull
    @Override
    public NBTList toTagStringList(@NotNull List<String> nbtString) {
        final NBTTagList nbtTagList = new NBTTagList();

        new Mapped<>(
            NBTTagString::new,
            nbtString
        ).forEach(nbtTagList::add);
        return new NBTTagListOf(nbtTagList);
    }

    @NotNull
    @Override
    public NBTList toTagStringList(@NotNull String nbtString) {
        final NBTTagList nbtTagList = new NBTTagList();

        nbtTagList.add(
            new NBTTagString(nbtString)
        );
        return new NBTTagListOf(nbtTagList);
    }

    @NotNull
    @Override
    public NBTCompound toCompound(@NotNull CreatureSpawner creatureSpawner) {
        final BlockPosition position = new BlockPosition(
            creatureSpawner.getX(),
            creatureSpawner.getY(),
            creatureSpawner.getZ()
        );
        final World world = ((CraftWorld)creatureSpawner.getWorld()).getHandle();
        final TileEntityMobSpawner spawner = (TileEntityMobSpawner) world.getTileEntity(position);

        if (spawner == null)
            return new NBTCompoundOf(
                new NBTTagCompound()
            );

        NBTTagCompound var1 = new NBTTagCompound();
        spawner.b(var1);
        var1.remove("SpawnPotentials");

        return new NBTCompoundOf(
            var1
        );
    }

    @NotNull
    @Override
    public CreatureSpawner toSpawner(@NotNull CreatureSpawner creatureSpawner, @NotNull NBTCompound nbtCompound) {
        final BlockPosition position = new BlockPosition(
            creatureSpawner.getX(),
            creatureSpawner.getY(),
            creatureSpawner.getZ()
        );
        final World world = ((CraftWorld)creatureSpawner.getWorld()).getHandle();
        final TileEntityMobSpawner spawner = (TileEntityMobSpawner) world.getTileEntity(position);

        if (spawner == null)
            return creatureSpawner;

        spawner.a(
            getTag(
                nbtCompound.toString()
            )
        );

        return creatureSpawner;
    }

    @Override
    public NBTCompound toCompound(@NotNull Entity entity) {
        net.minecraft.server.v1_8_R1.Entity nms = ((CraftEntity)entity).getHandle();
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nms.e(nbtTagCompound);

        return new NBTCompoundOf(
            nbtTagCompound
        );
    }

    @Override
    public Entity toEntity(@NotNull Entity original, @NotNull NBTCompound applied) {
        net.minecraft.server.v1_8_R1.Entity nms = ((CraftEntity)original).getHandle();
        nms.f(
            getTag(
                applied.toString()
            )
        );
        return nms.getBukkitEntity();
    }

    @NotNull
    private NBTTagCompound getTag(@NotNull final String nbtString) {
        try {
            return MojangsonParser.parse(nbtString);
        } catch (Exception e) {
            return new NBTTagCompound();
        }
    }

}
