package io.github.hhui64.pixelmoninfoplus.keybinding;

import io.github.hhui64.pixelmoninfoplus.gui.statspanel.StatsPanelGuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeyBindingManager {
    public static KeyBinding showStatsPanel;

    public KeyBindingManager() {
    }

    public static void bind() {
        KeyBindingManager.showStatsPanel = new KeyBinding("key.showorhiddenstatspanel.description", Keyboard.KEY_T, "key.showorhiddenstatspanel.category");
        ClientRegistry.registerKeyBinding(KeyBindingManager.showStatsPanel);
        MinecraftForge.EVENT_BUS.register(new KeyBindingManager.KeyBindingEventHandler());
    }

    public static class KeyBindingEventHandler {
        @SubscribeEvent
        public void onKeyInput(InputEvent.KeyInputEvent event) {
            if (KeyBindingManager.showStatsPanel.isPressed()) {
                StatsPanelGuiContainer.open();
            }
        }
    }
}
