package io.github.hhui64.PixelmonInfoPlus.listeners;

import io.github.hhui64.PixelmonInfoPlus.hotkey.HotKey;
import io.github.hhui64.PixelmonInfoPlus.gui.ivev.IVEVGuiContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HotKeyListener {
    private static final Logger logger = LogManager.getLogger("HOTKEY-EVENT");

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (HotKey.SHOW_IVEVGUI_KEY_BINDING.isPressed()) {
            IVEVGuiContainer.open();
        }
    }
}
