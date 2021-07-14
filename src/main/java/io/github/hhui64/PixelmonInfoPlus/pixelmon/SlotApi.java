package io.github.hhui64.PixelmonInfoPlus.pixelmon;


import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.github.hhui64.PixelmonInfoPlus.PixelmonInfoPlus;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.w3c.dom.Entity;

import java.util.Arrays;
import java.util.UUID;

public class SlotApi {
    public SlotApi() {

    }

    public static int selectPreviousPixelmon() {
        GuiPixelmonOverlay.selectPreviousPixelmon();
        return GuiPixelmonOverlay.selectedPixelmon;
    }

    public static int selectNextPixelmon() {
        GuiPixelmonOverlay.selectNextPixelmon();
        return GuiPixelmonOverlay.selectedPixelmon;
    }

    // TODO: 尚未完成
    public static int setPixelmon(int slotId) {
        if (slotId < 0)
            slotId = 0;
        if (slotId > 5)
            slotId = 5;
        return 0;
    }

    public static int getSelectedPokemonSlotId() {
        return GuiPixelmonOverlay.selectedPixelmon;
    }

    /**
     * 获取玩家当前选择的槽的宝可梦
     *
     * @return pokemon
     */
    public static Pokemon getSelectedPokemon() {
    }
}
