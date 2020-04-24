package io.github.portlek.nbt.nms.v1_9_R2;

import io.github.portlek.nbt.api.NBTBase;
import org.jetbrains.annotations.NotNull;

public abstract class NBTBaseEnvelope<T extends net.minecraft.server.v1_9_R2.NBTBase> implements NBTBase<T> {

    @NotNull
    private final T nbt;

    public NBTBaseEnvelope(@NotNull final T nbt) {
        this.nbt = nbt;
    }

    @NotNull
    @Override
    public T value() {
        return nbt;
    }

    @NotNull
    @Override
    public String toString() {
        return nbt.toString();
    }
}
