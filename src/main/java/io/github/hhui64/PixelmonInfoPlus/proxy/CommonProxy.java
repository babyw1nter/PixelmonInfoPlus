package io.github.hhui64.PixelmonInfoPlus.proxy;

import io.github.hhui64.PixelmonInfoPlus.command.IVEVGui;
import io.github.hhui64.PixelmonInfoPlus.hotkey.HotKey;
import io.github.hhui64.PixelmonInfoPlus.gui.ivev.IVEVGuiHandler;
import io.github.hhui64.PixelmonInfoPlus.listeners.HotKeyListener;
//import io.github.hhui64.PixelmonInfoPlus.listeners.RenderListener;
//import io.github.hhui64.PixelmonInfoPlus.listeners.SpawnListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonProxy {
    private static Logger logger = LogManager.getLogger("PBGUI");

    public void preInit(FMLPreInitializationEvent event) {
        // Pixelmon.EVENT_BUS.register(new SpawnListener());
    }

    public void init(FMLInitializationEvent event) {
        new HotKey();
        new IVEVGuiHandler();
        ClientRegistry.registerKeyBinding(HotKey.SHOW_IVEVGUI_KEY_BINDING);
        MinecraftForge.EVENT_BUS.register(new HotKeyListener());
        // MinecraftForge.EVENT_BUS.register(new RenderListener());
    }

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new IVEVGui());
        // ClientCommandHandler.instance.registerCommand(new IVEVGui());
    }

    public void serverStarted(FMLServerStartedEvent event) {

    }
}
