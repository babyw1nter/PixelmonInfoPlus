package io.github.hhui64.PixelmonInfoPlus.proxy;

//import io.github.hhui64.PixelmonInfoPlus.listeners.RenderListener;
//import io.github.hhui64.PixelmonInfoPlus.listeners.SpawnListener;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonProxy {
    private static Logger logger = LogManager.getLogger("CommonProxy");

    public void preInit(FMLPreInitializationEvent event) {
        // Pixelmon.EVENT_BUS.register(new SpawnListener());
    }

    public void init(FMLInitializationEvent event) {
        // MinecraftForge.EVENT_BUS.register(new RenderListener());
    }

    public void serverStarting(FMLServerStartingEvent event) {
//        event.registerServerCommand(new IVEVGui());
//        event.registerServerCommand(new Ivsa());
        // ClientCommandHandler.instance.registerCommand(new IVEVGui());
    }

    public void serverStarted(FMLServerStartedEvent event) {

    }
}
