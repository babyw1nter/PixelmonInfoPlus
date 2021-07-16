package io.github.hhui64.PixelmonInfoPlus.proxy;

import io.github.hhui64.PixelmonInfoPlus.PixelmonInfoPlus;
import io.github.hhui64.PixelmonInfoPlus.listeners.FMLNetworkEventHandler;
import io.github.hhui64.PixelmonInfoPlus.network.PixelmonInfoPlusPacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonProxy {
    private static Logger logger = LogManager.getLogger("CommonProxy");

    public void preInit(FMLPreInitializationEvent event) {
        PixelmonInfoPlusPacketHandler.init();
    }

    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new FMLNetworkEventHandler());
    }

    public void serverStarting(FMLServerStartingEvent event) {
        PixelmonInfoPlus.server = FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    public void serverStarted(FMLServerStartedEvent event) {

    }
}
