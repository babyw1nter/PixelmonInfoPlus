package io.github.hhui64.PixelmonInfoPlus.util;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import io.github.hhui64.PixelmonInfoPlus.network.PixelmonInfoPlusPacketHandler;
import io.github.hhui64.PixelmonInfoPlus.pixelmon.SlotApi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class PartyCache {
    private static final Logger logger = LogManager.getLogger("PartyCache");
    /**
     * 宝可梦的 IVStore 实例缓存 HashMap，使用 Pokemon UUID 为 key 值
     */
    public static Map<String, IVStore> pokemonsIVStore = new HashMap<>();

    public static void addPokemonIVStore(String uuid, IVStore ivStore) {
        if (!PartyCache.pokemonsIVStore.containsKey(uuid)) {
            PartyCache.pokemonsIVStore.put(uuid, ivStore);
        } else {
            PartyCache.pokemonsIVStore.replace(uuid, ivStore);
        }
    }

    public static void addPokemonIVStore(UUID uuid, IVStore ivStore) {
        PartyCache.addPokemonIVStore(uuid.toString(), ivStore);
    }

    public static void addPokemonIVStore(Pokemon pokemon, IVStore ivStore) {
        PartyCache.addPokemonIVStore(pokemon.getUUID(), ivStore);
    }

    /**
     * 获取指定 Pokemon UUID 的 IVStore
     *
     * @param uuid Pokemon UUID
     * @return IVStore
     */
    // TODO 应该还有优化空间，因为 6 个宝可梦槽，只要其中一个不存在缓存就会拉取整个列表...
    public static IVStore getPokemonIVStore(String uuid) {
        return pokemonsIVStore.containsKey(uuid) ? pokemonsIVStore.get(uuid) : new IVStore(new int[]{0, 0, 0, 0, 0, 0});
    }

    /**
     * 获取指定 Pokemon UUID 的 IVStore
     *
     * @param uuid         Pokemon UUID
     * @param shouldUpdate 尚未在缓存中找到时，是否向服务器请求更新缓存
     * @return IVStore
     */
    public static IVStore getPokemonIVStore(String uuid, boolean shouldUpdate) {
        if (shouldUpdate && !pokemonsIVStore.containsKey(uuid)) {
            PartyCache.updateCache(true);
        }
        return PartyCache.getPokemonIVStore(uuid);
    }

    public static IVStore getPokemonIVStore(UUID uuid) {
        return PartyCache.getPokemonIVStore(uuid.toString());
    }

    public static IVStore getPokemonIVStore(UUID uuid, boolean shouldUpdate) {
        return PartyCache.getPokemonIVStore(uuid.toString(), shouldUpdate);
    }

    public static IVStore getPokemonIVStore(Pokemon pokemon) {
        return PartyCache.getPokemonIVStore(pokemon.getUUID());
    }

    public static IVStore getPokemonIVStore(Pokemon pokemon, boolean shouldUpdate) {
        return PartyCache.getPokemonIVStore(pokemon.getUUID(), shouldUpdate);
    }

    public static boolean hasPokemonIVStore(String uuid) {
        return PartyCache.pokemonsIVStore.containsKey(uuid);
    }

    public static boolean hasPokemonIVStore(UUID uuid) {
        return PartyCache.hasPokemonIVStore(uuid.toString());
    }

    public static boolean hasPokemonIVStore(Pokemon pokemon) {
        return PartyCache.hasPokemonIVStore(pokemon.getUUID());
    }

    public static void cleanCache() {
        PartyCache.pokemonsIVStore = new HashMap<>();
    }

    public static void updateCache(boolean forceUpdate) {
        String[] localPartyPokemonsUUID = SlotApi.getTeamStringUUID();
        List<String> nonExistentPokemonUUID = new ArrayList<>();

        if (forceUpdate) {
            PixelmonInfoPlusPacketHandler.sendGetIVSMessageRequestToServer(String.join(":", localPartyPokemonsUUID));
        } else {
            for (String uuid : localPartyPokemonsUUID) {
                if (!PartyCache.pokemonsIVStore.containsKey(uuid)) {
                    nonExistentPokemonUUID.add(uuid);
                }
            }

            if (nonExistentPokemonUUID.size() > 0) {
                PixelmonInfoPlusPacketHandler.sendGetIVSMessageRequestToServer(String.join(":", nonExistentPokemonUUID));
            }
        }
    }

    public static void setCache(Map<String, IVStore> pokemonsIVStore) {
        PartyCache.pokemonsIVStore = pokemonsIVStore;
    }

    public static void addCache(Map<String, IVStore> pokemonsIVStore) {
        for (Map.Entry<String, IVStore> entry : pokemonsIVStore.entrySet()) {
            PartyCache.addPokemonIVStore(entry.getKey(), entry.getValue());
        }
    }
}
