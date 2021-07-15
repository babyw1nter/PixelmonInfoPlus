package io.github.hhui64.PixelmonInfoPlus.hotkey;

import io.github.hhui64.PixelmonInfoPlus.gui.ivev.IVEVGuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class HotKeyManager {
    public static KeyBinding showStatsPanel;

    public HotKeyManager() {
    }

    public static void bind() {
        HotKeyManager.showStatsPanel = new KeyBinding("key.showorhiddenstatspanel.description", Keyboard.KEY_T, "key.showorhiddenstatspanel.category");
        ClientRegistry.registerKeyBinding(HotKeyManager.showStatsPanel);
        MinecraftForge.EVENT_BUS.register(new HotKeyManager.HotKeyListener());
    }

    public static class HotKeyListener {
        @SubscribeEvent
        public void onKeyInput(InputEvent.KeyInputEvent event) {
            if (HotKeyManager.showStatsPanel.isPressed()) {
                IVEVGuiContainer.open();
            }
        }
    }
}
