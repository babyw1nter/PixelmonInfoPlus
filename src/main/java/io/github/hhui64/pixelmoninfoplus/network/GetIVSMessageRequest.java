package io.github.hhui64.pixelmoninfoplus.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GetIVSMessageRequest implements IMessage {
    private static final Logger logger = LogManager.getLogger("GetIVSMessageRequest");
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
                    PixelmonInfoPlusPacketHandler.sendGetIVSMessageResponseToClient(serverPlayer, message.compound.getString("query"));
                });
            }

            // 回应数据包
            return null;
        }
    }
}