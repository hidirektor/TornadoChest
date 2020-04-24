package io.github.portlek.nbt.nms.v1_13_R1.list;

import io.github.portlek.nbt.nms.v1_13_R1.NBTListEnvelope;
import net.minecraft.server.v1_13_R1.NBTTagLong;
import net.minecraft.server.v1_13_R1.NBTTagLongArray;
import org.jetbrains.annotations.NotNull;

public class NBTLongListOf extends NBTListEnvelope<NBTTagLong, NBTTagLongArray> {

    public NBTLongListOf(@NotNull final NBTTagLongArray nbt) {
        super(nbt);
    }
}
