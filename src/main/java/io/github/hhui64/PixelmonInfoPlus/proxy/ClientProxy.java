package io.github.hhui64.PixelmonInfoPlus.proxy;

import io.github.hhui64.PixelmonInfoPlus.gui.ivev.IVEVGuiHandler;
import io.github.hhui64.PixelmonInfoPlus.hotkey.HotKeyManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        new IVEVGuiHandler();
        HotKeyManager.bind();
        super.init(event);
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        super.serverStarting(event);
    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {
        super.serverStarted(event);
    }
}
