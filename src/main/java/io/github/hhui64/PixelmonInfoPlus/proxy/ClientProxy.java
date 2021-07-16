package io.github.hhui64.PixelmonInfoPlus.proxy;

import io.github.hhui64.PixelmonInfoPlus.gui.ivev.IVEVGuiHandler;
import io.github.hhui64.PixelmonInfoPlus.hotkey.HotKeyManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        IVEVGuiHandler.init();
        HotKeyManager.bind();

        super.init(event);
    }
}
