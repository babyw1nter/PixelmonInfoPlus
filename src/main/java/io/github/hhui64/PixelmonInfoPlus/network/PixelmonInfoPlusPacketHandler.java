package io.github.hhui64.PixelmonInfoPlus.network;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.github.hhui64.PixelmonInfoPlus.PixelmonInfoPlus;
import io.github.hhui64.PixelmonInfoPlus.pixelmon.SlotApi;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public static void sendGetIVSMessageResponseToClient(EntityPlayerMP p) {
        // 获取玩家的宝可梦 party
        PlayerPartyStorage playerPartyStorage = Pixelmon.storageManager.getParty(p);

        if (playerPartyStorage != null) {
            GetIvsMessageResponse getIvsMessageResponse = new GetIvsMessageResponse();
            getIvsMessageResponse.compound = new NBTTagCompound();

            for (int i = 0; i < 6; i++) {
                Pokemon pokemon = playerPartyStorage.get(i);
                String slotIVsKey = "slot:" + i;
                String slotIsHtKey = "slot:" + i + "ht";

                if (pokemon != null && !pokemon.isEgg()) {
                    getIvsMessageResponse.compound.setIntArray(slotIVsKey, pokemon.getIVs().getArray());
                    getIvsMessageResponse.compound.setIntArray(
                            slotIsHtKey,
                            new int[]{pokemon.getIVs().isHyperTrained(StatsType.HP) ? 1 : 0,
                                    pokemon.getIVs().isHyperTrained(StatsType.Attack) ? 1 : 0,
                                    pokemon.getIVs().isHyperTrained(StatsType.Defence) ? 1 : 0,
                                    pokemon.getIVs().isHyperTrained(StatsType.SpecialAttack) ? 1 : 0,
                                    pokemon.getIVs().isHyperTrained(StatsType.SpecialDefence) ? 1 : 0,
                                    pokemon.getIVs().isHyperTrained(StatsType.Speed) ? 1 : 0}
                    );
                } else {
                    getIvsMessageResponse.compound.setIntArray(slotIVsKey, new int[]{0, 0, 0, 0, 0, 0});
                    getIvsMessageResponse.compound.setIntArray(slotIsHtKey, new int[]{0, 0, 0, 0, 0, 0});
                }


            }
            PixelmonInfoPlusPacketHandler.INSTANCE.sendTo(getIvsMessageResponse, p);
        }
    }
}
