package io.github.hhui64.pixelmoninfoplus.listeners;

import io.github.hhui64.pixelmoninfoplus.pixelmon.PartyCache;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class FMLNetworkEventHandler {
    @SubscribeEvent
    public void onClientConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        PartyCache.updateCache(true);
    }

    @SubscribeEvent
    public void onClientDisconnectionFromServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        PartyCache.cleanCache();
    }
}
