//package io.github.hhui64.PixelmonInfoPlus.command;
//
//import com.pixelmonmod.pixelmon.Pixelmon;
//import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
//import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
//import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
//import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
//import io.github.hhui64.PixelmonInfoPlus.PixelmonInfoPlus;
//import net.minecraft.command.CommandBase;
//import net.minecraft.command.CommandException;
//import net.minecraft.command.ICommandSender;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.util.text.TextComponentString;
//
//import java.util.Arrays;
//
//public class Ivsa extends CommandBase {
//
//    /**
//     * Gets the name of the command
//     */
//    @Override
//    public String getName() {
//        return "ivsa";
//    }
//
//    /**
//     * Gets the usage string for the command.
//     */
//    @Override
//    public String getUsage(ICommandSender sender) {
//        return "";
//    }
//
//    /**
//     * Callback for when the command is executed
//     */
//    @Override
//    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
//        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
//        int slot = Integer.parseInt(args[0].replaceAll("[^0-9]", ""));
//        PlayerPartyStorage playerPartyStorage = Pixelmon.storageManager.getParty(player);
//
//        Pokemon pokemon = playerPartyStorage.get(slot - 1);
//
//        player.sendMessage(new TextComponentString(Arrays.toString(pokemon.getIVs().getArray())));
//    }
//
//    @Override
//    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
//        return true;
//    }
//}
