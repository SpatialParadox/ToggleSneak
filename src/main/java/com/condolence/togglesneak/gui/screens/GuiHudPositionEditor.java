package com.condolence.togglesneak.gui.screens;

import com.condolence.togglesneak.ToggleSneakMod;
import com.condolence.togglesneak.config.ToggleSneakSettings;
import com.condolence.togglesneak.gui.elements.GuiTransButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

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
        this.buttonList.add(new GuiTransButton(0, this.getCenter() - 50, this.height - 22, 100, 20, "Save Position"));
    }

    public int getCenter() {
        return this.width / 2;
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final String modLabel = "ToggleSneak Mod";

        super.drawDefaultBackground();
        this.updateDraggedModulePosition(mouseX, mouseY);
        GL11.glPushMatrix();
        GL11.glTranslatef(1.0f * -this.getCenter(), -25.0f, 0.0f);
        GL11.glScaled(2.0, 2.0, 2.0);
        this.drawCenteredString(this.mc.fontRendererObj, modLabel, this.getCenter(), 25, 815000);
        GL11.glPopMatrix();
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
