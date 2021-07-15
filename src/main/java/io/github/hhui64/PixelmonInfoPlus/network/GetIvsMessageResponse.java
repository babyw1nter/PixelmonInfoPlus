package io.github.hhui64.PixelmonInfoPlus.network;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import io.github.hhui64.PixelmonInfoPlus.PixelmonInfoPlus;
import io.github.hhui64.PixelmonInfoPlus.gui.ivev.IVEVGuiContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
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

public class GetIvsMessageResponse implements IMessage {
    public NBTTagCompound compound;

    public GetIvsMessageResponse() {
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        compound = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, compound);
    }

    public static class IvsMessageHandler implements IMessageHandler<GetIvsMessageResponse, IMessage> {

        @Override
        public IMessage onMessage(GetIvsMessageResponse message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                List<IVStore> slots = new ArrayList<>();

                for (int i = 0; i < 6; i++) {
                    String slotIVsKey = "slot:" + i;
                    String slotIsHtKey = "slot:" + i + "ht";

                    int[] ivsArray = message.compound.getIntArray(slotIVsKey);
                    int[] isHtArray = message.compound.getIntArray(slotIsHtKey);

                    IVStore ivs = new IVStore(ivsArray);
                    ivs.setHyperTrained(StatsType.HP, isHtArray[0] != 0);
                    ivs.setHyperTrained(StatsType.Attack, isHtArray[1] != 0);
                    ivs.setHyperTrained(StatsType.Defence, isHtArray[2] != 0);
                    ivs.setHyperTrained(StatsType.SpecialAttack, isHtArray[3] != 0);
                    ivs.setHyperTrained(StatsType.SpecialDefence, isHtArray[4] != 0);
                    ivs.setHyperTrained(StatsType.Speed, isHtArray[5] != 0);

                    slots.add(ivs);
                    // PixelmonInfoPlus.logger.info("宝可梦槽 {} 的 IVs {}", i, Arrays.toString(slots.get(i)));
                }

                Minecraft.getMinecraft().addScheduledTask(() -> {
                    IVEVGuiContainer.slots = slots;
                });
            }
            return null;
        }
    }
}