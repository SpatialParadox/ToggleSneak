package com.condolence.togglesneak;

import com.condolence.togglesneak.config.ToggleSneakSettings;
import com.condolence.togglesneak.gui.elements.HudRenderer;
import com.condolence.togglesneak.listeners.ToggleSneakListener;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = ToggleSneakMod.MOD_ID, version = ToggleSneakMod.VERSION, name = ToggleSneakMod.NAME)
public class ToggleSneakMod {
    public static final String MOD_ID = "togglesneak";
    public static final String VERSION = "1.0";
    public static final String NAME = "Toggle Sneak";

    private static final Logger logger = LogManager.getLogger("ToggleSneak");

    private ToggleSneakSettings settings;
    private ToggleSneakListener listener;
    private HudRenderer hudRenderer;

    public ToggleSneakMod() {
        this.settings = new ToggleSneakSettings();
        this.hudRenderer = new HudRenderer(this);
        this.listener = new ToggleSneakListener(this);
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this.listener);
        ClientCommandHandler.instance.registerCommand(new ToggleSneakCommand(this));
        this.settings.loadConfig();

        logger.info("Hello from ToggleSneak! - Successfully initialized.");
    }

    public ToggleSneakSettings getSettings() {
        return this.settings;
    }

    public HudRenderer getHudRenderer() { return this.hudRenderer; }

    public Logger getLogger() { return ToggleSneakMod.logger; }

    public String getVersion() { return ToggleSneakMod.VERSION; }

    public void openMenu() { this.listener.setOpenMenu(true); }
}
