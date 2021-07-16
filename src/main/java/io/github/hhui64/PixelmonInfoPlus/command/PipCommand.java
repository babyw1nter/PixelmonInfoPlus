package io.github.hhui64.PixelmonInfoPlus.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@SuppressWarnings("NullableProblems")
public class PipCommand extends CommandBase {
    /**
     * Gets the name of the command
     */
    @Override
    public String getName() {
        return "pip";
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender
     */
    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

    /**
     * Callback for when the command is executed
     *
     * @param server
     * @param sender
     * @param args
     */
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

    }
}
