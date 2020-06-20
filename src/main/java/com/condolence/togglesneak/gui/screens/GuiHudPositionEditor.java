package com.condolence.togglesneak.gui.screens;

import com.condolence.togglesneak.ToggleSneakMod;
import com.condolence.togglesneak.config.ToggleSneakSettings;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class GuiHudPositionEditor extends GuiScreen {
    private final ToggleSneakMod mod;
    private final ToggleSneakSettings settings;

    private boolean dragging;
    private int lastX;
    private int lastY;

    public  GuiHudPositionEditor(final ToggleSneakMod mod) {
        this.mod = mod;
        this.settings = this.mod.getSettings();
    }

    public void initGui() {
        final GuiButton saveButton = new GuiButton(0, this.getElementXCenter(100), this.height - 25, 100, 20, "Save Position");
        this.buttonList.add(saveButton);
    }

    private int getCenter() {
        return this.width / 2;
    }
    private int getElementXCenter(final int elementWidth) { return (this.width / 2) - (elementWidth / 2); }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawDefaultBackground();

        // DRAW TEXT
        this.drawCenteredString(this.mc.fontRendererObj, "ToggleSneak", this.getCenter(), 20, 2201331);
        this.drawCenteredString(this.mc.fontRendererObj, this.mod.getVersion(), this.getCenter(), 30, 3162015);

        // UPDATE DRAG POSITION
        this.updateDraggedModulePosition(mouseX, mouseY);

        // DRAW SCREEN
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void actionPerformed(final GuiButton button) {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiMainMenu(this.mod));
        }
    }

    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.settings.contains(mouseX, mouseY)) { this.dragging = true; }
    }

    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.dragging = false;
    }

    protected void updateDraggedModulePosition(final int mouseX, final int mouseY) {
        if (this.dragging) {
            this.settings.updateHudPosition(mouseX - this.lastX, mouseY - this.lastY);
        }
        this.lastX = mouseX;
        this.lastY = mouseY;
    }

    public void onGuiClosed() {
        this.settings.saveConfig();
    }
}
