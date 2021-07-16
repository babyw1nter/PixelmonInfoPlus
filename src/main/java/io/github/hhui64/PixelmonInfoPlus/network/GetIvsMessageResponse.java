package io.github.hhui64.PixelmonInfoPlus.network;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import io.github.hhui64.PixelmonInfoPlus.util.PartyCache;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class GetIvsMessageResponse implements IMessage {
    private static final Logger logger = LogManager.getLogger("GetIvsMessageResponse");
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
                List<UUID> listQueryIVSPokemonUUID;
                Map<String, IVStore> pokemonsIVStore = new HashMap<>();

                if (!message.compound.getString("query").equals("")) {
                    listQueryIVSPokemonUUID = Arrays.stream(message.compound.getString("query").split(":")).map(UUID::fromString).collect(Collectors.toList());

                    for (UUID pokemonUUID : listQueryIVSPokemonUUID) {
                        int[] ivsIntArray = message.compound.getIntArray(pokemonUUID.toString());
                        int[] ivsHtArray = message.compound.getIntArray(pokemonUUID + ":ht");

                        if (ivsIntArray.length > 0) {
                            IVStore ivs = new IVStore(ivsIntArray);
                            if (ivsHtArray.length > 0) {
                                ivs.setHyperTrained(StatsType.HP, ivsHtArray[0] != 0);
                                ivs.setHyperTrained(StatsType.Attack, ivsHtArray[1] != 0);
                                ivs.setHyperTrained(StatsType.Defence, ivsHtArray[2] != 0);
                                ivs.setHyperTrained(StatsType.SpecialAttack, ivsHtArray[3] != 0);
                                ivs.setHyperTrained(StatsType.SpecialDefence, ivsHtArray[4] != 0);
                                ivs.setHyperTrained(StatsType.Speed, ivsHtArray[5] != 0);
                            }

                            pokemonsIVStore.put(pokemonUUID.toString(), ivs);
                        }
                    }
                }

                Minecraft.getMinecraft().addScheduledTask(() -> {
                    // 添加进缓存
                    PartyCache.addCache(pokemonsIVStore);
                });
            }
            return null;
        }
    }
}