package io.github.hhui64.PixelmonInfoPlus.pixelmon;


import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public static int setSelectedPixelmon(int slotId) {
        if (slotId < 0)
            slotId = 0;
        if (slotId > 5)
            slotId = 5;
        return slotId;
    }

    public static int getSelectedPokemonSlotId() {
        return GuiPixelmonOverlay.selectedPixelmon;
    }

    public static List<Pokemon> getTeam() {
        return ClientStorageManager.party.getTeam();
    }

    public static String[] getTeamStringUUID() {
        return SlotApi.getTeam().stream().map(pokemon -> pokemon.getUUID().toString()).toArray(String[]::new);
    }

    /**
     * 获取玩家当前选择的槽的宝可梦
     *
     * @return pokemon
     */
    public static Pokemon getSelectedPokemon() {
        return ClientStorageManager.party.get(GuiPixelmonOverlay.selectedPixelmon);
    }
}
