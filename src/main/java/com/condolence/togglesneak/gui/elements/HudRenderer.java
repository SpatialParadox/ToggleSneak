package com.condolence.togglesneak.gui.elements;

import com.condolence.togglesneak.ToggleSneakMod;
import com.condolence.togglesneak.config.ToggleSneakSettings;
import com.condolence.togglesneak.gui.screens.GuiHudPositionEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;

public class HudRenderer {
    private final ToggleSneakSettings settings;
    private final Minecraft mc;

    public HudRenderer(final ToggleSneakMod mod) {
        this.mc = Minecraft.getMinecraft();
        this.settings = mod.getSettings();
    }

    public void renderHud() {
        final HudText current = this.settings.getHudText();
        final FontRenderer renderer = this.mc.fontRendererObj;

        final int hudX = this.settings.getHudX();
        final int hudY = this.settings.getHudY();

        if (this.mc.currentScreen == null) {
            if (current == null) { return; }

            final String text = current.getDescription().equals("Flying (boost)") ? current.getText().replace("{AMOUNT}", "" + (int) Math.round(this.settings.getFlyBoostAmount())) : current.getText();

            if (current.isChroma()) {
                int x = hudX;

                for (final char c : text.toCharArray()) {
                    long dif = x * 10 - hudY * 10;
                    final long l = System.currentTimeMillis() - dif;
                    final float ff = 2000.0f;
                    final int i = Color.HSBtoRGB(l % (int)ff / ff, 0.8f, 0.8f);
                    final String tmp = String.valueOf(c);
                    renderer.drawString(tmp, (float)x, (float)hudY, i, true);
                    x += (renderer.getCharWidth(c));
                }
            } else {
                renderer.drawString(text, (float)hudX, (float)hudY, current.getColor(), true);
            }
        }
        else if (this.mc.currentScreen instanceof GuiHudPositionEditor) {
            final HudText sprintToggledText = this.settings.getSpecificHudText("SprintToggledText");
            renderer.drawString(sprintToggledText.getText(), (float)hudX, (float)hudY, sprintToggledText.getColor(), true);
        }
    }
}
