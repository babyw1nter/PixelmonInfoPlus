package io.github.hhui64.pixelmoninfoplus.pixelmon;


import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class PartyApi {
    public PartyApi() {

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
        return PartyApi.getTeam().stream().map(pokemon -> pokemon.getUUID().toString()).toArray(String[]::new);
    }

    /**
     * 获取玩家当前选择的槽的宝可梦
     *
     * @return pokemon
     */
    public static Pokemon getSelectedPokemon() {
        return ClientStorageManager.party.get(GuiPixelmonOverlay.selectedPixelmon);
    }

    /**
     * 获取宝当前宝可梦的 IVStore 实例
     *
     * @param pokemonIn 指定的 {@link Pokemon}，如为 null 则自动获取当前队伍中选择的宝可梦
     * @return {@link IVStore}
     */
    public static IVStore getCurrentPokemonIVStore(@Nullable Pokemon pokemonIn) {
        Pokemon pokemon = pokemonIn != null ? pokemonIn : PartyApi.getSelectedPokemon();
        if (pokemon != null) {
            // 获取当前宝可梦自身的本地 IVStore
            IVStore localIVStore = pokemon.getIVs();
            // 尝试从缓存处获取当前宝可梦自身的 IVStore
            IVStore remoteIVStore = PartyCache.getPokemonIVStore(pokemon);
            return Arrays.stream(localIVStore.getArray()).sum() == 0 ? remoteIVStore : localIVStore;
        }
        return new IVStore(new int[]{0, 0, 0, 0, 0, 0});
    }


    /**
     * 获取宝当前宝可梦的 EVStore 实例
     *
     * @param pokemonIn 指定的 {@link Pokemon}，如为 null 则自动获取当前队伍中选择的宝可梦
     * @return {@link EVStore}
     */
    public static EVStore getCurrentPokemonEVStore(@Nullable Pokemon pokemonIn) {
        Pokemon pokemon = pokemonIn != null ? pokemonIn : PartyApi.getSelectedPokemon();
        if (pokemon != null) {
            return pokemon.getEVs();
        }
        return new EVStore(new int[]{0, 0, 0, 0, 0, 0});
    }
}
