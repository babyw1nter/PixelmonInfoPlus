package io.github.hhui64.PixelmonInfoPlus.network;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.github.hhui64.PixelmonInfoPlus.PixelmonInfoPlus;
import io.github.hhui64.PixelmonInfoPlus.gui.ivev.IVEVGuiContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GetIVSMessageRequest implements IMessage {
    public NBTTagCompound compound;

    public GetIVSMessageRequest() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        compound = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, compound);
    }

    public static class GetIvsMessageRequestHandler implements IMessageHandler<GetIVSMessageRequest, IMessage> {

        @Override
        public IMessage onMessage(GetIVSMessageRequest message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                // 获取向服务器发送数据包的玩家
                EntityPlayerMP serverPlayer = ctx.getServerHandler().player;

                // 添加为一个计划任务(Scheduled Task)，在主服务器线程上执行操作
                serverPlayer.getServerWorld().addScheduledTask(() -> {
                    PixelmonInfoPlusPacketHandler.sendGetIVSMessageResponseToClient(serverPlayer);
                });
            }

            // 回应数据包
            return null;
        }
    }
}