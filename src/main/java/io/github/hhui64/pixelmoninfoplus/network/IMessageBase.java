package io.github.hhui64.pixelmoninfoplus.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class IMessageBase implements IMessage {
    public static int protocolVersion = 2;
    public NBTTagCompound compound;

    public IMessageBase() {
        super();
        compound = new NBTTagCompound();
        if (getProtocolVersion() <= 0) {
            compound.setInteger("protocolVersion", IMessageBase.protocolVersion);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        compound = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, compound);
    }

    public int getLocalProtocolVersion() {
        return protocolVersion;
    }

    public int getProtocolVersion() {
        return this.compound.getInteger("protocolVersion");
    }

    public boolean isProtocolVersionMatched() {
        return this.compound.getInteger("protocolVersion") == IMessageBase.protocolVersion;
    }
}