package io.github.hhui64.PixelmonInfoPlus.proxy;

import io.github.hhui64.PixelmonInfoPlus.PixelmonInfoPlus;
import io.github.hhui64.PixelmonInfoPlus.command.IVEVGui;
import io.github.hhui64.PixelmonInfoPlus.gui.ivev.IVEVGuiHandler;
import io.github.hhui64.PixelmonInfoPlus.hotkey.HotKey;
import io.github.hhui64.PixelmonInfoPlus.listeners.HotKeyListener;
import net.minecraft.client.Minecraft;
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
        new HotKey();
        new IVEVGuiHandler();
        ClientRegistry.registerKeyBinding(HotKey.SHOW_IVEVGUI_KEY_BINDING);
        MinecraftForge.EVENT_BUS.register(new HotKeyListener());
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
