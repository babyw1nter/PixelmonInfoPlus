package io.github.hhui64.pixelmoninfoplus.network;

import io.github.hhui64.pixelmoninfoplus.PixelmonInfoPlus;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        getIVSMessageRequest.compound.setString("query", queryIVSPokemonUUID);
        PixelmonInfoPlusPacketHandler.INSTANCE.sendToServer(getIVSMessageRequest);
    }
}
