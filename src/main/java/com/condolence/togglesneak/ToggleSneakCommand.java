package com.condolence.togglesneak;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.Collections;
import java.util.List;

public class ToggleSneakCommand extends CommandBase {
    private final ToggleSneakMod mod;

    public ToggleSneakCommand(final ToggleSneakMod mod) {
        this.mod = mod;
    }

    public String getCommandName() {
        return "togglesneak";
    }

    public String getCommandUsage(final ICommandSender sender) {
        return "/" + this.getCommandName();
    }

    public void processCommand(final ICommandSender sender, final String[] args) {
        this.mod.openMenu();
    }

    public List<String> getCommandAliases() {
        return Collections.singletonList("togglesneakmod");
    }
    public boolean canCommandSenderUseCommand(final ICommandSender sender) { return true; }
}
