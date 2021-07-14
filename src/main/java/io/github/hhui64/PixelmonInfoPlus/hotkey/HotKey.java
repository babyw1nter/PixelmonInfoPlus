package io.github.hhui64.PixelmonInfoPlus.hotkey;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class HotKey {
    public static KeyBinding SHOW_IVEVGUI_KEY_BINDING;

    public HotKey() {
        HotKey.SHOW_IVEVGUI_KEY_BINDING = new KeyBinding("显示/隐藏 属性值界面", Keyboard.KEY_T, "Pixelmon Info Plus - 更多宝可梦信息");
    }
}
