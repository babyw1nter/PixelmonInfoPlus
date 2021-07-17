package io.github.hhui64.pixelmoninfoplus.proxy;

import io.github.hhui64.pixelmoninfoplus.gui.statspanel.StatsPanelGuiHandler;
import io.github.hhui64.pixelmoninfoplus.keybinding.KeyBindingManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        StatsPanelGuiHandler.init();
        KeyBindingManager.bind();

        super.init(event);
    }
}
