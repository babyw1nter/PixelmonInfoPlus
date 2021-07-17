package io.github.hhui64.pixelmoninfoplus.util;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import io.github.hhui64.pixelmoninfoplus.network.PixelmonInfoPlusPacketHandler;
import io.github.hhui64.pixelmoninfoplus.pixelmon.SlotApi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * 宝可梦队伍缓存类
 *
 * @author hhui64
 */
public class PartyCache {
    private static final Logger logger = LogManager.getLogger("PartyCache");
    /**
     * 宝可梦的 IVStore 实例缓存 HashMap，使用 Pokemon {@link UUID} 为 key 值
     */
    public static Map<String, IVStore> pokemonsIVStore = new HashMap<>();

    /**
     * 添加宝可梦 IVStore 实例缓存
     * <p>
     * 存在则覆盖，不存在则添加
     *
     * @param uuid    Pokemon {@link UUID}
     * @param ivStore {@link IVStore}
     */
    public static void addPokemonIVStore(String uuid, IVStore ivStore) {
        if (!PartyCache.pokemonsIVStore.containsKey(uuid)) {
            PartyCache.pokemonsIVStore.put(uuid, ivStore);
        } else {
            PartyCache.pokemonsIVStore.replace(uuid, ivStore);
        }
    }

    /**
     * 添加宝可梦 IVStore 实例缓存
     * <p>
     * 存在则覆盖，不存在则添加
     *
     * @param uuid    Pokemon {@link UUID}
     * @param ivStore {@link IVStore}
     */
    public static void addPokemonIVStore(UUID uuid, IVStore ivStore) {
        PartyCache.addPokemonIVStore(uuid.toString(), ivStore);
    }

    /**
     * 添加宝可梦 IVStore 实例缓存
     * <p>
     * 存在则覆盖，不存在则添加
     *
     * @param pokemon {@link Pokemon}
     * @param ivStore {@link IVStore}
     */
    public static void addPokemonIVStore(Pokemon pokemon, IVStore ivStore) {
        PartyCache.addPokemonIVStore(pokemon.getUUID(), ivStore);
    }

    /**
     * 获取指定 Pokemon UUID 的 IVStore 实例
     *
     * @param uuid Pokemon {@link UUID}
     * @return {@link IVStore}
     */
    public static IVStore getPokemonIVStore(String uuid) {
        return pokemonsIVStore.containsKey(uuid) ? pokemonsIVStore.get(uuid) : new IVStore(new int[]{0, 0, 0, 0, 0, 0});
    }

    /**
     * 获取指定 Pokemon UUID 的 IVStore 实例
     *
     * @param uuid         Pokemon {@link UUID}
     * @param shouldUpdate 尚未在缓存中找到时，是否向服务器请求更新缓存
     * @return {@link IVStore}
     */
    public static IVStore getPokemonIVStore(String uuid, boolean shouldUpdate) {
        if (shouldUpdate && !pokemonsIVStore.containsKey(uuid)) {
            PartyCache.updateCache(true);
        }
        return PartyCache.getPokemonIVStore(uuid);
    }

    /**
     * 获取指定 Pokemon UUID 的 IVStore 实例
     *
     * @param uuid Pokemon {@link UUID}
     * @return {@link IVStore}
     */
    public static IVStore getPokemonIVStore(UUID uuid) {
        return PartyCache.getPokemonIVStore(uuid.toString());
    }

    /**
     * 获取指定 Pokemon UUID 的 IVStore 实例
     *
     * @param uuid         Pokemon {@link UUID}
     * @param shouldUpdate 尚未在缓存中找到时，是否向服务器请求更新缓存
     * @return {@link IVStore}
     */
    public static IVStore getPokemonIVStore(UUID uuid, boolean shouldUpdate) {
        return PartyCache.getPokemonIVStore(uuid.toString(), shouldUpdate);
    }

    /**
     * 获取指定 Pokemon 实例的 IVStore 实例
     *
     * @param pokemon {@link Pokemon}
     * @return {@link IVStore}
     */
    public static IVStore getPokemonIVStore(Pokemon pokemon) {
        return PartyCache.getPokemonIVStore(pokemon.getUUID());
    }

    /**
     * 获取指定 Pokemon 实例的 IVStore 实例
     *
     * @param pokemon      {@link Pokemon}
     * @param shouldUpdate 尚未在缓存中找到时，是否向服务器请求更新缓存
     * @return {@link IVStore}
     */
    public static IVStore getPokemonIVStore(Pokemon pokemon, boolean shouldUpdate) {
        return PartyCache.getPokemonIVStore(pokemon.getUUID(), shouldUpdate);
    }

    /**
     * 查询缓存中是否存在指定 Pokemon UUID 的 IVStore 实例
     *
     * @param uuid Pokemon {@link UUID}
     * @return true 为存在，false 为不存在
     */
    public static boolean hasPokemonIVStore(String uuid) {
        return PartyCache.pokemonsIVStore.containsKey(uuid);
    }

    /**
     * 查询缓存中是否存在指定 Pokemon UUID 的 IVStore 实例
     *
     * @param uuid Pokemon {@link UUID}
     * @return {@code True} 为存在， {@code False} 为不存在
     */
    public static boolean hasPokemonIVStore(UUID uuid) {
        return PartyCache.hasPokemonIVStore(uuid.toString());
    }

    /**
     * 查询缓存中是否存在指定 Pokemon 实例的 IVStore 实例
     *
     * @param pokemon {@link Pokemon}
     * @return {@code True} 为存在， {@code False} 为不存在
     */
    public static boolean hasPokemonIVStore(Pokemon pokemon) {
        return PartyCache.hasPokemonIVStore(pokemon.getUUID());
    }

    /**
     * 清除缓存
     */
    public static void cleanCache() {
        PartyCache.pokemonsIVStore = new HashMap<>();
    }

    /**
     * 更新缓存
     *
     * @param forceUpdate 是否强制更新<p>
     *                    若此参数为 {@code True}，则向服务端请求当前宝可梦队伍的所有个体值数据。<p>
     *                    若此参数为 {@code False}，则会首先列出当前宝可梦队伍中不存在缓存中的宝可梦的 UUID，然后向服务器请求这些宝可梦的个体值数据。
     */
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

    /**
     * 设置缓存
     * <p>
     * 此方法会直接覆盖缓存 HashMap
     *
     * @param pokemonsIVStore pokemonsIVStore HashMap
     */
    public static void setCache(Map<String, IVStore> pokemonsIVStore) {
        PartyCache.pokemonsIVStore = pokemonsIVStore;
    }

    /**
     * 添加缓存
     * <p>
     * 此方法不会覆盖缓存 HashMap，而是存在的则覆盖，不存在的则添加
     *
     * @param pokemonsIVStore pokemonsIVStore HashMap
     */
    public static void addCache(Map<String, IVStore> pokemonsIVStore) {
        for (Map.Entry<String, IVStore> entry : pokemonsIVStore.entrySet()) {
            PartyCache.addPokemonIVStore(entry.getKey(), entry.getValue());
        }
    }
}
