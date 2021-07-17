package io.github.hhui64.pixelmoninfoplus.network;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GetIVSMessageRequest extends IMessageBase {
    private static final Logger logger = LogManager.getLogger("GetIVSMessageRequest");

    public GetIVSMessageRequest() {
        super();
    }

    public static class GetIvsMessageRequestHandler implements IMessageHandler<GetIVSMessageRequest, IMessage> {

        @Override
        public IMessage onMessage(GetIVSMessageRequest message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                // 获取向服务器发送数据包的玩家
                EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
                String queryIVSPokemonUUID = message.compound.getString("query");
                GetIvsMessageResponse getIvsMessageResponse = new GetIvsMessageResponse();

                // 添加为一个计划任务(Scheduled Task)，在主服务器线程上执行操作
                serverPlayer.getServerWorld().addScheduledTask(() -> {
                    // 获取玩家的宝可梦 party
                    PlayerPartyStorage playerPartyStorage = Pixelmon.storageManager.getParty(serverPlayer);

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

                    PixelmonInfoPlusPacketHandler.INSTANCE.sendTo(getIvsMessageResponse, serverPlayer);
                });
            }

            // 回应数据包
            return null;
        }
    }
}