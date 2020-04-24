package io.github.portlek.nbt.nms.v1_12_R1.base;

import io.github.portlek.nbt.nms.v1_12_R1.NBTBaseEnvelope;
import net.minecraft.server.v1_12_R1.NBTTagString;
import org.jetbrains.annotations.NotNull;

public class NBTStringOf extends NBTBaseEnvelope<NBTTagString> {

    public NBTStringOf(@NotNull final NBTTagString nbt) {
        super(nbt);
    }

}
