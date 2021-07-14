//package io.github.hhui64.PixelmonInfoPlus.listeners;
//
//import net.minecraftforge.fml.common.FMLCommonHandler;
//import org.apache.logging.log4j.Logger;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//import net.minecraftforge.fml.common.eventhandler.EventPriority;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.player.*;
//import net.minecraft.server.*;
//import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
//import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
//import com.pixelmonmod.pixelmon.enums.EnumBossMode;
//import org.apache.logging.log4j.LogManager;
//
//import java.util.List;
//
//public class SpawnListener {
//    private static final Logger logger = LogManager.getLogger("SPAWN-EVENT");
//
//    @SubscribeEvent(priority = EventPriority.LOWEST)
//    public void onSpawnEvent(final SpawnEvent event) {
//        final Entity entity = event.action.getOrCreateEntity();
//        final String worldName = entity.getEntityWorld().getWorldInfo().getWorldName();
//
//        // 判断是否是宝可梦实体类
//        if (entity instanceof EntityPixelmon) {
//            final EntityPixelmon pokemonEntity = (EntityPixelmon) entity;
//
//            if (pokemonEntity.isBossPokemon()) {
//                EnumBossMode bossMode = pokemonEntity.getBossMode();
//                // 交给对应方法去处理
//                this.onBossSpawnHandler(pokemonEntity, bossMode);
//            }
//
//            MinecraftServer s = FMLCommonHandler.instance().getMinecraftServerInstance().getServer();
//
//            if (s != null) {
//                final List<EntityPlayerMP> playerList = s.getPlayerList().getPlayers();
//                // playerList.forEach(p -> notice.sendTo(p));
//            }
//
//        }
//
//
//    }
//
//
//    /**
//     * boss 生成事件处理方法
//     *
//     * @param pokemonEntity 宝可梦实体
//     * @param bossMode      Boss类型
//     */
//    private void onBossSpawnHandler(EntityPixelmon pokemonEntity, EnumBossMode bossMode) {
//        String pokemonName = pokemonEntity.getPokemonName();
//        String pokemonLocalizedName = pokemonEntity.getLocalizedName();
//
//        logger.info("PBGUI >> {}", pokemonLocalizedName);
//    }
//}
