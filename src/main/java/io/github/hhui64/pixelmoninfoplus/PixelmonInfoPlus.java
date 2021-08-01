package io.github.hhui64.pixelmoninfoplus;

import io.github.hhui64.pixelmoninfoplus.proxy.CommonProxy;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Mod(modid = PixelmonInfoPlus.MODID, name = PixelmonInfoPlus.NAME, version = PixelmonInfoPlus.VERSION, dependencies = PixelmonInfoPlus.DEPENDENCIES, acceptableRemoteVersions = "*")
public class PixelmonInfoPlus {
    public static final String MODID = "pixelmoninfoplus";
    public static final String NAME = "Pixelmon Info Plus";
    public static final String VERSION = "1.0.1";
    public static final String DEPENDENCIES = "required-after:pixelmon";

    public static Logger logger = LogManager.getLogger("PixelmonInfoPlus");
    public static MinecraftServer server = null;

    @SidedProxy(clientSide = "io.github.hhui64.pixelmoninfoplus.proxy.ClientProxy", serverSide = "io.github.hhui64.pixelmoninfoplus.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(PixelmonInfoPlus.MODID)
    public static PixelmonInfoPlus instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        proxy.serverStarted(event);
    }
}
