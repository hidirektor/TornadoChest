package io.github.portlek.title.nms.v1_14_R1;

import io.github.portlek.title.api.ITitle;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.Packet;
import net.minecraft.server.v1_14_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_14_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_14_R1.PacketPlayOutTitle.EnumTitleAction;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TitlePlayer1_14_R1 implements ITitle {
   @NotNull
   private final Player player;

   public TitlePlayer1_14_R1(@NotNull Player player) {
      this.player = player;
   }

   public void sendTitle(@NotNull String title, @NotNull String subTitle, int fadeIn, int showTime, int fadeOut) {
      CraftPlayer craftPlayer = (CraftPlayer)this.player;
      EntityPlayer entityPlayer = craftPlayer.getHandle();
      IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + title + "\"}");
      IChatBaseComponent chatSubTitle = ChatSerializer.a("{\"text\": \"" + subTitle + "\"}");
      Packet timePacket = new PacketPlayOutTitle(fadeIn, showTime, fadeOut);
      Packet titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
      Packet subTitlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatSubTitle);
      entityPlayer.playerConnection.sendPacket(titlePacket);
      entityPlayer.playerConnection.sendPacket(subTitlePacket);
      entityPlayer.playerConnection.sendPacket(timePacket);
   }
}
