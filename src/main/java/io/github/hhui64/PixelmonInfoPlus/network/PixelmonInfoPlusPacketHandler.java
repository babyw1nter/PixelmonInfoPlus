package io.github.hhui64.PixelmonInfoPlus.network;

import io.github.hhui64.PixelmonInfoPlus.PixelmonInfoPlus;
import io.github.hhui64.PixelmonInfoPlus.pixelmon.SlotApi;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.List;

public class PixelmonInfoPlusPacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(PixelmonInfoPlus.MODID);
    public static int id = 0;

    public PixelmonInfoPlusPacketHandler() {
        INSTANCE.registerMessage(GetIVSMessageRequest.GetIvsMessageRequestHandler.class, GetIVSMessageRequest.class, id++, Side.SERVER);
        INSTANCE.registerMessage(GetIvsMessageResponse.IvsMessageHandler.class, GetIvsMessageResponse.class, id++, Side.CLIENT);
    }

    public static void sendGetIVSMessageRequestToServer() {
        GetIVSMessageRequest getIVSMessageRequest = new GetIVSMessageRequest();
        getIVSMessageRequest.compound = new NBTTagCompound();
        PixelmonInfoPlusPacketHandler.INSTANCE.sendToServer(getIVSMessageRequest);
    }

    public static void sendGetIVSMessageResponseToClient(List<int[]> slots, EntityPlayerMP p) {
        GetIvsMessageResponse getIvsMessageResponse = new GetIvsMessageResponse();
        getIvsMessageResponse.compound = new NBTTagCompound();
        for (int i = 0; i < slots.size(); i++) {
            String slotKey = "slot:" + i;
            getIvsMessageResponse.compound.setIntArray(slotKey, slots.get(i));
        }
        PixelmonInfoPlusPacketHandler.INSTANCE.sendTo(getIvsMessageResponse, p);
    }
}
