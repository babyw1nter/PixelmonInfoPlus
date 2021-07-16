package io.github.hhui64.PixelmonInfoPlus.network;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.github.hhui64.PixelmonInfoPlus.PixelmonInfoPlus;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PixelmonInfoPlusPacketHandler {
    private static final Logger logger = LogManager.getLogger("PixelmonInfoPlusPacketHandler");
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(PixelmonInfoPlus.MODID);
    public static int id = 0;

    public PixelmonInfoPlusPacketHandler() {
    }

    public static void init() {
        INSTANCE.registerMessage(GetIVSMessageRequest.GetIvsMessageRequestHandler.class, GetIVSMessageRequest.class, id++, Side.SERVER);
        INSTANCE.registerMessage(GetIvsMessageResponse.IvsMessageHandler.class, GetIvsMessageResponse.class, id++, Side.CLIENT);
    }

    /**
     * 客户端向服务端发送，请求获取远程 party 的 IVS 列表
     */
    public static void sendGetIVSMessageRequestToServer(String queryIVSPokemonUUID) {
        GetIVSMessageRequest getIVSMessageRequest = new GetIVSMessageRequest();
        getIVSMessageRequest.compound = new NBTTagCompound();
        getIVSMessageRequest.compound.setString("query", queryIVSPokemonUUID);
        PixelmonInfoPlusPacketHandler.INSTANCE.sendToServer(getIVSMessageRequest);
    }

    /**
     * 服务器向指定玩家的客户端发送
     *
     * @param p 玩家
     */
    public static void sendGetIVSMessageResponseToClient(EntityPlayerMP p, String queryIVSPokemonUUID) {
        GetIvsMessageResponse getIvsMessageResponse = new GetIvsMessageResponse();
        getIvsMessageResponse.compound = new NBTTagCompound();

        // 获取玩家的宝可梦 party
        PlayerPartyStorage playerPartyStorage = Pixelmon.storageManager.getParty(p);

        List<UUID> listQueryIVSPokemonUUID = new ArrayList<>();
        if (!queryIVSPokemonUUID.equals("")) {
            listQueryIVSPokemonUUID = Arrays.stream(queryIVSPokemonUUID.split(":")).map(UUID::fromString).collect(Collectors.toList());
            getIvsMessageResponse.compound.setString("query", queryIVSPokemonUUID);
        } else {
            getIvsMessageResponse.compound.setString("query", "");
        }

        if (playerPartyStorage != null) {
            for (UUID pokemonUUID : listQueryIVSPokemonUUID) {
                Pokemon pokemon = playerPartyStorage.get(playerPartyStorage.getSlot(pokemonUUID));

                // 写入 NBT 实例
                if (pokemon != null && !pokemon.isEgg()) {
                    IVStore ivs = pokemon.getIVs();
                    getIvsMessageResponse.compound.setIntArray(pokemon.getUUID().toString(), ivs.getArray());
                    getIvsMessageResponse.compound.setIntArray(
                            pokemon.getUUID().toString() + ":ht",
                            new int[]{ivs.isHyperTrained(StatsType.HP) ? 1 : 0,
                                    ivs.isHyperTrained(StatsType.Attack) ? 1 : 0,
                                    ivs.isHyperTrained(StatsType.Defence) ? 1 : 0,
                                    ivs.isHyperTrained(StatsType.SpecialAttack) ? 1 : 0,
                                    ivs.isHyperTrained(StatsType.SpecialDefence) ? 1 : 0,
                                    ivs.isHyperTrained(StatsType.Speed) ? 1 : 0}
                    );
                } else {
                    getIvsMessageResponse.compound.setIntArray(String.valueOf(pokemonUUID), new IVStore(new int[]{0, 0, 0, 0, 0, 0}).getArray());
                    getIvsMessageResponse.compound.setIntArray(pokemonUUID + ":ht", new int[]{0, 0, 0, 0, 0, 0});
                }
            }
        }

        // 无论如何，都要响应
        PixelmonInfoPlusPacketHandler.INSTANCE.sendTo(getIvsMessageResponse, p);
    }
}
